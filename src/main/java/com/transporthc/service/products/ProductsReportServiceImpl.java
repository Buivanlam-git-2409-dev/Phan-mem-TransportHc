package com.transporthc.service.products;

import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.entity.products.Products;
import com.transporthc.entity.products.ProductsReport;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.repository.products.productsReport.ProductsReportRepo;
import com.transporthc.repository.products.products.ProductsRepo;
import com.transporthc.repository.transaction.TransactionRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.utils;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsReportServiceImpl extends BaseService implements ProductsReportService {

    private final ProductsRepo productsRepo;
    private final TransactionRepo transactionRepo;
    private final ProductsReportRepo productsReportRepo;
    private final PermissionTypeEnum type = PermissionTypeEnum.REPORTS;

    @Override
    public void createProductsReport(String period) {
        Date[] range = utils.createDateRange(period);

        List<Products> productsList = productsRepo.findAll();
        for (Products products : productsList) {
            ProductsReport productsReportMinusMonths = productsReportRepo.getProductReport(products.getId(), range[0], range[1]);
            ProductsReport productsReportCurrentMonths = new ProductsReport();
            float inboundQuantity = transactionRepo.getQuantityByOrigin(products.getId(), true, range[0], range[1]);
            float outboundQuantity = transactionRepo.getQuantityByOrigin(products.getId(), false, range[0], range[1]);
            if(productsReportMinusMonths == null) {
                productsReportCurrentMonths.setBeginningInventory(products.getQuantity() - inboundQuantity + outboundQuantity);
                productsReportCurrentMonths.setEndingInventory(products.getQuantity());
                productsReportCurrentMonths.setProductsId(products.getId());
                productsReportCurrentMonths.setCreatedAt(new Date());
                productsReportRepo.save(productsReportCurrentMonths);
            } else {
                productsReportCurrentMonths.setBeginningInventory(productsReportMinusMonths.getEndingInventory());
                productsReportCurrentMonths.setEndingInventory(productsReportMinusMonths.getBeginningInventory() + inboundQuantity - outboundQuantity);
                productsReportCurrentMonths.setProductsId(products.getId());
                productsReportCurrentMonths.setCreatedAt(new Date());
                productsReportRepo.save(productsReportCurrentMonths);
            }
        }
    }

    @Override
    public List<ProductsReportDto> getProductsReport(String period) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        return productsReportRepo.getProductReportDto(range[0], range[1]);
    }

    @Override
    public ExportExcelResponse exportProductsReport(String period) throws Exception {
        List<ProductsReportDto> productsReports = getProductsReport(period);

        if (CollectionUtils.isEmpty(productsReports)) {
            throw new NotFoundException("No data");
        }
        String fileName = "ProductsReport Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(productsReports, fileName, ExportConfig.productsReportExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}