package com.transporthc.repository.rolepermission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transporthc.entity.rolepermission.RolePermissionEntity;

public interface RolePermissionRepo extends JpaRepository<RolePermissionEntity, Integer>, RolePermissionRepoCustom{
     Optional<RolePermissionEntity> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}
