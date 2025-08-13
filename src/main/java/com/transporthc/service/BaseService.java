package com.transporthc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.transporthc.entity.user.UserEntity;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.ForbiddenException;
import com.transporthc.repository.rolepermission.RolePermissionRepo;
import com.transporthc.repository.user.UserRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseService {
    @Autowired
    UserRepo userRepository;
    @Autowired
    RolePermissionRepo rolePermissionRepo;

    protected UserEntity getCurrentUserEntity(){
        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen==null || !authen.isAuthenticated() || authen.getPrincipal().equals("anonymousUser")){
            throw new ForbiddenException("Bạn chưa đăng nhập. Thử lại!");
        }
        String username = authen.getName();
        return userRepository.findByUsername(username);
    }
    protected boolean checkPermission(PermissionTypeEnum type, PermissionKeyEnum key){
        UserEntity user = getCurrentUserEntity();
        if(!rolePermissionRepo.hasPermission(user.getRoleId(), type, key)){
            throw new ForbiddenException("Huh, quyền truy cập của bạn không được phép!");
        }
        return true;
    }
}
