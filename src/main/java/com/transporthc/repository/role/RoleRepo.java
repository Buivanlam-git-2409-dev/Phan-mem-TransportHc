package com.transporthc.repository.role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.role.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, RoleRepoCustom  {
    
}
