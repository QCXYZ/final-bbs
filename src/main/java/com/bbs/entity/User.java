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
    private String avatar = "https://sky-microcosm.oss-cn-beijing.aliyuncs.com/03447aff-0d70-49ec-af19-cbfc36401fa3.png";
    private String nickname = "新用户";
    private String signature = "这个人很懒，什么都没留下";
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role = new Role(2L, "USER");
    // 以下两个字段是重置密码用的
    private String resetToken;
    private Date resetTokenExpiryDate;
}
