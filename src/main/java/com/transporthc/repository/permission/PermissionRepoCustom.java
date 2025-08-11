package com.transporthc.repository.permission;

import java.util.List;

import com.transporthc.entity.permission.PermissionEntity;

public interface PermissionRepoCustom {
    public List<PermissionEntity> getPermissionsByRoleId(Integer roleId);
}
