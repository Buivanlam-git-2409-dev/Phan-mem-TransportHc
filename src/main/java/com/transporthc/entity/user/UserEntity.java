package com.transporthc.entity.user;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    private String id;

    @Column(name = "full_name")
    private String nullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "note")
    private String note;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "status")
    private Integer status;
}
