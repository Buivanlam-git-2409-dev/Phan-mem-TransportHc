package com.transporthc.mapper.attached;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.transporthc.entity.attached.AttachedImgEntity;
import com.transporthc.enums.attached.AttachedTypeEnum;

@Component
public class AttachedImgMapper {
    public List<AttachedImgEntity> toAttchedImgs(String referenceId,AttachedTypeEnum type,String[] attachedImgPaths){
        List<AttachedImgEntity> imgs = new ArrayList<>();
        for(String path: attachedImgPaths){
            AttachedImgEntity img = AttachedImgEntity.builder()
            .referenceId(referenceId)
            .type(type.getValue())
            .imgPath(path.trim())
            .uploadedAt(new Date())
            .build();

            imgs.add(img);
        }
        return imgs;
    }
}
