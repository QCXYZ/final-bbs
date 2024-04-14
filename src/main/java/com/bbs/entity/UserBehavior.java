package com.bbs.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Integer visitCount;
    private String activeDegree;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> preferenceTags;
}
