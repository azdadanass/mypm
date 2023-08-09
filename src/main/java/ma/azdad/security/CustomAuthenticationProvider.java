package ma.azdad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ma.azdad.model.User;
import ma.azdad.service.UserService;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private UserService userService;
	
	private Boolean test_cookie= false;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		System.out.println("authenticate !!!");
		
		String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
		User user = userService.findByLogin(auth.getName());
		if ((user == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}
		if (test_cookie) {
			if (!verificationCode.equals(user.getSecret())) {
				throw new BadCredentialsException("Invalid verfication code");
			}
		}

		Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}