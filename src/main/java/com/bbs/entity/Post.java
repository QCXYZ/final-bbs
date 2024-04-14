package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String content;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;
    private String category;
}
