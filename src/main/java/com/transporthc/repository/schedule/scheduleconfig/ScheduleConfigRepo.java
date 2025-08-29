package com.transporthc.repository.schedule.scheduleconfig;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.schedule.ScheduleConfig;

@Repository
public interface ScheduleConfigRepo extends JpaRepository<ScheduleConfig, String>, ScheduleConfigRepoCustom{
    Optional<ScheduleConfig> findById(String id);
}
