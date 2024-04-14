package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private int loginCount;
    private int postCount;
    private int commentCount;
    private int likeCount;
    private int shareCount;
}
