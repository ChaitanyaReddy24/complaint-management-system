package com.cms.entity;


import java.time.LocalDateTime;

import com.cms.enums.Role;
import com.cms.util.DateAndTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity						//If we use name for entity, it will effect our jpql or hql code..
@Table(name="users")		//  That's why I am using @Table(name="users")..
public class User 
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	
	@NotBlank(message = "Name cannot be Empty..!")
	private String name;

	@NotBlank(message ="Email cannot be empty..!")
	@Email(message = "Invalid Email..!")
	@Column(unique = true)
	private String email;
	
	@Pattern(
			regexp="^[6-9][0-9]{9}$",
			message = "Mobile number must start with 6-9 and contain 10 digits..!")
	private String mobile;
	@Pattern(
			regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$!%^&*()_+=]).{8,}$",
			message ="Password must contain uppercase, lowercase, number, special character and be at least 8 characters long...!"
			)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(name ="created_date")
	private LocalDateTime createdDate;
	
	
	@PrePersist
	void onCreate()
	{
		this.createdDate = LocalDateTime.now();
	}
	
	@Transient
	public String getFormattedCreatedDate()
	{
	    return DateAndTime.format(createdDate);
	}
}
