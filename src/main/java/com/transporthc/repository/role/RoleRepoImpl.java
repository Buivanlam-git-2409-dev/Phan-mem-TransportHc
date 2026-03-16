package com.transporthc.repository.role;

import com.transporthc.entity.role.Role;
import com.transporthc.repository.BaseRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.transporthc.entity.role.QRoleEntity.roleEntity;

@Repository
public class RoleRepoImpl extends BaseRepo implements RoleRepoCustom {
    public RoleRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Role findRoleById(Integer id) {
        return query.selectFrom(roleEntity)
                .where(roleEntity.id.eq(id))
                .fetchOne();
    }

    public List<Role> getAll(){
        return query.selectFrom(roleEntity).fetch();
    }

    @Modifying
    @Transactional
    public void deleteRoleById(Integer id){
        query.delete(roleEntity).where(roleEntity.id.eq(id));
    }
}