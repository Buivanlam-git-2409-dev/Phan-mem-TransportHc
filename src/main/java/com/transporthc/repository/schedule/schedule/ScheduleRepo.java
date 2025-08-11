package com.transporthc.repository.schedule.schedule;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.schedule.ScheduleEntity;

import io.micrometer.common.lang.NonNull;

@Repository
public interface ScheduleRepo extends JpaRepository<ScheduleEntity, String>, ScheduleRepoCustom{
    @Override
    Optional<ScheduleEntity> findById(@NonNull String id);
}
