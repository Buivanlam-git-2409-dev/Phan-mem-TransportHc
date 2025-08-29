package com.transporthc.repository.rolepermission;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transporthc.entity.rolepermission.RolePermission;

public interface RolePermissionRepo extends JpaRepository<RolePermission, Integer>, RolePermissionRepoCustom{
     Optional<RolePermission> findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}
