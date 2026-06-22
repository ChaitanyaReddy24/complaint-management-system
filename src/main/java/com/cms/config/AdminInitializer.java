package com.cms.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cms.entity.User;
import com.cms.enums.Role;
import com.cms.repository.IUserRepository;

@Component
public class AdminInitializer implements CommandLineRunner
{

	private final IUserRepository userRepo;
	
	
	
	public AdminInitializer(IUserRepository userRepo)
	{
		this.userRepo = userRepo;
	}



	@Override
	public void run(String... args) throws Exception
	{
		boolean adminIsPresent = userRepo.findByEmail("admin@cms.com").isPresent();
		if(!adminIsPresent)
		{
			User admin = new User();
			
			admin.setName("System Admin");
            admin.setEmail("admin@cms.com");
            admin.setMobile("9876543210");
            admin.setPassword("Admin@123");
            admin.setRole(Role.ADMIN);
            
            userRepo.save(admin);
            
            System.out.println("Admin user created successfully..!");
			
		}
		
	}

}
