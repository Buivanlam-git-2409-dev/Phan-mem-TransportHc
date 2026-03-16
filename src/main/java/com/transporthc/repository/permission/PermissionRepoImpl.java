package com.transporthc.repository.permission;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.transporthc.entity.permission.Permission;
import com.transporthc.entity.permission.QPermissionEntity;
import com.transporthc.entity.rolepermission.QRolePermissionEntity;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;

@Repository
public class PermissionRepoImpl extends BaseRepo implements PermissionRepoCustom{
    public PermissionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        QPermissionEntity permission = QPermissionEntity.permission;
        QRolePermissionEntity rolePermission = QRolePermissionEntity.rolePermission;

        return query.select(permission)
                .from(permission)
                .innerJoin(rolePermission)
                .on(permission.id.eq(rolePermission.permissionId))
                .where(rolePermission.roleId.eq(roleId))
                .fetch();
    }
}
