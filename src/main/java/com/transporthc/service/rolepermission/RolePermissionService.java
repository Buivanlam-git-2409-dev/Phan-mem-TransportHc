package com.transporthc.service.rolepermission;

import com.transporthc.dto.rolepermission.UpdateRolePermissionRequest;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;

public interface RolePermissionService {
    boolean hasPermission(PermissionTypeEnum type, PermissionKeyEnum key);
    long updateRolePermission(UpdateRolePermissionRequest request);
}