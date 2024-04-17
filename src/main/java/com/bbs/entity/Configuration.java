package com.bbs.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "configuration")
public class Configuration {
    @Id
    private String setting;
    private String value;
}
