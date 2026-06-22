package com.cms.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.custom_exceptions.EmailAlreadyExistsException;
import com.cms.custom_exceptions.InvalidInputException;
import com.cms.custom_exceptions.UserNotFoundException;
import com.cms.entity.User;
import com.cms.enums.Role;
import com.cms.repository.IUserRepository;
import com.cms.service.IUserService;

@Service
public class UserServiceImpl implements IUserService
{

	private final IUserRepository userRepo;



	//	@Autowired    //Autowired is optional when we are using constructor injection...
	public UserServiceImpl(IUserRepository userRepo) {
		this.userRepo = userRepo;
	}

	//Register User Method //
	@Override
	public User registerUser(User user) 
	{

		// Validating user data is there or not
		if(user==null) throw new IllegalArgumentException("User cannot be null");

		// Validating Email already exists or not
		Optional<User> existingByEmail = userRepo.findByEmail(user.getEmail());

		if(existingByEmail.isPresent()) throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());

		// Validating Mobile Number already exists or not
		Optional<User> existingByMobile = userRepo.findByMobile(user.getMobile());

		//		if(existingByMobile.isPresent()) throw new MobileAlreadyExistsException("Mobile already exists: " + user.getMobile());

		//Setting default role as a customer..
		//		user.setRole(Role.CUSTOMER);

		// saving an user and returning the User object to caller of this method...
		return userRepo.save(user);

	}

	@Override
	public User login(String input, String password) 
	{
		//Validating have they passed input or not..!
		if(input == null || input.isBlank())
		{
			throw new InvalidInputException("Email/Mobile cannot be empty");
		}

		if(password == null || password.isBlank())
		{
			throw new InvalidInputException("Password cannot be empty");
		}


		Optional<User> optional;
		// Validating Mobile Number if they enter 10 digits
		if(input.matches("\\d{10}"))
		{
			optional = userRepo.findByMobile(input);
		}
		// Validating email if they are not enter 10 digits
		else
		{
			optional = userRepo.findByEmail(input);
		}

		//throwing an exception if both email and mobile failed to validate
		User user = optional.orElseThrow(
				()-> new UserNotFoundException("User not found with - "+ input ));

		// Validating password
		if(!password.equals(user.getPassword())) 
		{
			throw new InvalidInputException("Invalid password");
		}

		return user;
	}

	@Override
	public User findById(Long userId)
	{	
		return userRepo.findById(userId).orElseThrow(
				()-> new UserNotFoundException("User not found with - "+ userId ));
	}


	@Override
	public User findByEmail(String email) 
	{
		return userRepo.findByEmail(email).orElseThrow(
				()-> new UserNotFoundException("User not found with - "+ email ));
	}

	@Override
	public User findByMobile(String mobile) 
	{
		return userRepo.findByMobile(mobile).orElseThrow(
				()-> new UserNotFoundException("User not found with - "+ mobile ));
	}

	@Override
	public List<User> getAllEmployees()
	{
		return userRepo.findByRole(Role.EMPLOYEE);
	}

	@Override
	public Page<User> getEmployees(Integer pageNo)
	{
		Pageable pageable = PageRequest.of(pageNo, 5);

		return userRepo.findByRole(Role.EMPLOYEE, pageable);
	}

	@Override
	public User updateEmployee(User employee)
	{
		User existingEmployee =
				findById(employee.getUserId());

		existingEmployee.setName(employee.getName());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setMobile(employee.getMobile());
		existingEmployee.setPassword(employee.getPassword());

		return userRepo.save(existingEmployee);
	}

	@Override
	public void deleteEmployee(Long employeeId)
	{
		User employee = findById(employeeId);

		if(employee.getRole() != Role.EMPLOYEE)
		{
			throw new InvalidInputException(
					"Only employee records can be deleted");
		}

		userRepo.delete(employee);
	}

	@Override
	public void resetPassword(
			String email,
			String newPassword)
	{
		User user = findByEmail(email);

		if(newPassword == null || newPassword.isBlank())
		{
			throw new InvalidInputException(
					"Password cannot be empty");
		}

		if(newPassword.length() < 6)
		{
			throw new InvalidInputException(
					"Password must contain at least 6 characters");
		}

		user.setPassword(newPassword);

		userRepo.save(user);
	}
}
