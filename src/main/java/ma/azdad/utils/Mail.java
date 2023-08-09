package ma.azdad.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.util.ByteArrayDataSource;

import ma.azdad.service.ThymeLeafService;
import ma.azdad.service.UtilsFunctions;

public class Mail {

	private String to;
	private String[] cc;
	private String subject;
	private String message;
	private TemplateType templateType;
	private String template;

	private Boolean multipart = false;
	private Map<String, Object> parameterMap = new HashMap<>();
	private Map<String, File> inlineMap = new HashMap<>();
	private Map<String, File> attachmentMap = new HashMap<>();
	private Map<String, ByteArrayDataSource> dataSourceAttachmentMap = new HashMap<>();

	public Mail(String to, String subject, String template, TemplateType templateType, String... cc) {
		super();
		this.to = to;
		this.subject = subject;
		this.template = template;
		this.templateType = templateType;
		this.cc = cc;
	}

	public Mail(String to, String subject, String message) {
		super();
		this.to = to;
		this.subject = subject;
		this.message = message;
	}

	public void generateMessageFromTemplate(ThymeLeafService thymeLeafService) {
		message = thymeLeafService.generate(templateType, template, parameterMap);
	}

	public void addParameter(String name, Object value) {
		parameterMap.put(name, value);
	}

	public void addInline(String contentId, File file) {
		multipart = true;
		inlineMap.put(contentId, file);
		addParameter(contentId, contentId); // it s correct
	}

	public void addAttachment(String contentId, File file) {
		multipart = true;
		attachmentMap.put(contentId, file);
	}

	public void addAttachment(String contentId, ByteArrayDataSource byteArrayDataSource) {
		multipart = true;
		dataSourceAttachmentMap.put(contentId, byteArrayDataSource);
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public InternetAddress[] getCc() {
		if (cc == null || cc.length == 0)
			return null;
		return Arrays.stream(cc).filter(i -> i != null && UtilsFunctions.validateEmail(i)).map(i -> generateInternetAddress(i)).toArray(InternetAddress[]::new);

//		List<String> l = new ArrayList<>(Arrays.asList(cc)) ;
//
//		List<InternetAddress> list =Arrays.stream(cc).filter(i -> i != null && UtilsFunctions.validateEmail(i)).map(i -> generateInternetAddress(i)).collect(Collectors.toList());
//		return list.toArray(new InternetAddress[list.size()]);

	}

	private InternetAddress generateInternetAddress(String email) {
		try {
			return new InternetAddress(email);
		} catch (AddressException e) {
			return null;
		}
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getMultipart() {
		return multipart;
	}

	public void setMultipart(Boolean multipart) {
		this.multipart = multipart;
	}

	public Map<String, File> getInlineMap() {
		return inlineMap;
	}

	public void setInlineMap(Map<String, File> inlineMap) {
		this.inlineMap = inlineMap;
	}

	public Map<String, File> getAttachmentMap() {
		return attachmentMap;
	}

	public void setAttachmentMap(Map<String, File> attachmentMap) {
		this.attachmentMap = attachmentMap;
	}

	public Map<String, ByteArrayDataSource> getDataSourceAttachmentMap() {
		return dataSourceAttachmentMap;
	}

	public void setDataSourceAttachmentMap(Map<String, ByteArrayDataSource> dataSourceAttachmentMap) {
		this.dataSourceAttachmentMap = dataSourceAttachmentMap;
	}

}
