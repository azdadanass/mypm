package ma.azdad.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class DelegationDetail extends GenericModel<Integer> {

	private DelegationDetailType type;
	private Delegation delegation;
	private Project project;
	private Lob lob;
	private User user;

	public DelegationDetail() {
	}

	public DelegationDetail(Project project) {
		this.type = DelegationDetailType.PM;
		this.project = project;
	}
	
	public DelegationDetail(Lob lob) {
		this.type = DelegationDetailType.LOB;
		this.lob = lob;
	}

	public DelegationDetail(User user) {
		this.type = DelegationDetailType.LM;
		this.user = user;
	}

	public DelegationDetail(Integer id, DelegationDetailType type, Date startDate, Date endDate, String delegateFullName, String delegatePhoto, String delegatorFullName, String userFullName,
			String projectName) {
		super(id);
		this.type = type;
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setDelegateFullName(delegateFullName);
		this.setDelegatePhoto(delegatePhoto);
		this.setDelegatorFullName(delegatorFullName);
		this.setUserFullName(userFullName);
		this.setProjectName(projectName);
	}

	@Transient
	public Date getStartDate() {
		return delegation == null ? null : delegation.getStartDate();
	}

	@Transient
	public void setStartDate(Date startDate) {
		if (delegation == null)
			delegation = new Delegation();
		delegation.setStartDate(startDate);
	}

	@Transient
	public Date getEndDate() {
		return delegation == null ? null : delegation.getEndDate();
	}

	@Transient
	public void setEndDate(Date endDate) {
		if (delegation == null)
			delegation = new Delegation();
		delegation.setEndDate(endDate);
	}

	@Transient
	public String getDelegateFullName() {
		return delegation == null ? null : delegation.getDelegateFullName();
	}

	@Transient
	public void setDelegateFullName(String delegateFullName) {
		if (delegation == null)
			delegation = new Delegation();
		delegation.setDelegateFullName(delegateFullName);
	}

	@Transient
	public String getDelegatePhoto() {
		return delegation == null ? null : delegation.getDelegatePhoto();
	}

	@Transient
	public void setDelegatePhoto(String delegatePhoto) {
		if (delegation == null)
			delegation = new Delegation();
		delegation.setDelegatePhoto(delegatePhoto);
	}

	@Transient
	public String getDelegatorFullName() {
		return delegation == null ? null : delegation.getDelegatorFullName();
	}

	@Transient
	public void setDelegatorFullName(String delegatorFullName) {
		if (delegation == null)
			delegation = new Delegation();
		delegation.setDelegatorFullName(delegatorFullName);
	}

	@Transient
	public String getUserFullName() {
		return user == null ? null : user.getFullName();
	}

	@Transient
	public void setUserFullName(String userFullName) {
		if (user == null)
			user = new User();
		user.setFullName(userFullName);
	}

	@Transient
	public String getUserPhoto() {
		return user == null ? null : user.getPhoto();
	}

	@Transient
	public void setUserPhoto(String userPhoto) {
		if (user == null)
			user = new User();
		user.setPhoto(userPhoto);
	}

	@Transient
	public String getProjectName() {
		return project == null ? null : project.getName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (project == null)
			project = new Project();
		project.setName(projectName);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public DelegationDetailType getType() {
		return type;
	}

	public void setType(DelegationDetailType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Delegation getDelegation() {
		return delegation;
	}

	public void setDelegation(Delegation delegation) {
		this.delegation = delegation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Lob getLob() {
		return lob;
	}

	public void setLob(Lob lob) {
		this.lob = lob;
	}

}
