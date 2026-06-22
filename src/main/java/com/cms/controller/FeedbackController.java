package com.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.entity.Complaint;
import com.cms.entity.Feedback;
import com.cms.service.IComplaintService;
import com.cms.service.IFeedbackService;

@Controller
public class FeedbackController
{
    private final IComplaintService complaintService;
	private final IFeedbackService feedbackService;

    public FeedbackController(
            IComplaintService complaintService,
            IFeedbackService feedbackService)
    {
        this.complaintService = complaintService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedback/add/{complaintId}")
    public String showFeedbackForm(
            @PathVariable("complaintId") Long complaintId,
            Model model)
    {
        Complaint complaint =
                complaintService.findComplaintById(
                        complaintId);

        model.addAttribute(
                "complaint",
                complaint);

        return "feedback-form";
    }
    
    @PostMapping("/feedback/save")
    public String saveFeedback(
            @RequestParam("complaintId") Long complaintId,
            @RequestParam("rating") Integer rating,
            @RequestParam("comments") String comments,
            RedirectAttributes redirectAttributes)
    {
        feedbackService.submitFeedback(
                complaintId,
                rating,
                comments);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Feedback submitted successfully");

        return "redirect:/complaints/view?id="
                + complaintId;
    }
    
    @GetMapping("/admin/feedbacks/view/{id}")
    public String viewFeedback(
            @PathVariable Long id,
            Model model)
    {
        Feedback feedback =
                feedbackService.getFeedbackById(id);

        model.addAttribute(
                "feedback",
                feedback);

        return "feedback-details";
    }
}