package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;
    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser;
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
    @Column(nullable = false)
    private String reason;
}
