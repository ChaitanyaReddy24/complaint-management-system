package com.cms.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cms.custom_exceptions.ComplaintNotFoundException;
import com.cms.custom_exceptions.FeedbackAlreadyExistsException;
import com.cms.custom_exceptions.FeedbackNotFoundException;
import com.cms.custom_exceptions.InvalidInputException;
import com.cms.entity.Complaint;
import com.cms.entity.Feedback;
import com.cms.enums.ComplaintStatus;
import com.cms.repository.IComplaintRepository;
import com.cms.repository.IFeedbackRepository;
import com.cms.service.IFeedbackService;

@Service
public class FeedbackServiceImpl implements IFeedbackService 
{
	private final IFeedbackRepository feedbackRepo;
	private final IComplaintRepository complaintRepo;

	public FeedbackServiceImpl(IFeedbackRepository feedbackRepo, IComplaintRepository complaintRepo) {
		this.feedbackRepo = feedbackRepo;
		this.complaintRepo = complaintRepo;
	}

	@Override
	public Feedback submitFeedback(Long complaintId, Integer rating, String comments) 
	{

		Complaint complaint = complaintRepo.findById(complaintId).orElseThrow(
				() -> new ComplaintNotFoundException("Complaint not found with id : "+complaintId));

		if(complaint.getStatus() != ComplaintStatus.CLOSED
				&& complaint.getStatus() != ComplaintStatus.RESOLVED)
		{
			throw new InvalidInputException(
					"Feedback can only be submitted for RESOLVED or CLOSED complaints");

		}

		if(feedbackRepo.findByComplaintComplaintId(complaintId).isPresent())
		{
			throw new FeedbackAlreadyExistsException("Feedback already submitted for this complaint.");
		}

		if(rating ==null)
		{
			throw new InvalidInputException("Rating is required");
		}
		if(rating <1 || rating >5)
		{
			throw new InvalidInputException(
					"Rating must be in between 1-5..");
		}
		
		if(comments == null || comments.isBlank())
		{
			 throw new InvalidInputException(
			            "Comments cannot be empty");
		}

		Feedback feedback = new Feedback();
		
		feedback.setComplaint(complaint);
		feedback.setRating(rating);
		feedback.setComments(comments);


		return feedbackRepo.save(feedback);
	}


	@Override
	public List<Feedback> getAllFeedbacks() 
	{
		return feedbackRepo.findAll();
	}

	@Override
	public Feedback getFeedbackById(Long feedbackId) 
	{
		return feedbackRepo.findById(feedbackId).orElseThrow(
				() -> new FeedbackNotFoundException("Feedback not found with id : " + feedbackId));
	}

	@Override
	public Feedback getFeedbackByComplaint(Long complaintId) 
	{
	 Complaint complaint = complaintRepo.findById(complaintId).orElseThrow(
				() -> new ComplaintNotFoundException("Complaint not found with id : "+ complaintId));


		return feedbackRepo.findByComplaintComplaintId(complaintId).orElseThrow(
				() -> new FeedbackNotFoundException("Feedback not found with id : "+ complaintId));
	}
	
	@Override
	public boolean feedbackExists(Long complaintId)
	{
	    return feedbackRepo
	            .findByComplaintComplaintId(complaintId)
	            .isPresent();
	}

}
