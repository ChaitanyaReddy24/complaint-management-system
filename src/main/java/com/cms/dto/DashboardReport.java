package com.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardReport
{
    private Long totalComplaints;
    private Long openComplaints;
    private Long assignedComplaints;
    private Long inProgressComplaints;
    private Long resolvedComplaints;
    private Long totalEmployees;
    private Long totalFeedbacks;
    private Double averageRating;

    // Getters and Setters
}