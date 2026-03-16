package com.transporthc.repository;

import jakarta.persistence.EntityManager;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.jpa.impl.JPAQueryFactory;

@NoRepositoryBean
public class BaseRepo {
    protected EntityManager entityManager;
    protected JPAQueryFactory query;

    public BaseRepo(EntityManager entityManager){
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }
}
