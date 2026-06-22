package com.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cms.entity.Complaint;
import com.cms.entity.User;
import com.cms.service.IComplaintService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/customer")
public class CustomerController 
{

	private final IComplaintService complaintService;

	public CustomerController(IComplaintService complaintService) {
		this.complaintService = complaintService;
	}


	@GetMapping("/dashboard")
	public String customerDashboard(Model model, HttpSession session) 
	{
		User user= (User) session.getAttribute("loggedInUser");

		if(user == null)
		{
			return "redirect:/login";
		}

		model.addAttribute("user", user);
		model.addAttribute("complaint", new Complaint());

		model.addAttribute("complaints", complaintService.
				getComplaintsByCustomer(user.getUserId()));

		return "customer-dashboard";
	}

	@PostMapping("/raise")
	public String raiseComplaint(
			@Valid @ModelAttribute Complaint complaint, BindingResult result,
			Model model,  HttpSession session)
	{
		User user= (User) session.getAttribute("loggedInUser");

		if(user == null)
		{
			return "redirect:/login";
		}


		if(result.hasErrors())
		{
			model.addAttribute("user", user);
			model.addAttribute("complaint", complaint);
			return "customer-dashboard";
		}

		complaintService.raiseComplaint(complaint, user.getUserId());

		return "redirect:/customer/dashboard";
	}


}
