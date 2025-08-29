package com.transporthc.repository.schedule.schedule;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.schedule.Schedule;

import io.micrometer.common.lang.NonNull;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, String>, ScheduleRepoCustom{
    @Override
    Optional<Schedule> findById(@NonNull String id);
}
