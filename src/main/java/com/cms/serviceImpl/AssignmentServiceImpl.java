package com.cms.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cms.custom_exceptions.AssignmentNotFoundException;
import com.cms.custom_exceptions.ComplaintNotFoundException;
import com.cms.custom_exceptions.InvalidInputException;
import com.cms.custom_exceptions.UserNotFoundException;
import com.cms.entity.Assignment;
import com.cms.entity.Complaint;
import com.cms.entity.User;
import com.cms.enums.AssignmentStatus;
import com.cms.enums.ComplaintStatus;
import com.cms.enums.Role;
import com.cms.repository.IAssignmentRepository;
import com.cms.repository.IComplaintRepository;
import com.cms.repository.IUserRepository;
import com.cms.service.IAssignmentService;

import jakarta.transaction.Transactional;

@Service
public class AssignmentServiceImpl implements IAssignmentService
{

	private final IAssignmentRepository assignmentRepo;
	private final IUserRepository userRepo;
	private final IComplaintRepository complaintRepo;

	public AssignmentServiceImpl(IAssignmentRepository assignmentRepo, IUserRepository userRepo,
			IComplaintRepository complaintRepo) {
		this.assignmentRepo = assignmentRepo;
		this.userRepo = userRepo;
		this.complaintRepo = complaintRepo;
	}

	@Transactional
	@Override
	public Assignment assignComplaint(Long complaintId, Long employeeId, String remarks) 
	{
		Complaint complaint = complaintRepo.findById(complaintId).orElseThrow(
				() -> new ComplaintNotFoundException("Complaint not found with id : "+complaintId));
		
		User employee = userRepo.findById(employeeId).orElseThrow( 
				() -> new UserNotFoundException("Employee not found with id : "+employeeId));
		
		if(employee.getRole() != Role.EMPLOYEE)
		{
			throw new InvalidInputException(
					"Complaints can only be assigned to employees..");
		}
		
		if(complaint.getStatus() != ComplaintStatus.OPEN)
		{
			throw new InvalidInputException(
					"This complaints is not at OPEN stage..");
		}
		
		Assignment assignment = new Assignment();
		assignment.setRemarks(remarks);
		assignment.setComplaint(complaint);
		assignment.setEmployee(employee);
		assignment.setStatus(AssignmentStatus.ASSIGNED);
		complaint.setStatus(ComplaintStatus.ASSIGNED);
		
		complaintRepo.save(complaint);

		return assignmentRepo.save(assignment);
	}

	@Override
	public Assignment getAssignmentById(Long assignmentId) 
	{	
		return assignmentRepo.findById(assignmentId).orElseThrow(
				() -> new AssignmentNotFoundException("Assignment not found with id : "+ assignmentId));
	}

	@Override
	public List<Assignment> getAllAssignments() 
	{
		return assignmentRepo.findAll();
	}

	@Override
	public List<Assignment> getAssignmentByEmployeeId(Long employeeId) 
	{
		User employee = userRepo.findById(employeeId).orElseThrow(
				() -> new UserNotFoundException("Employee not found with id : "+employeeId) );
		
		if(employee.getRole() != Role.EMPLOYEE)
		{
			 throw new InvalidInputException("User is not an employee");
		}
//		List<Assignment> assignments = assignmentRepo.findByEmployeeUserId(employeeId);
		return assignmentRepo.findByEmployeeUserId(employeeId);
	}


	
	@Override
	public Assignment acceptAssignment(Long assignmentId)
	{
	    Assignment assignment =
	            getAssignmentById(assignmentId);

	    assignment.setStatus(
	            AssignmentStatus.ACCEPTED);

	    Complaint complaint =
	            assignment.getComplaint();

	    complaint.setStatus(
	            ComplaintStatus.IN_PROGRESS);

	    complaintRepo.save(complaint);

	    return assignmentRepo.save(assignment);
	}
	
	@Override
	public Assignment completeAssignment(Long assignmentId)
	{
	    Assignment assignment =
	            getAssignmentById(assignmentId);

	    if(assignment.getStatus() != AssignmentStatus.ACCEPTED)
	    {
	        throw new InvalidInputException(
	                "Only accepted assignments can be completed");
	    }

	    assignment.setStatus(
	            AssignmentStatus.COMPLETED);

	    Complaint complaint =
	            assignment.getComplaint();

	    complaint.setStatus(
	            ComplaintStatus.RESOLVED);

	    complaintRepo.save(complaint);

	    return assignmentRepo.save(assignment);
	}

}
