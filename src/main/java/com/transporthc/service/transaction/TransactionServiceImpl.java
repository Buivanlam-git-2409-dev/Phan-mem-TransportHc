package com.transporthc.service.transaction;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.entity.products.Products;
import com.transporthc.entity.transaction.Transaction;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.ConflictException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.transaction.TransactionMapper;
import com.transporthc.repository.products.products.ProductsRepo;
import com.transporthc.repository.transaction.TransactionRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import com.transporthc.utils.FileFactory;
import com.transporthc.utils.ImportConfig;
import com.transporthc.utils.utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl extends BaseService implements TransactionService {
    private final ProductsRepo productsRepo;
    private final TransactionRepo repository;
    private final TransactionMapper mapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.TRANSACTIONS;

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        Products products = productsRepo.findById(transactionDto.getGoodsId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

        if (transactionDto.getOrigin().getValue()) {
            products.setQuantity(products.getQuantity() + transactionDto.getQuantity());
            productsRepo.save(products);
        } else {
            if (products.getQuantity() < transactionDto.getQuantity()) {
                throw new ConflictException("Không đủ " + products.getName() + "trong kho");
            }
            products.setQuantity(products.getQuantity() - transactionDto.getQuantity());
            productsRepo.save(products);
        }

        Transaction transaction = mapper.toTransaction(transactionDto);
        repository.save(transaction);

        return repository.getTransactionsById(transaction.getId()).get();
    }


    @Override
    public Optional<TransactionDto> updateTransaction(String id, TransactionDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch"));

        Products products = productsRepo.findById(transaction.getProductsId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

        if (dto.getOrigin() != null && !transaction.getOrigin().equals(dto.getOrigin().getValue())) {
            throw new ConflictException("Không được cập nhật kiểu giao dịch");
        }

        if (dto.getQuantity() != null) {
            if (transaction.getOrigin()) {
                products.setQuantity(products.getQuantity() - transaction.getQuantity() + dto.getQuantity());
            } else if (products.getQuantity() < dto.getQuantity()) {
                throw new ConflictException("Không đủ hàng trong kho");
            } else {
                products.setQuantity(products.getQuantity() - transaction.getQuantity() + dto.getQuantity());
            }
        }

        productsRepo.save(products);

        long updatedCount = repository.updateTransaction(transaction, id, dto);
        if (updatedCount == 0) {
            throw new NotFoundException("Cập nhật giao dịch thất bại");
        }

        return repository.getTransactionsById(id);
    }

    @Override
    public String deleteTransaction(String id) {
        checkPermission(type, PermissionKeyEnum.DELETE);

        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giao dịch"));
        if (repository.deleteTransaction(id) > 0) {
            return "Xóa thành công";
        } else {
            throw new ConflictException("Xóa thất bại");
        }
    }

    @Override
    public TransactionDto getTransactionById(String id) {

        checkPermission(type, PermissionKeyEnum.VIEW);

        return repository.getTransactionsById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin giao dịch!"));
    }

    @Override
    public List<TransactionDto> getTransactionByFilter(int page, String warehouseId, Boolean origin, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(fromDateStr, toDateStr);
        return repository.getTransactionByFilter(page, warehouseId, origin, range[0], range[1]);
    }

    @Override
    public List<Transaction> importTransactionData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<TransactionDto> transactionDtoList = ExcelUtils.getImportData(workbook, ImportConfig.transactionImport);

        List<Transaction> transactions = mapper.toTransactions(transactionDtoList);

        for (Transaction transaction : transactions) {
            Products products = productsRepo.findById(transaction.getProductsId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy hàng hóa"));

            if (transaction.getOrigin()) {
                products.setQuantity(products.getQuantity() + transaction.getQuantity());
                productsRepo.save(products);
            } else {
                if (products.getQuantity() < transaction.getQuantity()) {
                    throw new ConflictException("Không đủ " + products.getName() + "trong kho");
                }
                products.setQuantity(products.getQuantity() - transaction.getQuantity());
                productsRepo.save(products);
            }
        }
        return repository.saveAll(transactions);
    }

    @Override
    public ExportExcelResponse exportTransaction(int page, String warehouseId, Boolean origin, String fromDate, String toDate) throws Exception {
        Date[] range = utils.createDateRange(fromDate, toDate);
        List<TransactionDto> transactions = repository.getTransactionByFilter(page, warehouseId, origin, range[0], range[1]);

        if (CollectionUtils.isEmpty(transactions)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Transactions Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(transactions, fileName, ExportConfig.transactionExport);
        InputStreamResource resource = new InputStreamResource(in);

        return new ExportExcelResponse(fileName, resource);
    }
}