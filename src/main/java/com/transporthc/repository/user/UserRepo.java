package com.transporthc.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.user.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>, UserRepoCustom {
}