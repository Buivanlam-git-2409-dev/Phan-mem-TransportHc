package com.transporthc.dto.rolepermission;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionResponse {
    private Integer roleId;
    private String roleName;
    private Integer permissionId;
    private String permissionName;

    private Boolean canWrite;
    private Boolean canView;
    private Boolean canApprove;
    private Boolean canDelete;
    
    private Date createdAt;
    private Date updatedAt;
}
