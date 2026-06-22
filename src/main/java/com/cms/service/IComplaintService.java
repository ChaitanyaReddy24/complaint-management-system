package com.cms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cms.entity.Complaint;
import com.cms.enums.ComplaintStatus;

public interface IComplaintService 
{

	Complaint raiseComplaint(Complaint complaint, Long customerId);
	
	Complaint findComplaintById(Long complaintId);
	
	List<Complaint> getAllComplaints();
	
	List<Complaint> getComplaintsByStatus(ComplaintStatus status);
	
	List<Complaint> getComplaintsByCustomer(Long CustomerId);
	
	Complaint updateComplaintStatus( Long complaintId, ComplaintStatus status);
	
	Complaint updateComplaint(Long complaintId, String title, String description);
	
	Page<Complaint> getAllComplaints(Integer pageNo);
	
	void deleteComplaint(Long complaintId);
}
