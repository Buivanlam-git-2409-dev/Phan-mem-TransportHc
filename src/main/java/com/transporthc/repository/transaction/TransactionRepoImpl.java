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
import com.transporthc.entity.transaction.TransactionEntity;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static com.transporthc.entity.products.QProductsEntity.productsEntity;
import static com.transporthc.entity.transaction.QTransactionEntity.transactionEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;

@Repository
public class TransactionRepoImpl extends BaseRepo implements TransactionRepoCustom {
    public TransactionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TransactionDto> transactionProjection() {
        return Projections.constructor(TransactionDto.class,
                transactionEntity.id.as("id"),
                transactionEntity.refUserId.as("refUserId"),
                JPAExpressions.select(userEntity.fullName.as("fullNameRefUser"))
                        .from(userEntity)
                        .where(userEntity.id.eq(transactionEntity.refUserId)),
                transactionEntity.productsEntityId.as("productsEntityId"),
                JPAExpressions.select(productsEntity.name.as("productsEntityName"))
                        .from(productsEntity)
                        .where(productsEntity.id.eq(transactionEntity.productsEntityId)),
                transactionEntity.quantity.as("quantity"),
                transactionEntity.destination.as("destination"),
                transactionEntity.customerName.as("customerName"),
                transactionEntity.transactionTime.as("transactionTime"),
                transactionEntity.origin.as("origin"), //true f
                new CaseBuilder()
                        .when(transactionEntity.origin.eq(true)).then(TransactionType.INBOUND_TRANSACTION.getTitle())
                        .otherwise(TransactionType.OUTBOUND_TRANSACTION.getTitle()).as("originDescription"),
                transactionEntity.image.as("image"),
                transactionEntity.createdAt.as("createdAt"),
                transactionEntity.updatedAt.as("updatedAt")
        );
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(transactionEntity.id.eq(id))
                .and(transactionEntity.deleted.eq(false));
    }

    @Override
    @Modifying
    @Transactional
    public long updateTransaction(TransactionEntity OldTransaction, String id, TransactionDto dto) {
        BooleanBuilder builder = initGetOneBuilder(id);

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, transactionEntity);

        boolean isUpdated = false;
        boolean isChanged = false;

        if (dto.getRefUserId() != null) {
            if(!dto.getRefUserId().equals(OldTransaction.getRefUserId())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.refUserId, dto.getRefUserId());
            isUpdated = true;
        }
        if (dto.getCustomerName() != null) {
            if(!dto.getCustomerName().equals(OldTransaction.getCustomerName())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.customerName, dto.getCustomerName());
            isUpdated = true;
        }
        if (dto.getGoodsId() != null) {
            if(!dto.getGoodsId().equals(OldTransaction.getProductsId())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.productsEntityId, dto.getGoodsId());
            isUpdated = true;
        }
        if (dto.getQuantity() != null) {
            if(!dto.getQuantity().equals(OldTransaction.getQuantity())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.quantity, dto.getQuantity());
            isUpdated = true;
        }
        if (dto.getDestination() != null) {
            if(!dto.getDestination().equals(OldTransaction.getDestination())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.destination, dto.getDestination());
            isUpdated = true;
        }
        if (dto.getImage() != null) {
            if(!dto.getImage().equals(OldTransaction.getImage())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.image, dto.getImage());
            isUpdated = true;
        }
        if (dto.getOrigin() != null) {
            if(!dto.getOrigin().getValue().equals(OldTransaction.getOrigin())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.origin, dto.getOrigin().getValue());
            isUpdated = true;
        }
        if (dto.getTransactionTime() != null) {
            if(!dto.getTransactionTime().equals(OldTransaction.getTransactionTime())){
                isChanged = true;
            }
            updateClause.set(transactionEntity.transactionTime, dto.getTransactionTime());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(transactionEntity.updatedAt, new Date());
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
                query.from(transactionEntity)
                .where(builder)
                .select(transactionProjection())
                .fetchOne()
        );
    }

    @Override
    public List<TransactionDto> getTransactionByFilter(int page, String warehouseId, Boolean origin, Date fromDate, Date toDate) {

        BooleanBuilder builder = new BooleanBuilder()
                .and(transactionEntity.deleted.eq(false));

        if (warehouseId != null) {
            builder.and(productsEntity.warehouseId.eq(warehouseId));
        }

        if (origin != null) {
            builder.and(transactionEntity.origin.eq(origin));
        }

        if (fromDate != null && toDate != null) {
            builder.and(transactionEntity.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(transactionEntity.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(transactionEntity.createdAt.loe(toDate));
        }

        long offset = (long) (page - 1) * Pagination.TEN.getSize();

        return query.from(transactionEntity)
                .leftJoin(productsEntity).on(productsEntity.id.eq(transactionEntity.productsEntityId))
                .where(builder)
                .select(transactionProjection())
                .orderBy(transactionEntity.updatedAt.desc())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long deleteTransaction(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(transactionEntity)
                .where(builder)
                .set(transactionEntity.deleted, true)
                .execute();
    }

    @Override
    public Float getQuantityByOrigin(String productsEntityId, Boolean origin , Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(transactionEntity.origin.eq(origin))
                .and(transactionEntity.productsEntityId.eq(productsEntityId));
        if (fromDate != null && toDate != null) {
            builder.and(transactionEntity.createdAt.between(fromDate, toDate));
        }

        Float totalQuantity = query.from(transactionEntity)
                .where(builder)
                .select(transactionEntity.quantity.sum())
                .fetchOne();

        return totalQuantity != null ? totalQuantity : 0f;
    }
}