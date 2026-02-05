package com.example.my_automation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Setter
@Getter
@MappedSuperclass
@Cacheable
@ToString(callSuper = true)
public class IEntity implements Serializable {

	private static final long serialVersionUID = 7591965655232913940L;
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false, unique = true)
	private String id;
}