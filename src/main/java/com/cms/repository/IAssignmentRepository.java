package com.cms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.entity.Assignment;

public interface IAssignmentRepository extends JpaRepository<Assignment, Long> 
{
	List<Assignment> findByEmployeeUserId(Long employeeId);

}
