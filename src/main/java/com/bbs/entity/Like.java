package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;
}
