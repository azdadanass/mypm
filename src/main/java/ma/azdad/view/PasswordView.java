package ma.azdad.view;

import java.io.IOException;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.service.EmailService;
import ma.azdad.service.SmsService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PasswordView {

	@Autowired
	protected UserService userService;

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected EmailService emailService;

	@Autowired
	protected SmsService smsService;

	private String oldPassword;
	private String newPassword;
	private String confirmation;

	public void updatePassword() throws IOException {

		String oldPasswordMD5 = UtilsFunctions.stringToMD5(oldPassword);
		if (!sessionView.getUser().getPassword().equals(oldPasswordMD5)) {
			FacesContextMessages.ErrorMessages("Old Password Incorrect");
			return;
		}
		if (!newPassword.equals(confirmation)) {
			FacesContextMessages.ErrorMessages("Confirmation dosent match");
			return;
		}

		String newPasswordMD5 = UtilsFunctions.stringToMD5(newPassword);
		userService.updatePassword(sessionView.getUsername(), newPasswordMD5);
		FacesContextMessages.InfoMessages("Password Changed !");
		emailService.sendPasswordChangedNotification(sessionView.getUser());
		smsService.sendSms(sessionView.getUser().getPhone(), "Dear " + sessionView.getUser().getFullName() + ", Your password has been changed successfully on the Orange application system");
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

}