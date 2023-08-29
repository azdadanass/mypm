package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class ToNotify extends GenericModel<Integer> implements Serializable {

	private User internalResource;
	private Boolean notifyByEmail = true;
	private Boolean notifyBySms = true;

	private User user;
	private UserAppraisal userAppraisal;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public UserAppraisal getUserAppraisal() {
		return userAppraisal;
	}

	public void setUserAppraisal(UserAppraisal userAppraisal) {
		this.userAppraisal = userAppraisal;
	}

	private String internalResourceUsername;

	public ToNotify() {
		super();
	}

	public ToNotify(User internalResource) {
		this.internalResource = internalResource;
	}
	public ToNotify(User internalResource,UserAppraisal userAppraisal) {
		super();
		this.internalResource = internalResource;
		this.userAppraisal=userAppraisal;
	}

	
	public void init() {
		if (internalResource != null)
			internalResourceUsername = internalResource.getUsername();
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && internalResource != null)
			result = internalResource.getFullName().toLowerCase().contains(query);
		if (!result && internalResource != null)
			result = internalResource.getCompanyName().toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getFullName() {
		return internalResource.getFullName();
	}

	@Transient
	public String getEmail() {
		return internalResource.getEmail();
	}

	@Transient
	public String getPhone() {
		return internalResource.getPhone();
	}

	@Transient
	public String getPhoto() {
		return internalResource.getPhoto();
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public User getInternalResource() {
		return internalResource;
	}

	public void setInternalResource(User internalResource) {
		this.internalResource = internalResource;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	@Transient
	public String getInternalResourceUsername() {
		return internalResourceUsername;
	}

	@Transient
	public void setInternalResourceUsername(String internalResourceUsername) {
		this.internalResourceUsername = internalResourceUsername;
	}

	public Boolean getNotifyByEmail() {
		return notifyByEmail;
	}

	public void setNotifyByEmail(Boolean notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

	public Boolean getNotifyBySms() {
		return notifyBySms;
	}

	public void setNotifyBySms(Boolean notifyBySms) {
		this.notifyBySms = notifyBySms;
	}

	@Override
	public String toString() {
		return "ToNotify [getFullName()=" + getFullName() + ", getEmail()=" + getEmail() + ", getPhone()=" + getPhone() + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
