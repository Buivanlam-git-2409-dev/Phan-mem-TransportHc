package com.transporthc.repository.rolepermission;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.transporthc.dto.rolepermission.RolePermissionResponse;
import com.transporthc.entity.permission.QPermissionEntity;
import com.transporthc.entity.role.QRoleEntity;
import com.transporthc.entity.rolepermission.QRolePermissionEntity;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class RolePermissionRepoImpl extends BaseRepo implements RolePermissionRepoCustom {
    public RolePermissionRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<RolePermissionResponse> fetchRolePermissions() {
        QRoleEntity role = QRoleEntity.role;
        QRolePermissionEntity rolePermission = QRolePermissionEntity.rolePermission;
        QPermissionEntity permission = QPermissionEntity.permission;

        return query
                .select(Projections.constructor(RolePermissionResponse.class,
                        role.id.as("roleId"),
                        role.name.as("roleName"),
                        permission.id.as("permissionId"),
                        permission.name.as("permissionName"),
                        rolePermission.canWrite,
                        rolePermission.canView,
                        rolePermission.canApprove,
                        rolePermission.canDelete,
                        rolePermission.createdAt.as("rolePermissionCreatedAt"),
                        rolePermission.updatedAt.as("rolePermissionUpdatedAt")
                ))
                .from(role)
                .innerJoin(rolePermission).on(role.id.eq(rolePermission.roleId))
                .innerJoin(permission).on(rolePermission.permissionId.eq(permission.id))
                .fetch();
    }

    @Override
    public boolean hasPermission(Integer roleId, PermissionTypeEnum type, PermissionKeyEnum key) {
        QRolePermissionEntity qRolePermission = QRolePermissionEntity.rolePermission;
        QPermissionEntity qPermission = QPermissionEntity.permission;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qRolePermission.roleId.eq(roleId))
                .and(qPermission.name.eq(type.getName()));

        if (key != null){
            switch(key){
                case VIEW -> builder.and(qRolePermission.canView.eq(true));
                case WRITE -> builder.and(qRolePermission.canWrite.eq(true));
                case APPROVE -> builder.and(qRolePermission.canApprove.eq(true));
                case DELETE -> builder.and(qRolePermission.canDelete.eq(true));
            }
        }

        Long count = query
                .select(qRolePermission.id.count())
                .from(qRolePermission)
                .innerJoin(qPermission).on(qRolePermission.permissionId.eq(qPermission.id))
                .where(builder)
                .fetchOne();

        return count != null && count > 0;
    }

    @Override
    @Transactional
    public long changePermissionByRoleId(Integer roleId, Integer permissionId, List<Path<?>> paths, List<?> values) {
        QRolePermissionEntity qRolePermission = QRolePermissionEntity.rolePermission;

        BooleanBuilder builder = new BooleanBuilder()
                .and(qRolePermission.roleId.eq(roleId))
                .and(qRolePermission.permissionId.eq(permissionId));

        return query.update(qRolePermission)
                .where(builder)
                .set(paths, values)
                .set(qRolePermission.updatedAt, new Date())
                .execute();
    }
}
