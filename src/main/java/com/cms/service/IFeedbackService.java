package com.cms.service;

import java.util.List;

import com.cms.entity.Feedback;

public interface IFeedbackService 
{

	Feedback submitFeedback(Long complaintId, Integer rating, String comments);

	List<Feedback> getAllFeedbacks();

	Feedback getFeedbackById(Long feedbackId);

	Feedback getFeedbackByComplaint(Long complaintId);
	
	boolean feedbackExists(Long complaintId);
}
