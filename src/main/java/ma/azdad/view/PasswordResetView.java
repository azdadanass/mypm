package ma.azdad.view;

import java.io.IOException;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.service.EmailService;
import ma.azdad.service.SmsService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PasswordResetView {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SmsService smsService;

	private String input;
	private String generatedCode;
	private String code;
	private User user;
	private Integer step = 1;
	private String password;
	private String confirmation;

	public void sendPasswordResetCode() throws IOException {
		if (step != 1)
			return;
		try {
			if (input.contains("@"))
				user = userService.findByEmail(input);
			else
				user = userService.findByPhone(input);
		} catch (Exception e) {
			user = null;
		}

		if (user == null) {
			FacesContextMessages.ErrorMessages("Email / Phone Number not found in database !");
			return;
		}

		generatedCode = generateRandomCode();

		emailService.sendPasswordResetNotification(user, generatedCode);
		smsService.sendSms(user.getPhone(), "Dear " + user.getFullName() + ", Based on your request to reset the password,  Please use the code " + generatedCode + " for authentication");
		step++;
	}

	public void checkCode() {
		if (step != 2)
			return;
		if (generatedCode.equals(code))
			step++;
		else
			FacesContextMessages.ErrorMessages("Incorrect code !");
	}

	public void resetPassword() throws IOException {
		if (step != 3)
			return;
		if (!password.equals(confirmation)) {
			FacesContextMessages.ErrorMessages("Confirmation dosent match");
			return;
		}

		String newPasswordMD5 = UtilsFunctions.stringToMD5(password);

		userService.updatePassword(user.getUsername(), newPasswordMD5);

		step++;

		FacesContextMessages.InfoMessages("Password Changed !");
		emailService.sendPasswordChangedNotification(user);
	}

	private String generateRandomCode() {
		return String.format("%04d", (int) (Math.random() * 10000));
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public SmsService getSmsService() {
		return smsService;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode) {
		this.generatedCode = generatedCode;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

}