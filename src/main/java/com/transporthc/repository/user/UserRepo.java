package com.transporthc.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.user.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String>, UserRepoCustom {
}