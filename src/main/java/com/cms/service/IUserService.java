package com.cms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.cms.entity.User;

public interface IUserService 
{
	User registerUser(User user);

	User login(String input, String password); //input can have both mobile number and email..

	User findById(Long userId);

	User findByEmail(String email);

	User findByMobile(String mobile);
	
	List<User> getAllEmployees();
	
	Page<User> getEmployees(Integer pageNo);
	
	User updateEmployee(User employee);
	
	void deleteEmployee(Long employeeId);
	
	void resetPassword(String email, String newPassword);


}
