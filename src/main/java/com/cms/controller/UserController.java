package com.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.entity.User;
import com.cms.enums.Role;
import com.cms.service.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController 
{

	private final IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String getRegistrationForm(@ModelAttribute User user)
	{
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute User user, 
			BindingResult result,  RedirectAttributes redirectAttributes )
	{
		if(result.hasErrors())
		{
			return "register";
		}

		user.setRole(Role.CUSTOMER);
		userService.registerUser(user);
		redirectAttributes.addFlashAttribute("success", "Registration Successful. Please login");
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String getLoginForm()
	{
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam("username") String input, 
			@RequestParam("password") String password, 
			HttpSession session, Model model)
	{
		try {
			
			User user = userService.login(input, password);

			session.setAttribute("loggedInUser", user);

//			if(user.getRole() == Role.ADMIN)
//			{
//				return "redirect:/admin/dashboard";
//			}
//			if(user.getRole() == Role.EMPLOYEE)
//			{
//				return "redirect:/employee/dashboard";
//			}
//			return "redirect:/customer/dashboard";
			
			switch(user.getRole())
			{
			case ADMIN: return "redirect:/admin/dashboard";
			
			case EMPLOYEE: return "redirect:/employee/dashboard";
			
			default: return "redirect:/customer/dashboard";
			}
		}
		catch(RuntimeException e)
		{
			model.addAttribute("error", e.getMessage());
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logoutUser(HttpSession session)
	{
		session.invalidate();
		return "redirect:/login";
	}
	
	@GetMapping("/forgot-password")
	public String showForgotPasswordPage()
	{
	    return "forgot-password";
	}
	
	@PostMapping("/forgot-password")
	public String verifyEmail(
	        @RequestParam("email") String email,
	        RedirectAttributes redirectAttributes)
	{
	    try
	    {
	        userService.findByEmail(email);

	        return "redirect:/reset-password?email=" + email;
	    }
	    catch(RuntimeException e)
	    {
	        redirectAttributes.addFlashAttribute(
	                "error",
	                e.getMessage());

	        return "redirect:/forgot-password";
	    }
	}
	
	@GetMapping("/reset-password")
	public String showResetPasswordPage(
	        @RequestParam("email") String email,
	        Model model)
	{
	    model.addAttribute("email", email);

	    return "reset-password";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(
	        @RequestParam String email,
	        @RequestParam String newPassword,
	        @RequestParam String confirmPassword,
	        RedirectAttributes redirectAttributes)
	{
	    try
	    {
	        if(!newPassword.equals(confirmPassword))
	        {
	            redirectAttributes.addFlashAttribute(
	                    "error",
	                    "Passwords do not match");

	            return "redirect:/reset-password?email=" + email;
	        }

	        userService.resetPassword(
	                email,
	                newPassword);

	        redirectAttributes.addFlashAttribute(
	                "success",
	                "Password reset successful. Please login.");

	        return "redirect:/login";
	    }
	    catch(RuntimeException e)
	    {
	        redirectAttributes.addFlashAttribute(
	                "error",
	                e.getMessage());

	        return "redirect:/reset-password?email=" + email;
	    }
	}


}
