package com.transporthc.service.attached;

import java.rmi.ServerException;
import java.util.List;

import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.entity.attached.AttachedImg;
import com.transporthc.enums.attached.AttachedTypeEnum;

public interface AttachedImgService {
   List<AttachedImg> addAttachedImages(String referenceId, AttachedTypeEnum type, AttachedImgPathDto attachedImagePathsDTO);
    List<AttachedImg> addAttachedImages(String referenceId, AttachedTypeEnum type, String[] attachedImagePaths);
    String deleteAttachedImages(String referenceId, AttachedImgPathDto attachedImagePathsDTO) throws ServerException;
}
