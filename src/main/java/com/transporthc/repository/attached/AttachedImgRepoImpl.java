package com.transporthc.repository.attached;

import org.springframework.data.jpa.repository.Modifying;

import com.querydsl.core.BooleanBuilder;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static com.transporthc.entity.attached.QAttachedImgEntity.attachedImgEntity;

public class AttachedImgRepoImpl extends BaseRepo implements AttachedImgRepoCustom{
    public AttachedImgRepoImpl(EntityManager entityManager){
        super(entityManager);
    }
    @Override
    @Transactional
    @Modifying
    public long deleteAttachedImgs(String referenceId, String[] attachedImgPaths){
        BooleanBuilder builder = new BooleanBuilder()
        .and(attachedImgEntity.referenceId.eq(referenceId))
        .and(attachedImgEntity.imgPath.in(attachedImgPaths));

        return query.delete(attachedImgEntity).where(builder).excute();
    }
}
