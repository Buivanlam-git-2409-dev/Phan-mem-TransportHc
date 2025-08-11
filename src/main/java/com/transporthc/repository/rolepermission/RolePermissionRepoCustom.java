package com.transporthc.repository.rolepermission;

import java.util.List;

import com.querydsl.core.types.Path;
import com.transporthc.dto.rolepermission.RolePermissionResponse;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;

public interface RolePermissionRepoCustom {
    List<RolePermissionResponse> fetchRolePermissions();
    boolean hasPermission(Integer roleId, PermissionTypeEnum type, PermissionKeyEnum key);
    long changePermissionByRoleId(Integer roleId, Integer permissionId, List<Path<?>> paths, List<?> values);
}
