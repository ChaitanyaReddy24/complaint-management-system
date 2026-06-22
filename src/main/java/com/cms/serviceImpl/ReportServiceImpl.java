package com.cms.serviceImpl;

import org.springframework.stereotype.Service;

import com.cms.dto.DashboardReport;
import com.cms.enums.ComplaintStatus;
import com.cms.enums.Role;
import com.cms.repository.IComplaintRepository;
import com.cms.repository.IFeedbackRepository;
import com.cms.repository.IUserRepository;
import com.cms.service.IReportService;

@Service
public class ReportServiceImpl
        implements IReportService
{
    private final IComplaintRepository complaintRepo;
    private final IUserRepository userRepo;
    private final IFeedbackRepository feedbackRepo;

    public ReportServiceImpl(
            IComplaintRepository complaintRepo,
            IUserRepository userRepo,
            IFeedbackRepository feedbackRepo)
    {
        this.complaintRepo = complaintRepo;
        this.userRepo = userRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public DashboardReport getDashboardReport()
    {
        DashboardReport report =
                new DashboardReport();

        report.setTotalComplaints(
                complaintRepo.count());

        report.setOpenComplaints(
                complaintRepo.countByStatus(
                        ComplaintStatus.OPEN));

        report.setAssignedComplaints(
                complaintRepo.countByStatus(
                        ComplaintStatus.ASSIGNED));

        report.setInProgressComplaints(
                complaintRepo.countByStatus(
                        ComplaintStatus.IN_PROGRESS));

        report.setResolvedComplaints(
                complaintRepo.countByStatus(
                        ComplaintStatus.RESOLVED));

        report.setTotalEmployees(
                userRepo.countByRole(
                        Role.EMPLOYEE));

        report.setTotalFeedbacks(
                feedbackRepo.count());

        Double avg =
                feedbackRepo.getAverageRating();

        report.setAverageRating(
                avg == null ? 0.0 : avg);

        return report;
    }
}