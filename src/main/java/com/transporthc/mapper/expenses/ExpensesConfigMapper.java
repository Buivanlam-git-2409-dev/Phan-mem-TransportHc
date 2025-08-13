package com.transporthc.mapper.expenses;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.entity.expenses.ExpenseConfigEntity;
import com.transporthc.enums.IDKey;
import com.transporthc.utils.utils;

@Component
public class ExpensesConfigMapper {

    public ExpenseConfigEntity toExpenseConfigEntity(ExpensesConfigDto dto){
        if(dto==null) return null;
        return ExpenseConfigEntity.builder()
        .id(utils.genID(IDKey.EXPENSES_CONFIG))
        .type(dto.getType())
        .note(dto.getNote())
        .build();
    }

    public List<ExpenseConfigEntity> toExpenseConfigEntitiesList(List<ExpensesConfigDto> dtos){
        if(dtos==null || dtos.isEmpty()){
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toExpenseConfigEntity).collect(Collectors.toList());
    }
    public void updateExpensesConfig(ExpenseConfigEntity config,ExpensesConfigDto dto){
        if(dto==null) return;
        if(dto.getType()!=null&&!dto.getType().equals(config.getType())){
            config.setType(dto.getType());
        }
        if(dto.getNote()!=null&&!dto.getNote().equals(config.getNote())){
            config.setNote(dto.getNote());
        }
    }
}
