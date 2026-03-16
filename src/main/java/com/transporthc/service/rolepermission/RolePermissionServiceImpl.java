package com.transporthc.service.rolepermission;

import com.transporthc.dto.rolepermission.UpdateRolePermissionRequest;
import com.transporthc.entity.rolepermission.QRolePermission;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.repository.rolepermission.RolePermissionRepo;
import com.transporthc.service.BaseService;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends BaseService implements RolePermissionService {

    private final RolePermissionRepo rolePermissionRepo;
    private final PermissionTypeEnum type = PermissionTypeEnum.PERMISSIONS;

    @Override
    public boolean hasPermission(PermissionTypeEnum type, PermissionKeyEnum key){
        return checkPermission(type, key);
    }

    @Override
    @Transactional
    public long updateRolePermission(UpdateRolePermissionRequest dto) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        List<Path<?>> paths = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        QRolePermission qRolePermission = QRolePermission.rolePermission;

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                PathBuilder<Object> path = new PathBuilder<>(Object.class, qRolePermission.getMetadata().getName() + "." + field.getName());
                paths.add(path);
                Object value = field.get(dto);
                if (value != null) {
                    values.add(value);
                }
            } catch (Exception e) {
            }
        }

        return rolePermissionRepo.changePermissionByRoleId(dto.getRoleId(), dto.getPermissionId(), paths, values);
    }
}