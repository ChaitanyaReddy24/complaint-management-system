package com.cms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.entity.Complaint;
import com.cms.enums.ComplaintStatus;

public interface IComplaintRepository extends JpaRepository<Complaint, Long> 
{
	List<Complaint> findByStatus(ComplaintStatus status);
	
	List<Complaint> findByCustomerUserId(Long userId);
	
	Page<Complaint> findAll(Pageable pageable);
	
	long countByStatus(ComplaintStatus status);
	
	

}
