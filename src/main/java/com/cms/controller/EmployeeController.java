package com.cms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cms.entity.Assignment;
import com.cms.entity.User;
import com.cms.service.IAssignmentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/employee")
public class EmployeeController
{

	private final IAssignmentService assignmentService;

	public EmployeeController(IAssignmentService assignmentService)
	{
		this.assignmentService = assignmentService;
	}

	@GetMapping("/dashboard")
	public String employeeDashboard(
			Model model,
			HttpSession session)
	{
		User employee =
				(User) session.getAttribute("loggedInUser");

		if(employee == null)
		{
			return "redirect:/login";
		}

		List<Assignment> assignments =
				assignmentService.getAssignmentByEmployeeId(
						employee.getUserId());

		model.addAttribute("user", employee);
		model.addAttribute("assignments", assignments);

		return "employee-dashboard";
	}

	@GetMapping("/assignment/view/{id}")
	public String viewAssignment(
			@PathVariable Long id,
			Model model)
	{
		Assignment assignment =
				assignmentService.getAssignmentById(id);

		model.addAttribute("assignment", assignment);

		return "assignment-details";
	}

	@GetMapping("/accept/{id}")
	public String acceptComplaint(
			@PathVariable Long id)
	{
		assignmentService.acceptAssignment(id);

		return "redirect:/employee/dashboard";
	}

	@GetMapping("/complete/{id}")
	public String completeAssignment(
	        @PathVariable Long id)
	{
	    assignmentService.completeAssignment(id);

	    return "redirect:/employee/dashboard";
	}
}