package com.transporthc.service.attached;

import java.rmi.ServerException;
import java.util.List;

import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.entity.attached.AttachedImgEntity;
import com.transporthc.enums.attached.AttachedTypeEnum;

public interface AttachedImgService {
   List<AttachedImgEntity> addAttachedImages(String referenceId, AttachedTypeEnum type, AttachedImgPathDto attachedImagePathsDTO);
    List<AttachedImgEntity> addAttachedImages(String referenceId, AttachedTypeEnum type, String[] attachedImagePaths);
    String deleteAttachedImages(String referenceId, AttachedImgPathDto attachedImagePathsDTO) throws ServerException;
}
