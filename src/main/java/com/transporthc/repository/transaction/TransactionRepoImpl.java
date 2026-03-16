package com.transporthc.repository.transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.entity.transaction.Transaction;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static com.transporthc.entity.products.QProducts.products;
import static com.transporthc.entity.transaction.QTransaction.transaction;
import static com.transporthc.entity.user.QUser.user;

@Repository
public class TransactionRepoImpl extends BaseRepo implements TransactionRepoCustom {
    public TransactionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TransactionDto> transactionProjection() {
        return Projections.constructor(TransactionDto.class,
                transaction.id.as("id"),
                transaction.refUserId.as("refUserId"),
                JPAExpressions.select(user.fullName.as("fullNameRefUser"))
                        .from(user)
                        .where(user.id.eq(transaction.refUserId)),
                transaction.productsId.as("productsId"),
                JPAExpressions.select(products.name.as("productsName"))
                        .from(products)
                        .where(products.id.eq(transaction.productsId)),
                transaction.quantity.as("quantity"),
                transaction.destination.as("destination"),
                transaction.customerName.as("customerName"),
                transaction.transactionTime.as("transactionTime"),
                transaction.origin.as("origin"), //true f
                new CaseBuilder()
                        .when(transaction.origin.eq(true)).then(TransactionType.INBOUND_TRANSACTION.getTitle())
                        .otherwise(TransactionType.OUTBOUND_TRANSACTION.getTitle()).as("originDescription"),
                transaction.image.as("image"),
                transaction.createdAt.as("createdAt"),
                transaction.updatedAt.as("updatedAt")
        );
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(transaction.id.eq(id))
                .and(transaction.deleted.eq(false));
    }

    @Override
    @Modifying
    @Transactional
    public long updateTransaction(Transaction OldTransaction, String id, TransactionDto dto) {
        BooleanBuilder builder = initGetOneBuilder(id);

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, transaction);

        boolean isUpdated = false;
        boolean isChanged = false;

        if (dto.getRefUserId() != null) {
            if(!dto.getRefUserId().equals(OldTransaction.getRefUserId())){
                isChanged = true;
            }
            updateClause.set(transaction.refUserId, dto.getRefUserId());
            isUpdated = true;
        }
        if (dto.getCustomerName() != null) {
            if(!dto.getCustomerName().equals(OldTransaction.getCustomerName())){
                isChanged = true;
            }
            updateClause.set(transaction.customerName, dto.getCustomerName());
            isUpdated = true;
        }
        if (dto.getGoodsId() != null) {
            if(!dto.getGoodsId().equals(OldTransaction.getProductsId())){
                isChanged = true;
            }
            updateClause.set(transaction.productsId, dto.getGoodsId());
            isUpdated = true;
        }
        if (dto.getQuantity() != null) {
            if(!dto.getQuantity().equals(OldTransaction.getQuantity())){
                isChanged = true;
            }
            updateClause.set(transaction.quantity, dto.getQuantity());
            isUpdated = true;
        }
        if (dto.getDestination() != null) {
            if(!dto.getDestination().equals(OldTransaction.getDestination())){
                isChanged = true;
            }
            updateClause.set(transaction.destination, dto.getDestination());
            isUpdated = true;
        }
        if (dto.getImage() != null) {
            if(!dto.getImage().equals(OldTransaction.getImage())){
                isChanged = true;
            }
            updateClause.set(transaction.image, dto.getImage());
            isUpdated = true;
        }
        if (dto.getOrigin() != null) {
            if(!dto.getOrigin().getValue().equals(OldTransaction.getOrigin())){
                isChanged = true;
            }
            updateClause.set(transaction.origin, dto.getOrigin().getValue());
            isUpdated = true;
        }
        if (dto.getTransactionTime() != null) {
            if(!dto.getTransactionTime().equals(OldTransaction.getTransactionTime())){
                isChanged = true;
            }
            updateClause.set(transaction.transactionTime, dto.getTransactionTime());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(transaction.updatedAt, new Date());
        } else {
            throw new IllegalArgumentException("No data fields are updated!!!");
        }

        if(!isChanged) {
            throw new EditNotAllowedException("Data is not changed!!!");
        }

        return updateClause.where(builder).execute();
    }

    @Override
    public Optional<TransactionDto> getTransactionsById(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);

        return Optional.ofNullable(
                query.from(transaction)
                .where(builder)
                .select(transactionProjection())
                .fetchOne()
        );
    }

    @Override
    public List<TransactionDto> getTransactionByFilter(int page, String warehouseId, Boolean origin, Date fromDate, Date toDate) {

        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.deleted.eq(false));

        if (warehouseId != null) {
            builder.and(products.warehouseId.eq(warehouseId));
        }

        if (origin != null) {
            builder.and(transaction.origin.eq(origin));
        }

        if (fromDate != null && toDate != null) {
            builder.and(transaction.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(transaction.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(transaction.createdAt.loe(toDate));
        }

        long offset = (long) (page - 1) * Pagination.TEN.getSize();

        return query.from(transaction)
                .leftJoin(products).on(products.id.eq(transaction.productsId))
                .where(builder)
                .select(transactionProjection())
                .orderBy(transaction.updatedAt.desc())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long deleteTransaction(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(transaction)
                .where(builder)
                .set(transaction.deleted, true)
                .execute();
    }

    @Override
    public Float getQuantityByOrigin(String productsId, Boolean origin , Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transaction.origin.eq(origin))
                .and(transaction.productsId.eq(productsId));
        if (fromDate != null && toDate != null) {
            builder.and(transaction.createdAt.between(fromDate, toDate));
        }

        Float totalQuantity = query.from(transaction)
                .where(builder)
                .select(transaction.quantity.sum())
                .fetchOne();

        return totalQuantity != null ? totalQuantity : 0f;
    }
}