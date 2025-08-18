package com.transporthc.controller.attached;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.enums.attached.AttachedTypeEnum;
import com.transporthc.service.attached.AttachedImgService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;


@RestController
@RequestMapping("/attached_images")
@RequiredArgsConstructor
public class AttachedImgController {
    private final AttachedImgService attachedImgService;
    
    @PostMapping("/{refId}")
    public ResponseEntity<Object> attachImages(@PathVariable String refId,@RequestParam AttachedTypeEnum type,@Valid AttachedImgPathDto dto) {
        return ResponseEntity.ok(
            BaseResponse.ok(attachedImgService.addAttachedImages(refId, type, dto))
        );      
    }
    @PostMapping("/delete/{refId}")
    public ResponseEntity<Object> deleteAttachedImages(@PathVariable String refId,@RequestBody AttachedImgPathDto dto) throws ServerException{
        String result = attachedImgService.deleteAttachedImages(refId, dto);
        return ResponseEntity.ok(BaseResponse.ok(result,"Đã xóa thành công "+result+" ảnh minh chứng!"));
    }
}
