package com.transporthc.repository.attached;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.attached.AttachedImgEntity;


@Repository
public interface AttachedImgRepo extends JpaRepository<AttachedImgEntity,Integer>, AttachedImgRepoCustom{
    
}
