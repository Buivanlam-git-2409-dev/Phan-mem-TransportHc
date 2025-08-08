package com.transporthc.dto.attached;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor
public class AttachedImgPathDto {
    @NotEmpty(message = "Vui lòng cung cấp ít nhất một ảnh minh chứng")
    private String[] attachedImgPaths;
}
