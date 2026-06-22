package com.cms.entity;


import java.time.LocalDateTime;
import com.cms.util.DateAndTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "feedbacks")
public class Feedback 
{

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	private Long feedbackId;
	
	@NotNull(message = "Complaint is required")
	@OneToOne
	@JoinColumn(name = "complaint_id", unique = true)
	private Complaint complaint;
	
	@Min(value = 1, message = "Rating should be between 1 and 5")
	@Max(value = 5, message = "Rating should be between 1 and 5")
	@NotNull(message = "Rating is required")
	private Integer rating;
	
	@NotBlank(message = "Comments cannot be empty..!")
	private String comments;
	
	@Column(name = "feedback_date")
	private LocalDateTime feedbackDate;
	
	@PrePersist
	public void onCreate()
	{
		this.feedbackDate=LocalDateTime.now();
	}
	
	@Transient
	public String getFormattedCreatedDate()
	{
	    return DateAndTime.format(feedbackDate);
	}
}
