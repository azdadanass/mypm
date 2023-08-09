package ma.azdad.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean
@Component
@Scope("view")
public class VerifyView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private String username = "a.azdad";
	private String password = "1a1dc91c907325c69271ddf0c944bc72";
	
	private String username1 = "a.azdad";
	private String password1 = "1a1dc91c907325c69271ddf0c944bc72";

	private Boolean cookie = false;

	public void verify() {
		System.out.println("verify");
		System.out.println(username);
		System.out.println(password);
		System.out.println(username1);
		System.out.println(password1);
	}

	public Boolean getCookie() {
		return cookie;
	}

	public void setCookie(Boolean cookie) {
		this.cookie = cookie;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername1() {
		return username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	
	

}