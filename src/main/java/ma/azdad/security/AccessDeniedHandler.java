package ma.azdad.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ma.azdad.service.AccessLogService;
import ma.azdad.service.UtilsFunctions;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

	@Autowired
	AccessLogService accessLogService;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			String message = "User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI();
			System.out.println(request.getRemoteAddr());
			accessLogService.addError(message, UtilsFunctions.getRemoteIpAddress(request));
		}
		response.sendRedirect(request.getContextPath() + "/ad.xhtml");
	}
}