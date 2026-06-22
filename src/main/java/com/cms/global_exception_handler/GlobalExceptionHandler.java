package com.cms.global_exception_handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cms.custom_exceptions.AssignmentNotFoundException;
import com.cms.custom_exceptions.ComplaintNotFoundException;
import com.cms.custom_exceptions.EmailAlreadyExistsException;
import com.cms.custom_exceptions.FeedbackAlreadyExistsException;
import com.cms.custom_exceptions.FeedbackNotFoundException;
import com.cms.custom_exceptions.InvalidInputException;
import com.cms.custom_exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(
            UserNotFoundException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "User Not Found");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(ComplaintNotFoundException.class)
    public String handleComplaintNotFound(
            ComplaintNotFoundException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Complaint Not Found");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public String handleAssignmentNotFound(
            AssignmentNotFoundException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Assignment Not Found");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(FeedbackNotFoundException.class)
    public String handleFeedbackNotFound(
            FeedbackNotFoundException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Feedback Not Found");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(FeedbackAlreadyExistsException.class)
    public String handleFeedbackAlreadyExists(
            FeedbackAlreadyExistsException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Feedback Already Submitted");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Email Already Exists");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(InvalidInputException.class)
    public String handleInvalidInput(
            InvalidInputException ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Invalid Input");

        model.addAttribute(
                "errorMessage",
                ex.getMessage());

        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception ex,
            Model model)
    {
        model.addAttribute(
                "title",
                "Unexpected Error");

        model.addAttribute(
                "errorMessage",
                "Something went wrong. Please try again.");

        return "error-page";
    }
}

