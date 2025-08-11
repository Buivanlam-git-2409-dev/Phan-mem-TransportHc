package com.transporthc.repository.salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.salary.SalaryEntity;

@Repository
public interface SalaryRepo extends JpaRepository<SalaryEntity, Integer>, SalaryRepoCustom{
    
}
