package com.cms.util;

import com.cms.entity.User;
import com.cms.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor
{

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception
	{
		HttpSession session = request.getSession(false);

		if(session == null)
		{
			response.sendRedirect("/login");
			return false;
		}

		User user =
				(User) session.getAttribute(
						"loggedInUser");

		if(user == null)
		{
			response.sendRedirect("/login");
			return false;
		}

		String uri = request.getRequestURI();

		if(uri.startsWith("/admin")
				&& user.getRole() != Role.ADMIN)
		{
			response.sendRedirect("/access-denied");
			return false;
		}

		if(uri.startsWith("/employee")
				&& user.getRole() != Role.EMPLOYEE)
		{
			response.sendRedirect("/access-denied");
			return false;
		}

		return true;
	}
}