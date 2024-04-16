package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String avatar;
    @Column
    private String nickname;
    @Column
    private String signature;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role = new Role(2L, "USER");
    // 以下两个字段是重置密码用的
    @Column
    private String resetToken;
    @Column
    private Date resetTokenExpiryDate;
}
