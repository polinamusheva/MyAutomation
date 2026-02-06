package com.example.my_automation.builder;


import com.example.my_automation.dto.LayoutDTO;

import java.time.LocalDateTime;

public class ProjectBuilder {

	public static LayoutDTO createProjectForTest(String name) {
		LayoutDTO layoutDTO = new LayoutDTO();
		layoutDTO.setName(name);
		layoutDTO.setProgress(0L);
		layoutDTO.setPriority("Regular");
		layoutDTO.setStatus("open");
		return layoutDTO;
	}

	public static LayoutDTO createProjectForTest(String name, String description, String priority, LocalDateTime dueDate, String avatar) {
		LayoutDTO layoutDTO = new LayoutDTO();
		layoutDTO.setName(name);
		layoutDTO.setDescription(description);
		layoutDTO.setProgress(0L);
		layoutDTO.setPriority(priority);
		layoutDTO.setDueDate(dueDate);
		layoutDTO.setUploadedAvatar(avatar);
		layoutDTO.setStatus("open");
		return layoutDTO;
	}
	public static LayoutDTO createProjectForUpdate(String name, String description, String priority, LocalDateTime dueDate, String status, String avatar)  {
		LayoutDTO projectUpdated = new LayoutDTO();
		projectUpdated.setName(name);
		projectUpdated.setDescription(description);
		projectUpdated.setProgress(0L);
		projectUpdated.setPriority(priority);
		projectUpdated.setDueDate(dueDate);
		projectUpdated.setUploadedAvatar(avatar);
		projectUpdated.setStatus(status);
		return projectUpdated;
	}
}
