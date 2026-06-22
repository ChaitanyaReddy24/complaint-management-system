package com.cms.entity;


import java.time.LocalDateTime;
import com.cms.enums.AssignmentStatus;
import com.cms.util.DateAndTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
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
@Entity
@Table(name="assignments")
public class Assignment 
{

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long assignmentId;
	
	@Column(name ="assigned_date")
	private LocalDateTime assignedDate;
	

	private String remarks;
	

	@Enumerated(EnumType.STRING)
	private AssignmentStatus status;

	@NotNull(message = "Complaint is required")
	@JoinColumn(name = "complaint_id")
	@OneToOne
	private Complaint complaint;

	@NotNull(message = "Employee is required")
	@JoinColumn(name = "employee_id")
	@ManyToOne
	private User employee;
	
	@PrePersist
	public void onCreate()
	{
		this.assignedDate = LocalDateTime.now();
	}
	
	@Transient
	public String getFormattedCreatedDate()
	{
	    return DateAndTime.format(assignedDate);
	}

	
}
