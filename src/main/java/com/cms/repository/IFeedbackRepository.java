package com.cms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cms.entity.Feedback;

public interface IFeedbackRepository extends JpaRepository<Feedback, Long>
{

	Optional<Feedback> findByComplaintComplaintId(Long complaintId);
	
	@Query("SELECT AVG(f.rating) FROM Feedback f")
	Double getAverageRating();
}
