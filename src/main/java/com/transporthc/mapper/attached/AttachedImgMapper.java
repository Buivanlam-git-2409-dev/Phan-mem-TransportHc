package com.transporthc.mapper.attached;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.transporthc.entity.attached.AttachedImg;
import com.transporthc.enums.attached.AttachedTypeEnum;

@Component
public class AttachedImgMapper {
    public List<AttachedImg> toAttchedImgs(String referenceId,AttachedTypeEnum type,String[] attachedImgPaths){
        List<AttachedImg> imgs = new ArrayList<>();
        for(String path: attachedImgPaths){
            AttachedImg img = AttachedImg.builder()
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
