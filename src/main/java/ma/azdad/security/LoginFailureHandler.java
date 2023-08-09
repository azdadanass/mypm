package ma.azdad.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.service.AccessLogService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserService userService;

	@Autowired
	AccessLogService accessLogService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String login = request.getParameter("username");
		String ip = UtilsFunctions.getRemoteIpAddress(request);
		User user = userService.findByLogin(login);
		if (user != null) {
			if (user.isEnabled() && user.isAccountNonLocked()) {
				if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
					userService.increaseFailedAttempts(user);
				} else {
					userService.lock(user);
					exception = new LockedException("Your account has been locked due to " + UserService.MAX_FAILED_ATTEMPTS + " failed attempts." + " It will be unlocked after " + UtilsFunctions.convertDurationToText(UserService.LOCK_TIME_DURATION));
					accessLogService.addError(login + " has been locked due to " + UserService.MAX_FAILED_ATTEMPTS + " failed attempts", ip);
				}
			} else if (!user.isAccountNonLocked()) {
				if (userService.unlockWhenTimeExpired(user)) {
					exception = new LockedException("Your account has been unlocked. Please try to login again.");
					accessLogService.addInfo(login + " has been unlocked", ip);
				} else
					accessLogService.addError(login + " tried to connect,but account is locked", ip);
			}
			if (exception instanceof BadCredentialsException)
				accessLogService.addError("Incorrect Login attempt  " + login + " (bad password)", ip);
			else if (exception instanceof DisabledException)
				accessLogService.addError("Incorrect Login attempt  " + login + " (Disabled User)", ip);
		} else
			accessLogService.addError("Incorrect Login attempt  " + login + " (login not found)", ip);

		super.setDefaultFailureUrl("/login.xhtml");
		super.onAuthenticationFailure(request, response, exception);
	}

}