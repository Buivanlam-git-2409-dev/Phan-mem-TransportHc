package com.transporthc.repository.role;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.role.RoleEntity;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, Integer>, RoleRepoCustom  {
    
}
