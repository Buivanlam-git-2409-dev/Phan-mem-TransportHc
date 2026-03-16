package com.transporthc.service.attached;

import java.rmi.ServerException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.entity.attached.AttachedImg;
import com.transporthc.enums.attached.AttachedTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.attached.AttachedImgMapper;
import com.transporthc.repository.attached.AttachedImgRepo;
import com.transporthc.service.upload.FileStorageServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachedImgServiceImpl implements AttachedImgService {
    private final AttachedImgRepo attachedImgRepo;
    private final AttachedImgMapper attachedImgMapper;
    private final FileStorageServiceImpl fileStorageService;

    @Override
    public List<AttachedImg> addAttachedImages(String referenceId, AttachedTypeEnum type, AttachedImgPathDto attachedImagePathsDTO) {
        String[] attachedImagePaths = attachedImagePathsDTO.getAttachedImgPaths();
        return addAttachedImages(referenceId, type, attachedImagePaths);
    }

    public List<AttachedImg> addAttachedImages(String referenceId, AttachedTypeEnum type, String[] attachedImagePaths) {
        List<AttachedImg> attachedImages = attachedImgMapper.toAttchedImgs(referenceId, type, attachedImagePaths);
        return attachedImgRepo.saveAll(attachedImages);
    }

    @Override
    @Transactional
    public String deleteAttachedImages(String referenceId, AttachedImgPathDto attachedImagePathsDTO) throws ServerException {
        String[] attachedImagePaths = attachedImagePathsDTO.getAttachedImgPaths();
        long numOfRowsDeleted = attachedImgRepo.deleteAttachedImgs(referenceId, attachedImagePaths);
        if (numOfRowsDeleted == 0) {
            throw new NotFoundException("Ảnh minh chứng không tồn tại!");
        }
        fileStorageService.deleteFiles(attachedImagePaths);
        return numOfRowsDeleted + "/" + attachedImagePaths.length;
    }
}