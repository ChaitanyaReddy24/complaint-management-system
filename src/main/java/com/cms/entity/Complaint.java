package com.cms.entity;

import java.time.LocalDateTime;
import com.cms.enums.ComplaintStatus;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
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
@Table(name="complaints")
public class Complaint 
{

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long complaintId;
	
	@NotBlank(message = "Title cannot be empty..!")
	private String title;
	
	@NotBlank(message = "Description cannot be empty..!")
	private String description;
	
	@Enumerated(EnumType.STRING)
	private ComplaintStatus status;
	
	@Column(name = "created_date")
	private LocalDateTime createdDate;
	
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
	
//	@NotNull(message = "Customer is required")
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private User customer;
	
	@PrePersist
	public void onCreate()
	{
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate()
	{
		this.updatedDate = LocalDateTime.now();
	}
	
	@Transient
	public String getFormattedCreatedDate()
	{
	    return DateAndTime.format(createdDate);
	}

	@Transient
	public String getFormattedUpdatedDate()
	{
	    return DateAndTime.format(updatedDate);
	}	
}
