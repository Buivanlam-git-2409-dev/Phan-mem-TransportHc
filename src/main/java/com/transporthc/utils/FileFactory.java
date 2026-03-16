package com.transporthc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileFactory {
    // C:\\Users\\fptsh\intellij-workspace\\
    public static final String PATH_TEMPLATE = "";

    public static File createFile(String fileName,Workbook wb) throws Exception{
        wb = new XSSFWorkbook();
        OutputStream out = new FileOutputStream(PATH_TEMPLATE + fileName);
        wb.write(out);
        return ResourceUtils.getFile(PATH_TEMPLATE+fileName);
    }
    public static Workbook getWorkbookStream(MultipartFile importFile){
        InputStream is = null;
        try{
            is = importFile.getInputStream();
            Workbook wb = WorkbookFactory.create(is);
            return wb;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
