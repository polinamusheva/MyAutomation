package com.example.my_automation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public class BaseDomainObject implements Serializable {
    
    private static final long serialVersionUID = 1L;
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    protected Long id;
}
