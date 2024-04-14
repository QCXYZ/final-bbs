package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ContentAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String title;
    private Integer views;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> trendingTags;
}
