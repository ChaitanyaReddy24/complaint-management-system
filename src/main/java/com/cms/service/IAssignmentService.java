package com.cms.service;

import java.util.List;

import com.cms.entity.Assignment;

public interface IAssignmentService 
{

	Assignment assignComplaint(Long complaintId, Long employeeId, String remarks);
	
	Assignment getAssignmentById(Long assignmentId);
	
	List<Assignment> getAllAssignments();
	
	List<Assignment> getAssignmentByEmployeeId(Long employeeId);
	
	Assignment acceptAssignment(Long assignmentId);
	
	Assignment completeAssignment(Long assignmentId);
}
