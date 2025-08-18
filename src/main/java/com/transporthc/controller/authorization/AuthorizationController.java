package com.transporthc.controller.authorization;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.rolepermission.UpdateRolePermissionRequest;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.service.rolepermission.RolePermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role-permission")
@RequiredArgsConstructor
public class AuthorizationController {
    private final RolePermissionService rolePermissionService;

    @PostMapping("/check-permission")
    public ResponseEntity<BaseResponse<Boolean>> checkPermisson(@RequestParam PermissionTypeEnum permissionName, @RequestParam PermissionKeyEnum key){
        return ResponseEntity.ok(BaseResponse.ok(rolePermissionService.hasPermission(permissionName, key)));
    }

    @PostMapping("/authorization")
    public ResponseEntity<BaseResponse<Boolean>> updateRolePermission(@Valid @RequestBody UpdateRolePermissionRequest request) {

        long updatedRolePermission = rolePermissionService.updateRolePermission(request);
        return ResponseEntity.ok(BaseResponse.ok(updatedRolePermission >0));
    }
}