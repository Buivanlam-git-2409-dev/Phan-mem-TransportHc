package com.transporthc.controller.salary;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.salary.SalaryUserDto;
import com.transporthc.dto.salary.UpdateSalaryDto;
import com.transporthc.service.salary.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @GetMapping("/get-all-user")
    public ResponseEntity<BaseResponse<List<SalaryUserDto>>> getAllSalaryWithUserPeriod(@RequestParam String period){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.getAllSalaryWithUserPeriod(period))
        );
    }

    @GetMapping("/get-one")
    public ResponseEntity<BaseResponse<SalaryUserDto>> getSalaryWithUser(@RequestParam Integer salaryId){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.getSalaryWithUser(salaryId))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<BaseResponse<SalaryUserDto>> updateSalary(@RequestParam Integer salaryId, @RequestBody UpdateSalaryDto updateSalaryDto){
        return ResponseEntity.ok(
                BaseResponse.ok(salaryService.updateSalary(salaryId, updateSalaryDto))
        );
    }

    @GetMapping("/create-all-user")
    public ResponseEntity<BaseResponse<String>> createSalaryForAllUsers(){
        salaryService.createSalaryForAllUsers();
        return ResponseEntity.ok(
                BaseResponse.ok("Tạo thành công lương tháng cho tất cả user với giá trị default rồi he")
        );
    }
}