package com.transporthc.repository.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.permission.Permission;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer>, PermissionRepoCustom{
    
}
