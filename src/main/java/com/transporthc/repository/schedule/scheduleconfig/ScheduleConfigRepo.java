package com.transporthc.repository.schedule.scheduleconfig;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.schedule.ScheduleConfigEntity;

@Repository
public interface ScheduleConfigRepo extends JpaRepository<ScheduleConfigEntity, String>, ScheduleConfigRepoCustom{
    Optional<ScheduleConfigEntity> findById(String id);
}
