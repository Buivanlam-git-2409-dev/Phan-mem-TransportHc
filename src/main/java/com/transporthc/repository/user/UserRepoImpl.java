package com.transporthc.repository.user;

import static com.transporthc.entity.role.QRoleEntity.roleEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.transporthc.dto.user.UserDto;
import com.transporthc.entity.user.User;
import com.transporthc.enums.Pagination;
import com.transporthc.enums.role.UserRoleEnum;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class UserRepoImpl extends BaseRepo implements UserRepoCustom {
    public UserRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<UserDto> expression() {
        return Projections.constructor(UserDto.class,
                userEntity.id.as("id"),
                userEntity.fullName.as("fullName"),
                userEntity.phone.as("phone"),
                userEntity.dateOfBirth.as("dateOfBirth"),
                userEntity.imagePath.as("imagePath"), //
                userEntity.note.as("note"),
                userEntity.username.as("username"),
                userEntity.password.as("password"),
                userEntity.roleId.as("roleId"),
                JPAExpressions.select(roleEntity.name.as("roleName"))
                        .from(roleEntity)
                        .where(roleEntity.id.eq(userEntity.roleId)),
                userEntity.status.as("status"),
                userEntity.createdAt.as("createdAt")
        );
    }

    @Override
    public List<User> getAll(int page){
        QUser qUser = QUser.userEntity;
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.selectFrom(qUser)
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public User getUserById(String id){
        QUserEnity qUser = QUserEntity.userEntity;
        BooleanBuilder builder = new BooleanBuilder()
                .and(qUser.id.eq(id));
        return query.selectFrom(qUser)
                .where(builder)
                .fetchOne();
    }

    @Override
    @Transactional
    public Boolean updateUser(User exitingUser,String id, UserDto updateUserDTO) {
        QUserEntity qUser = QUserEntity.userEntity;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qUser.id.eq(id));

        JPAUpdateClause updateClause = query.update(qUser).where(whereClause);

        boolean isUpdated = false;
        boolean isChanged = false;

        if (updateUserDTO.getFullName() != null) {
            if(!updateUserDTO.getFullName().equals(exitingUser.getFullName())){
                isChanged = true;
            }
            updateClause.set(qUser.fullName, updateUserDTO.getFullName());
            isUpdated = true;
        }
        if (updateUserDTO.getPhone() != null) {
            if(!updateUserDTO.getPhone().equals(exitingUser.getPhone())){
                isChanged = true;
            }
            updateClause.set(qUser.phone, updateUserDTO.getPhone());
            isUpdated = true;
        }
        if (updateUserDTO.getDateOfBirth() != null) {
            if(!updateUserDTO.getDateOfBirth().equals(exitingUser.getDateOfBirth())){
                isChanged = true;
            }
            updateClause.set(qUser.dateOfBirth, updateUserDTO.getDateOfBirth());
            isUpdated = true;
        }
        if (updateUserDTO.getImagePath() != null) {
            if(!updateUserDTO.getImagePath().equals(exitingUser.getImagePath())){
                isChanged = true;
            }
            updateClause.set(qUser.imagePath, updateUserDTO.getImagePath());
            isUpdated = true;
        }
        if (updateUserDTO.getNote() != null) {
            if(!updateUserDTO.getNote().equals(exitingUser.getNote())){
                isChanged = true;
            }
            updateClause.set(qUser.note, updateUserDTO.getNote());
            isUpdated = true;
        }
        if (updateUserDTO.getUsername() != null) {
            if(!updateUserDTO.getUsername().equals(exitingUser.getUsername())){
                isChanged = true;
            }
            updateClause.set(qUser.username, updateUserDTO.getUsername());
            isUpdated = true;
        }
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            if(!updateUserDTO.getPassword().equals(exitingUser.getPassword())){
                isChanged = true;
            }
            updateClause.set(qUser.password, updateUserDTO.getPassword());
            isUpdated = true;
        }
        if (updateUserDTO.getRoleId() != null) {
            if(!updateUserDTO.getRoleId().equals(exitingUser.getRoleId())){
                isChanged = true;
            }
            updateClause.set(qUser.roleId, updateUserDTO.getRoleId());
            isUpdated = true;
        }
        if (updateUserDTO.getStatus() != null) {
            if(!updateUserDTO.getStatus().equals(exitingUser.getStatus())){
                isChanged = true;
            }
            updateClause.set(qUser.status, updateUserDTO.getStatus());
            isUpdated = true;
        }

        if (isUpdated) {
            updateClause.set(qUser.updatedAt, new Date());
        } else {
            throw new InvalidFieldException("No data fields are updated!!!");
        }

        if(!isChanged) {
            throw new EditNotAllowedException("Data is not changed!!!");
        }

        return updateClause.execute() > 0;
    }

    @Modifying
    @Transactional
    public long deleteUser(String id){
        QUserEntity qUser = QUserEntity.user;
        return query.update(qUser)
                .where(qUser.id.eq(id))
                .set(qUser.status,0)
                .execute();
    }

    @Override
    public User  findByUsername(String username) {
        QUserEntity qUser = QUserEntity.user;

        return query.selectFrom(qUser)
                .where(qUser.username.eq(username))
                .fetchOne();

    }

    @Override
    public List<UserDto> getDriver(int page) {
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(userEntity)
                .leftJoin(roleEntity).on(roleEntity.id.eq(userEntity.roleId))
                .where(roleEntity.id.eq(UserRoleEnum.DRIVER.getId()))
                .select(expression())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public List<UserDto> getAdmin(int page) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(roleEntity.id.eq(UserRoleEnum.ADMIN.getId()))
                .or(roleEntity.id.eq(UserRoleEnum.ACCOUNTANT.getId()))
                .or(roleEntity.id.eq(UserRoleEnum.MANAGER.getId()));
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(userEntity)
                .leftJoin(roleEntity).on(roleEntity.id.eq(userEntity.roleId))
                .where(builder)
                .select(expression())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }
}