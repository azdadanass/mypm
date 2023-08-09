package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
public class Assignment extends GenericModel<Integer> implements Serializable {

	private Date creationDate;
	private Date startDate;
	private Date endDate;
	private String description;

	private User assignator;
	private User user;

	private List<AssignmentDetail> detailList = new ArrayList<>();

	// TMP
	private String status;
	private String userUsername;

	public Assignment() {
		super();
	}

	public Assignment(User assignator) {
		super();
		this.assignator = assignator;
	}

	public void init() {
		if (user != null)
			userUsername = user.getUsername();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && description != null)
			result = description.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getStatus() {
		if (status == null) {
			Date currentDate = new Date();
			status = UtilsFunctions.compareDates(currentDate, startDate) >= 0 && UtilsFunctions.compareDates(currentDate, endDate) <= 0 ? "Active" : "Inactive";
		}
		return status;
	}

	@Transient
	public String getStatusStyleClass() {
		return "Active".equals(getStatus()) ? "badge-success" : "badge-danger";
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getAssignator() {
		return assignator;
	}

	public void setAssignator(User assignator) {
		this.assignator = assignator;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment", cascade = CascadeType.ALL)
	public List<AssignmentDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<AssignmentDetail> detailList) {
		this.detailList = detailList;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Transient
	public String getUserUsername() {
		return userUsername;
	}

	@Transient
	public void setUserUsername(String userUsername) {
		this.userUsername = userUsername;
	}

}
