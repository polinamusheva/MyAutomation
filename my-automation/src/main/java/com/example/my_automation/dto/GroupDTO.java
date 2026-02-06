package com.example.my_automation.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String groupName;
	private String groupLabel;
	private List<String> memberUsers = new ArrayList<>();
	private List<String> memberGroups = new ArrayList<>();
	private String role;
}
