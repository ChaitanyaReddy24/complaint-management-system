package com.cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.entity.User;
import com.cms.enums.Role;

public interface IUserRepository extends JpaRepository<User, Long> 
{

//	@Query("FROM User u WHERE u.email=:input OR u.mobile=:input")
//	Optional<User> findByEmailOrMobile(@Param("input") String input);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByMobile(String mobile);
	
	List<User> findByRole(Role role);
	
	Page<User> findByRole(Role role, Pageable pageable);
	
	long countByRole(Role role);
}
