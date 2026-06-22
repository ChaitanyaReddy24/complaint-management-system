package com.cms.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.entity.Complaint;
import com.cms.entity.Feedback;
import com.cms.entity.User;
import com.cms.enums.ComplaintStatus;
import com.cms.enums.Role;
import com.cms.repository.IUserRepository;
import com.cms.service.IAssignmentService;
import com.cms.service.IComplaintService;
import com.cms.service.IFeedbackService;
import com.cms.service.IReportService;
import com.cms.service.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/admin")
public class AdminController 
{

	private final IComplaintService complaintService;
	private final IUserRepository userRepo;
	private final IAssignmentService assignmentService;
	private final IUserService userService;
	private final IFeedbackService feedbackService;
	private final IReportService reportService;

	public AdminController(IComplaintService complaintService, IUserRepository userRepo,
			IAssignmentService assignmentService, IUserService userService, 
			IFeedbackService feedbackService, IReportService reportService) 
	{
		this.complaintService = complaintService;
		this.userRepo = userRepo;
		this.assignmentService = assignmentService;
		this.userService=userService;
		this.feedbackService = feedbackService;
		this.reportService = reportService;
	}



	//	@GetMapping("/dashboard")
	//	public String adminDashboard(@ModelAttribute User user, HttpSession session)  
	//	{
	//		return "admin-dashboard";
	//	}

	//	Both methods works same if we use @ModelAttribute spring automatically create the object. 
	//	And it will pass the object. As below method..

	@GetMapping("/dashboard")
	public String adminDashboard(HttpSession session, Model model) 
	{
		User admin = (User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		List<Complaint> complaints = complaintService.getAllComplaints();

		long openComplaints = complaints.stream().
				filter(c -> c.getStatus() == ComplaintStatus.OPEN).count();
		long resolvedComplaints = complaints.stream().
				filter(c -> c.getStatus() == ComplaintStatus.RESOLVED).count();

		model.addAttribute("user",admin);
		model.addAttribute("complaints", complaints);

		model.addAttribute("totalComplaints",
				complaints.size());

		model.addAttribute("openComplaints",
				openComplaints);

		model.addAttribute("resolvedComplaints",
				resolvedComplaints);

		return "admin-dashboard";
	}

	@GetMapping("/assign")
	public String showAssignPage(@RequestParam("complaintId") Long complaintId, Model model, HttpSession session) 
	{
		User admin = (User) session.getAttribute("loggedInUser");


		if(admin == null)
		{
			return "redirect:/login";
		}

		Complaint complaint = complaintService.findComplaintById(complaintId);

		List<User> employees = userRepo.findByRole(Role.EMPLOYEE);

		model.addAttribute("complaint", complaint);
		model.addAttribute("employees", employees);

		return "assign-complaint";
	}

	@PostMapping("/assign")
	public String assignComplaint(@RequestParam("complaintId") Long complaintId, 
			@RequestParam("employeeId") Long employeeId, @RequestParam("remarks") String remarks) 
	{

		assignmentService.assignComplaint(complaintId, employeeId, remarks);

		return "redirect:/admin/dashboard";
	}

	@GetMapping("/employees")
	public String getEmployees(
			@RequestParam(defaultValue = "0") Integer pageNo,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		Page<User> pageData =
				userService.getEmployees(pageNo);

		model.addAttribute("employees",
				pageData.getContent());

		model.addAttribute("currentPage",
				pageNo);

		model.addAttribute("totalPages",
				pageData.getTotalPages());

		model.addAttribute("user",
				admin);

		return "employee-list";
	}

	@GetMapping("/employees/add")
	public String getAddEmployeePage(
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		model.addAttribute("employee",
				new User());

		model.addAttribute("user",
				admin);

		return "employee-add";
	}

	@PostMapping("/employees/add")
	public String addEmployee(
			@Valid @ModelAttribute("employee") User employee,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		if(result.hasErrors())
		{
			model.addAttribute("user", admin);
			return "employee-add";
		}

		try
		{
			employee.setRole(Role.EMPLOYEE);

			userService.registerUser(employee);

			redirectAttributes.addFlashAttribute(
					"success",
					"Employee created successfully.");

			return "redirect:/admin/employees";
		}
		catch(RuntimeException e)
		{
			model.addAttribute("user", admin);
			model.addAttribute("error",
					e.getMessage());

			return "employee-add";
		}
	}

	@GetMapping("/employees/edit/{id}")
	public String getEditEmployeePage(
			@PathVariable Long id,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		User employee = userService.findById(id);

		model.addAttribute("employee", employee);
		model.addAttribute("user", admin);

		return "employee-edit";
	}

	@PostMapping("/employees/update")
	public String updateEmployee(
			@Valid @ModelAttribute("employee") User employee,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		if(result.hasErrors())
		{
			model.addAttribute("user", admin);
			return "employee-edit";
		}

		employee.setRole(Role.EMPLOYEE);

		userService.updateEmployee(employee);

		redirectAttributes.addFlashAttribute(
				"success",
				"Employee updated successfully.");

		return "redirect:/admin/employees";
	}

	@GetMapping("/employees/delete/{id}")
	public String deleteEmployee(
			@PathVariable Long id,
			RedirectAttributes redirectAttributes)
	{
		userService.deleteEmployee(id);

		redirectAttributes.addFlashAttribute(
				"success",
				"Employee deleted successfully.");

		return "redirect:/admin/employees";
	}


	@GetMapping("/complaints")
	public String getComplaints(
			@RequestParam(defaultValue = "0")
			Integer pageNo,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		Page<Complaint> pageData =
				complaintService.getAllComplaints(pageNo);

		model.addAttribute(
				"complaints",
				pageData.getContent());

		model.addAttribute(
				"currentPage",
				pageNo);

		model.addAttribute(
				"totalPages",
				pageData.getTotalPages());

		model.addAttribute(
				"user",
				admin);

		return "complaint-list";
	}
	
	@GetMapping("/complaints/filter")
	public String filterComplaints(
	        @RequestParam(required = false)
	        ComplaintStatus status,
	        Model model,
	        HttpSession session)
	{
	    User admin =
	            (User) session.getAttribute("loggedInUser");

	    if(admin == null)
	    {
	        return "redirect:/login";
	    }

	    List<Complaint> complaints;

	    if(status == null)
	    {
	        complaints =
	                complaintService.getAllComplaints();
	    }
	    else
	    {
	        complaints =
	                complaintService.getComplaintsByStatus(status);
	    }

	    model.addAttribute(
	            "complaints",
	            complaints);

	    model.addAttribute(
	            "user",
	            admin);

	    return "complaint-list";
	}

	@GetMapping("/complaints/view/{id}")
	public String viewComplaint(
			@PathVariable Long id,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		Complaint complaint =
				complaintService.findComplaintById(id);

		model.addAttribute("user", admin);
		model.addAttribute("complaint", complaint);

		return "complaint-details";
	}

	@GetMapping("/complaints/assign/{id}")
	public String getAssignComplaintPage(
			@PathVariable Long id,
			Model model,
			HttpSession session)
	{
		User admin =
				(User) session.getAttribute("loggedInUser");

		if(admin == null)
		{
			return "redirect:/login";
		}

		Complaint complaint =
				complaintService.findComplaintById(id);

		model.addAttribute("complaint", complaint);

		model.addAttribute(
				"employees",
				userService.getAllEmployees());

		return "assign-complaint";
	}

	@PostMapping("/complaints/assign")
	public String assignComplaint(
			@RequestParam Long complaintId,
			@RequestParam Long employeeId,
			@RequestParam String remarks,
			RedirectAttributes redirectAttributes)
	{
		assignmentService.assignComplaint(
				complaintId,
				employeeId,
				remarks);

		redirectAttributes.addFlashAttribute(
				"success",
				"Complaint assigned successfully.");

		return "redirect:/admin/complaints";
	}

	@GetMapping("/feedbacks")
	public String viewAllFeedbacks(Model model)
	{
		List<Feedback> feedbacks =
				feedbackService.getAllFeedbacks();

		model.addAttribute("feedbacks", feedbacks);

		return "feedback-list";
	}
	
	@GetMapping("/reports")
	public String reports(Model model)
	{
	    model.addAttribute(
	            "report",
	            reportService.getDashboardReport());

	    return "dashboard-report";
	}

}
