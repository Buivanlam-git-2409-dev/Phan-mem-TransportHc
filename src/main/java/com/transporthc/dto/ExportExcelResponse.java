package com.transporthc.dto;

import org.springframework.core.io.InputStreamResource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder@AllArgsConstructor@NoArgsConstructor
public class ExportExcelResponse {
    private String filename;
    private InputStreamResource resource;
}
