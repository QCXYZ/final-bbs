package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String qqId;
    private String token; // 用于认证和授权
    private String nickname;
    private String avatar;
    private String personalSign;
}
