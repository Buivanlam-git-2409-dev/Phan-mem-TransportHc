package com.transporthc.repository.permission;

import java.util.List;

import com.transporthc.entity.permission.Permission;

public interface PermissionRepoCustom {
    public List<Permission> getPermissionsByRoleId(Integer roleId);
}
