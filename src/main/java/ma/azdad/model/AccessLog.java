package ma.azdad.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccessLog extends GenericModel<Integer> {

	private String app = "iadmin";
	private Date date;
	private AccessLogType type;
	private String message;
	private String ip;

	public AccessLog() {
	}

	public AccessLog(Date date, AccessLogType type, String message, String ip) {
		super();
		this.date = date;
		this.message = message;
		this.ip = ip;
		this.type = type;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, app, date, type != null ? type.getValue() : "", message, ip);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(nullable = false, columnDefinition = "TEXT")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@Enumerated(EnumType.STRING)
	public AccessLogType getType() {
		return type;
	}

	public void setType(AccessLogType type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "\nAccessLog [app=" + app + ", date=" + date + ", type=" + type + ", message=" + message + ", ip=" + ip
				+ "]";
	}
	
	

}
