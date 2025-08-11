package com.transporthc.repository.role;
import java.util.List;

import com.transporthc.entity.role.RoleEntity;


public interface RoleRepoCustom {
    RoleEntity findRoleById(Integer id);
    List<RoleEntity> getAll();
    void deleteRoleById(Integer id);
}
