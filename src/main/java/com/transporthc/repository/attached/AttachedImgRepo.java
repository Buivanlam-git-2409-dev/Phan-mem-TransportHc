package com.transporthc.repository.attached;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.attached.AttachedImg;


@Repository
public interface AttachedImgRepo extends JpaRepository<AttachedImg,Integer>, AttachedImgRepoCustom{
    
}
