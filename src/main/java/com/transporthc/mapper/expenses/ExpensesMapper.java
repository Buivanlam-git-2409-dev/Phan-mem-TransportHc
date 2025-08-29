package com.transporthc.mapper.expenses;

import com.transporthc.utils.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.entity.expenses.Expenses;
import com.transporthc.enums.IDKey;
import com.transporthc.enums.expenses.ExpensesStatusEnum;

@Component
public class ExpensesMapper {

    public Expenses toExpensesEntity(ExpensesDto dto){
        if(dto == null) return null;
        return Expenses.builder()
        .id(utils.genID(IDKey.EXPENSES))
        .scheduleId(dto.getScheduleId())
        .expensesConfigId(dto.getExpensesConfigId())
        .amount(dto.getAmount())
        .note(dto.getNote())
        .status(ExpensesStatusEnum.PENDING.getValue())
        .build();
    }

    public List<Expenses> toExpensesEntities(List<ExpensesDto> dtos){
        if(dtos == null || dtos.isEmpty()){
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toExpensesEntity).collect(Collectors.toList());
    }

    public void updateExpenses(Expenses exe,ExpensesDto exdto){
        if(exdto==null) return;
        if(exdto.getScheduleId()!=null&&!exdto.getScheduleId().equals(exe.getScheduleId())){
            exe.setScheduleId(exdto.getScheduleId());
        }
        if(exdto.getExpensesConfigId()!=null && !exdto.getExpensesConfigId().equals(exe.getExpensesConfigId())){
            exe.setExpensesConfigId(exdto.getExpensesConfigId());
        }
        if(exdto.getAmount()!=null && !exdto.getAmount().equals(exe.getAmount())){
            exe.setAmount(exdto.getAmount());
        }
        if(exdto.getNote()!=null && !exdto.getNote().equals(exe.getNote())){
            exe.setNote(exdto.getNote());
        }
    }
}
