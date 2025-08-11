package com.transporthc.repository.products.productsReport;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.entity.products.ProductsReportEntity;
import com.transporthc.repository.BaseRepo;
import com.transporthc.repository.products.products.ProductsRepoCustom;

import jakarta.persistence.EntityManager;

import static com.transporthc.entity.products.QProductsEntity.productsEntity;
import static com.transporthc.entity.products.QProductsReportEntity.productsReportEntity;
import static com.transporthc.transaction.QTransactionEntity.transactionEntity;

import java.util.Date;
import java.util.List;

@Repository
public class ProductsReportRepoImpl extends BaseRepo implements ProductsRepoCustom{
    public ProductsReportRepoImpl(EntityManager entityManager){
        super(entityManager);
    }
    BooleanBuilder initBuilder(Date fromDate, Date toDate){
        return new BooleanBuilder().and(productsReportEntity.createAt.between(fromDate,toDate));
    }

    @Override
    public ProductsReportEntity getProductReport(String productsId, Date fromdate,Date toDate){
        BooleanBuilder builder = initBuilder(fromdate, toDate)
        .and(productsReportEntity.productsId.eq(productsId));

        return query.from(productsReportEntity)
        .where(builder)
        .select(productsReportEntity)
        .fetchOne();
    }

    @Override
    public List<ProductsReportDto> getProductReportDto(Date fromDate, Date toDate) {
        BooleanBuilder builder = initBuilder(fromDate, toDate);

        ConstructorExpression<ProductsReportDto> expression = Projections.constructor(ProductsReportDto.class,
                productsReportEntity.id.as("id"),
                productsReportEntity.productsId.as("productsId"),
                JPAExpressions.select(productsEntity.name.as("productsEntityName"))
                        .from(productsEntity)
                        .where(productsEntity.id.eq(productsReportEntity.productsId)),
                productsReportEntity.beginningInventory.coalesce(0F).as("beginingInventoryQuantity"),
                JPAExpressions.select((productsEntity.amount.multiply(productsReportEntity.beginningInventory)).coalesce(0F).as("beginingInventoryTotalAmount"))
                        .from(productsEntity)
                        .where(productsEntity.id.eq(productsReportEntity.productsId)),
                JPAExpressions.select((transactionEntity.quantity.sum()).coalesce(0F).as("inboundTransactionQuantity"))
                        .from(transactionEntity)
                        .where(transactionEntity.origin.eq(true)
                                .and(transactionEntity.productsId.eq(productsReportEntity.productsId))),
                JPAExpressions.select(
                                transactionEntity.quantity.sum()
                                        .multiply(
                                                JPAExpressions.select(productsEntity.amount)
                                                        .from(productsEntity)
                                                        .where(productsEntity.id.eq(transactionEntity.productsId))
                                        ).coalesce(0F).as("inboundTransactionTotalAmount")
                        )
                        .from(transactionEntity)
                        .where(transactionEntity.origin.eq(true)
                                .and(transactionEntity.productsId.eq(productsReportEntity.productsId))),
                JPAExpressions.select((transactionEntity.quantity.sum()).coalesce(0F).as("outboundTransactionQuantity"))
                        .from(transactionEntity)
                        .where(transactionEntity.origin.eq(false)
                                .and(transactionEntity.productsId.eq(productsReportEntity.productsId))),
                JPAExpressions.select(
                                transactionEntity.quantity.sum()
                                        .multiply(
                                                JPAExpressions.select(productsEntity.amount)
                                                        .from(productsEntity)
                                                        .where(productsEntity.id.eq(transactionEntity.productsId))
                                        ).coalesce(0F).as("outboundTransactionTotalAmount")
                        )
                        .from(transactionEntity)
                        .where(transactionEntity.origin.eq(false)
                                .and(transactionEntity.productsId.eq(productsReportEntity.productsId)))
                ,
                productsReportEntity.endingInventory.coalesce(0F).as("endingInventoryQuantity"),
                JPAExpressions.select((productsEntity.amount.multiply(productsReportEntity.endingInventory)).coalesce(0F).as("endingInventoryTotalAmount"))
                        .from(productsEntity)
                        .where(productsEntity.id.eq(productsReportEntity.productsId)),
                JPAExpressions.select(productsEntity.amount.coalesce(0F).as("unitAmount"))
                        .from(productsEntity)
                        .where(productsEntity.id.eq(productsReportEntity.productsId)),
                productsReportEntity.createdAt.as("createdAt")
                );

        return query.from(productsReportEntity)
                .where(builder)
                .select(expression)
                .fetch();
    }
}
