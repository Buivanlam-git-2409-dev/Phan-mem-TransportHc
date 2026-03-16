package com.transporthc.controller.expenses;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.expenses.ExpensesAdvancesDto;
import com.transporthc.service.expenses.expenseAdvances.ExpenseAdvancesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses/advances")
@RequiredArgsConstructor
public class ExpensesAdvancesController {
    private final ExpenseAdvancesService expenseAdvancesService;

    @GetMapping()
    public ResponseEntity<Object> getExpenseAdvances(@RequestParam int page) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.getAll(page))
        );
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<Object> getExpenseAdvancesByID(@PathVariable String driverId) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.getByDriverId(driverId))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpenseAdvances(@Valid @RequestBody ExpensesAdvancesDto dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expenseAdvancesService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpenseAdvances(@PathVariable Integer id, @Valid @RequestBody ExpensesAdvancesDto dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpenseAdvancesByID(@PathVariable Integer id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expenseAdvancesService.delete(id))
        );
    }
}