package com.cms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.entity.Complaint;
import com.cms.entity.User;
import com.cms.service.IComplaintService;
import com.cms.service.IFeedbackService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/complaints")
public class ComplaintController
{

	private final IComplaintService complaintService;
	private final IFeedbackService feedbackService;

	public ComplaintController(IComplaintService complaintService, IFeedbackService feedbackService)
	{
		this.complaintService = complaintService;
		this.feedbackService = feedbackService;
	}

	@GetMapping("/my-complaints")
	public String viewMyComplaints(HttpSession session,
								   Model model)
	{
		User user = (User) session.getAttribute("loggedInUser");

		List<Complaint> complaints =
				complaintService.getComplaintsByCustomer(
						user.getUserId());

		model.addAttribute("complaints", complaints);

		return "my-complaints";
	}
	
	@GetMapping("/view")
	public String viewComplaint(@RequestParam("id") Long id,
	                            Model model)
	{
	    Complaint complaint =
	            complaintService.findComplaintById(id);

	    model.addAttribute("complaint", complaint);
	    
	    boolean feedbackExists =
	            feedbackService.feedbackExists(
	                    complaint.getComplaintId());

	    model.addAttribute(
	            "feedbackExists",
	            feedbackExists);

	    return "my-complaint-details";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id,
							   Model model)
	{
		Complaint complaint =
				complaintService.findComplaintById(id);

		model.addAttribute("complaint", complaint);

		return "edit-complaint";
	}
	
	@PostMapping("/update")
	public String updateComplaint(
	        @RequestParam Long complaintId,
	        @RequestParam String title,
	        @RequestParam String description,
	        RedirectAttributes redirectAttributes)
	{
	    complaintService.updateComplaint(
	            complaintId,
	            title,
	            description);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "Complaint updated successfully");

	    return "redirect:/complaints/view?id=" + complaintId;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteComplaint(@PathVariable Long id,
	                              RedirectAttributes redirectAttributes)
	{
	    complaintService.deleteComplaint(id);

	    redirectAttributes.addFlashAttribute(
	            "successMessage",
	            "Complaint deleted successfully");

	    return "redirect:/complaints/my-complaints";
	}
}