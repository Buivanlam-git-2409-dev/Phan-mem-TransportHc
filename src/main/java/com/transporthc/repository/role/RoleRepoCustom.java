package com.transporthc.repository.role;
import java.util.List;

import com.transporthc.entity.role.Role;


public interface RoleRepoCustom {
    Role findRoleById(Integer id);
    List<Role> getAll();
    void deleteRoleById(Integer id);
}
