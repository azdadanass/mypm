package ma.azdad.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private String verificationCode;

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		verificationCode = request.getParameter("code");
		Arrays.stream(request.getCookies()).forEach(c -> {
			System.out.println(c.getName() + ":" + c.getValue());
		});
	}

	public String getVerificationCode() {
		return verificationCode;
	}
}