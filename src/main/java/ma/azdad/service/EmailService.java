package ma.azdad.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.utils.App;
import ma.azdad.utils.Mail;
import ma.azdad.utils.TemplateType;

@Component
@Transactional
public class EmailService {
	protected final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Value("#{'${spring.profiles.active}'.replaceAll('-dev','')}")
	private String erp;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private ThymeLeafService thymeLeafService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${applicationName}")
	private String applicationName;

	@Async
	public void sendPasswordResetNotification(User user, String code) throws IOException {
		String to = user.getEmail();
		String subject = "Password Reset";
		Mail mail = new Mail(to, subject, erp + "/passwordReset.html", TemplateType.HTML);
		mail.addParameter("user", user);
		mail.addParameter("code", code);
		mail.addParameter("currentDate", new Date());
		mail.addInline("app_icon", resourceLoader.getResource("classpath:img/app_icon.png").getFile());
		mail.addInline("erp_logo", resourceLoader.getResource("classpath:img/" + erp + "/logo.png").getFile());
		if ("orange".contentEquals(erp))
			mail.addInline("telo", resourceLoader.getResource("classpath:img/orange/telo.png").getFile());
		mail.generateMessageFromTemplate(thymeLeafService);
		send(mail);
	}

	@Async
	public void sendPasswordChangedByAdminNotification(User user, String password) throws IOException {
		String to = user.getEmail();
		String subject = "Password Reset !";
		Mail mail = new Mail(to, subject, erp + "/passwordChangedByAdmin.html", TemplateType.HTML);
		mail.addParameter("user", user);
		mail.addParameter("date", new Date());
		mail.addParameter("password", password);
		mail.addParameter("currentDate", new Date());
		mail.addInline("app_icon", resourceLoader.getResource("classpath:img/app_icon.png").getFile());
		mail.addInline("erp_logo", resourceLoader.getResource("classpath:img/" + erp + "/logo.png").getFile());
		mail.addInline("img1", resourceLoader.getResource("classpath:img/passwordResetImg1.png").getFile());
		if ("orange".contentEquals(erp))
			mail.addInline("telo", resourceLoader.getResource("classpath:img/orange/telo.png").getFile());
		mail.generateMessageFromTemplate(thymeLeafService);
		send(mail);
	}

	@Async
	public void sendPasswordChangedNotification(User user) throws IOException {
		String to = user.getEmail();
		String subject = "Your password has been changed !";
		Mail mail = new Mail(to, subject, erp + "/passwordChanged.html", TemplateType.HTML);
		mail.addParameter("user", user);
		mail.addParameter("currentDate", new Date());
		mail.addInline("app_icon", resourceLoader.getResource("classpath:img/app_icon.png").getFile());
		mail.addInline("erp_logo", resourceLoader.getResource("classpath:img/" + erp + "/logo.png").getFile());
		if ("orange".contentEquals(erp))
			mail.addInline("telo", resourceLoader.getResource("classpath:img/orange/telo.png").getFile());
		mail.generateMessageFromTemplate(thymeLeafService);
		send(mail);
	}

	@Async
	public void sendLobChangedNotification(User user) {
		String to = user.getEmail();
		String subject = "Your LOB has been changed !";
		Mail mail = new Mail(to, subject, "lobChanged.html", TemplateType.HTML);
		mail.addParameter("user", user);
		mail.addParameter("lobName", user.getAffectation().getLobName());
		mail.generateMessageFromTemplate(thymeLeafService);
		send(mail);
	}

	@Async
	public void sendLmChangedNotification(User user) {
		String to = user.getEmail();
		String subject = "Your Line Manager has been changed !";
		Mail mail = new Mail(to, subject, "lmChanged.html", TemplateType.HTML);
		mail.addParameter("user", user);
		mail.addParameter("lmName", user.getAffectation().getLineManager().getName());
		mail.generateMessageFromTemplate(thymeLeafService);
		send(mail);
	}

	public void sendSimpleMail(String to, String subject, String message) {
		send(new Mail(to, subject, message));
	}

	public void sendTestMail() {
		String to = "a.azdad@3gcom-int.com";
		String subject = "testMail";
		String cc1 = "azdadanass@gmail.com";
		String cc2 = "gcomappstest@gmail.com";

		Mail mail = new Mail(to, subject, "test.html", TemplateType.HTML, cc1, cc2);
		mail.addParameter("name", "A A");
		mail.addParameter("subscriptionDate", new Date());
		mail.addParameter("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
		mail.addInline("img1", new File("/home/azdad/Bureau/test.png"));
		mail.addAttachment("attach1.pdf", new File("/home/azdad/Documents/sample.pdf"));
		mail.generateMessageFromTemplate(thymeLeafService);

		send(mail);
	}

	private void send(final Mail mail) {
		if (!UtilsFunctions.validateEmail(mail.getTo()))
			return;
		mailSender.send(new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException, UnsupportedEncodingException {
				log.info("------------------------------------------------------");
				log.info("Send mail");
				log.info("\tto : " + mail.getTo());
				log.info("\tmultipart : " + mail.getMultipart());
				log.info("\tSubject : " + mail.getSubject());
				log.info("------------------------------------------------------");
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, mail.getMultipart(), "UTF-8");
				message.setFrom(App.SYSTEM_MAIL.getLink(), applicationName + " " + getErpName());
				message.setTo(mail.getTo());
				message.setSubject(mail.getSubject());
				message.setText(mail.getMessage(), true);
				if (mail.getCc() != null)
					message.setCc(mail.getCc());

				if (mail.getMultipart()) {
					if (mail.getInlineMap() != null)
						mail.getInlineMap().forEach((x, y) -> {
							try {
								message.addInline(x, y);
							} catch (MessagingException e) {
								e.printStackTrace();
							}
						});
					if (mail.getAttachmentMap() != null)
						mail.getAttachmentMap().forEach((x, y) -> {
							try {
								message.addAttachment(x, y);
							} catch (MessagingException e) {
								e.printStackTrace();
							}
						});
					if (mail.getDataSourceAttachmentMap() != null)
						mail.getDataSourceAttachmentMap().forEach((x, y) -> {
							try {
								message.addAttachment(x, y);
							} catch (MessagingException e) {
								e.printStackTrace();
							}
						});
				}
			}
		});
	}

	private String getErpName() {
		switch (erp) {
		case "gcom":
			return "3Gcom";
		case "orange":
			return "Orange";
		case "mise":
			return "Mise";
		default:
			return "";
		}
	}

}
