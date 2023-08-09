package ma.azdad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Delegation extends GenericModel<Integer> {

	private Date createDate;
	private Date startDate;
	private Date endDate;
	private String description;
	private String status;
//	private Boolean partial;
//	private Boolean pm = false;
//	private Boolean lm = false;

	private DelegationType type;

	private User delegator;
	private User delegate;

	private List<DelegationDetail> detailList = new ArrayList<>();

	public Delegation() {
	}

	public Delegation(Integer id, Date startDate, Date endDate, String description, String status, String delegateFullName, String delegatePhoto, String delegatorFullName) {
		super(id);
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.status = status;
		this.setDelegateFullName(delegateFullName);
		this.setDelegatePhoto(delegatePhoto);
		this.setDelegatorFullName(delegatorFullName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, description, startDate, endDate, getDelegateFullName());
	}

	public void calculateStatus() {
		Date currentDate = new Date();
		if (currentDate.compareTo(endDate) > 0)
			status = "Expired";
		else if (currentDate.compareTo(startDate) < 0)
			status = "Pending";
		else
			status = "Active";
	}

	@Transient
	public Boolean getIsProject() {
		return DelegationType.PROJECT.equals(type);
	}

	@Transient
	public Boolean getIsLob() {
		return DelegationType.LOB.equals(type);
	}

	@Transient
	public Boolean getIsUser() {
		return DelegationType.USER.equals(type);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	public void addDetail(DelegationDetail detail) {
		detail.setDelegation(this);
		detailList.add(detail);
	}

	public void removeDetail(DelegationDetail detail) {
		detail.setDelegation(null);
		detailList.remove(detail);
	}

	public void removeDetailList() {
		detailList.forEach(i -> i.setDelegation(null));
		detailList.clear();
	}

	@Transient
	public List<DelegationDetail> getProjectDetailList() {
		return detailList.stream().filter(i -> DelegationDetailType.PM.equals(i.getType())).collect(Collectors.toList());
	}

	@Transient
	public List<DelegationDetail> getUserDetailList() {
		return detailList.stream().filter(i -> DelegationDetailType.LM.equals(i.getType())).collect(Collectors.toList());
	}

	@Transient
	public String getDelegateFullName() {
		return delegate == null ? null : delegate.getFullName();
	}

	@Transient
	public void setDelegateFullName(String delegateFullName) {
		if (delegate == null)
			delegate = new User();
		delegate.setFullName(delegateFullName);
	}

	@Transient
	public String getDelegatePhoto() {
		return delegate == null ? null : delegate.getPhoto();
	}

	@Transient
	public void setDelegatePhoto(String delegatePhoto) {
		if (delegate == null)
			delegate = new User();
		delegate.setPhoto(delegatePhoto);
	}

	@Transient
	public String getDelegatorFullName() {
		return delegator == null ? null : delegator.getFullName();
	}

	@Transient
	public void setDelegatorFullName(String delegatorFullName) {
		if (delegator == null)
			delegator = new User();
		delegator.setFullName(delegatorFullName);
	}

	@Transient
	public String getDelegatorPhoto() {
		return delegator == null ? null : delegator.getPhoto();
	}

	@Transient
	public void setDelegatorPhoto(String delegatorPhoto) {
		if (delegator == null)
			delegator = new User();
		delegator.setPhoto(delegatorPhoto);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iddelegation")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegator", nullable = false)
	public User getDelegator() {
		return delegator;
	}

	public void setDelegator(User delegator) {
		this.delegator = delegator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegate", nullable = false)
	public User getDelegate() {
		return delegate;
	}

	public void setDelegate(User delegate) {
		this.delegate = delegate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createdate", length = 10)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "startdate", length = 10)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "enddate", length = 10)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "status", length = 50)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "delegation", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DelegationDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DelegationDetail> detailList) {
		this.detailList = detailList;
	}

//	public Boolean getPartial() {
//		return partial;
//	}
//
//	public void setPartial(Boolean partial) {
//		this.partial = partial;
//	}
//
//	public Boolean getPm() {
//		return pm;
//	}
//
//	public void setPm(Boolean pm) {
//		this.pm = pm;
//	}
//
//	public Boolean getLm() {
//		return lm;
//	}
//
//	public void setLm(Boolean lm) {
//		this.lm = lm;
//	}

	@Enumerated(EnumType.STRING)
	public DelegationType getType() {
		return type;
	}

	public void setType(DelegationType type) {
		this.type = type;
	}

}
