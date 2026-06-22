package com.cms.serviceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cms.custom_exceptions.ComplaintNotFoundException;
import com.cms.custom_exceptions.InvalidInputException;
import com.cms.custom_exceptions.UserNotFoundException;
import com.cms.entity.Complaint;
import com.cms.entity.User;
import com.cms.enums.ComplaintStatus;
import com.cms.enums.Role;
import com.cms.repository.IComplaintRepository;
import com.cms.repository.IUserRepository;
import com.cms.service.IComplaintService;

@Service
public class ComplaintServiceImpl implements IComplaintService 
{

	private final IComplaintRepository complaintRepo;
	private final IUserRepository userRepo;

	public ComplaintServiceImpl(IComplaintRepository complaintRepo, IUserRepository userRepo) 
	{
		this.complaintRepo = complaintRepo;
		this.userRepo = userRepo;
	}

	@Override
	public Complaint raiseComplaint(Complaint complaint, Long customerId) 
	{

		// checking whether user is there or not..
		User customer = userRepo.findById(customerId).orElseThrow(
				() -> 
				new UserNotFoundException("Customer not found with id : " + customerId));

		// if user found, checking is he belongs to customer role or other, if other exception will thrown
		if(customer.getRole() != Role.CUSTOMER)
		{
			throw new InvalidInputException(
					"Only customers can raise complaints");
		}

		//If all validations passes, then we are setting customer object and complaintStatus to the complaint object 
		complaint.setCustomer(customer);
		complaint.setStatus(ComplaintStatus.OPEN);

		//Registering the complaint.. If all tests passed
		return complaintRepo.save(complaint);
	}

	@Override
	public Complaint findComplaintById(Long complaintId) 
	{
		return complaintRepo.findById(complaintId).orElseThrow(
				() -> new ComplaintNotFoundException("Complaint is not found with id : " + complaintId));
	}

	@Override
	public List<Complaint> getAllComplaints()
	{
		return complaintRepo.findAll();
	}

	@Override
	public List<Complaint> getComplaintsByStatus(ComplaintStatus status) 
	{
		List<Complaint> complaints = complaintRepo.findByStatus(status);

		//		if(complaints.isEmpty())
		//		{
		//			throw new ComplaintNotFoundException("No complaints found with status : " + status);
		//		}

		return complaints;
	}

	@Override
	public List<Complaint> getComplaintsByCustomer(Long customerId) 
	{
		User customer =	userRepo.findById(customerId).orElseThrow(
				() -> new UserNotFoundException("Customer not found with id : " + customerId));

		if(customer.getRole() != Role.CUSTOMER)
		{
			throw new InvalidInputException(
					"Only customers can raise complaints");
		}

		List<Complaint> complaints = complaintRepo.findByCustomerUserId(customerId);

		//		if(complaints.isEmpty())
		//		{
		//			throw new ComplaintNotFoundException("No complaints found with customer ID : " + customerId);
		//		}
		return complaints;
	}

	@Override
	public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status) 
	{
		//		Complaint complaint = complaintRepo.findById(complaintId).orElseThrow(
		//				() -> new ComplaintNotFoundException("Complaint is not found with id : " + complaintId));

		Complaint complaint = findComplaintById(complaintId);

		if(status == null)
		{
			throw new InvalidInputException("Status cannot be null");
		}
		complaint.setStatus(status);

		return complaintRepo.save(complaint);
	}

	@Override
	public Page<Complaint> getAllComplaints(Integer pageNo) 
	{
		Pageable pageable =
	            PageRequest.of(pageNo, 5);

	    return complaintRepo.findAll(pageable);
	}
	
	@Override
	public Complaint updateComplaint(Long complaintId,
									 String title,
									 String description)
	{
		Complaint complaint = findComplaintById(complaintId);

		if(complaint.getStatus() != ComplaintStatus.OPEN)
		{
			throw new InvalidInputException(
					"Only OPEN complaints can be updated");
		}

		complaint.setTitle(title);
		complaint.setDescription(description);

		return complaintRepo.save(complaint);
	}
	
	@Override
	public void deleteComplaint(Long complaintId)
	{
	    Complaint complaint = findComplaintById(complaintId);

	    if(complaint.getStatus() != ComplaintStatus.OPEN)
	    {
	        throw new InvalidInputException(
	                "Only OPEN complaints can be deleted");
	    }

	    complaintRepo.delete(complaint);
	}

}
