package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import ma.azdad.service.UtilsFunctions;

@Entity
public class Restriction extends GenericModel<Integer> {

	private RestrictionType type;
	private Date startDate;
	private Date endDate;
	private String action;
	private String reason;
	private String resolutionComment;
	private Boolean manual = true;
	private String app;

	private Boolean iadmin = false;
	private Boolean ibuy = false;
	private Boolean iexpense = false;
	private Boolean invoice = false;
	private Boolean myhr = false;
	private Boolean myreports = false;
	private Boolean mytools = false;
	private Boolean wtr = false;

	private Date creationDate;
	private Date editDate;
	private Date resolutionDate;

	private User user;
	private User requester;

	private Integer countFiles = 0;

	private List<RestrictionFile> fileList = new ArrayList<>();
	private List<RestrictionHistory> historyList = new ArrayList<>();
	private List<RestrictionComment> commentList = new ArrayList<>();

	private List<CommentGroup<RestrictionComment>> commentGroupList;

	public Restriction() {
	}

	// c1
	public Restriction(Integer id, RestrictionType type, Date startDate, Date endDate, Date resolutionDate, String reason, Boolean manual, String app, Boolean iadmin, Boolean ibuy,
			Boolean iexpense, Boolean invoice, Boolean myhr, Boolean myreports, Boolean mytools, Boolean wtr, Integer countFiles, String userFullName, String userPhoto,
			Boolean userInternal,CompanyType userCompanyType,String userCompanyName,String userCustomerName,String userSupplierName, String requesterFullName) {
		super(id);
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.resolutionDate = resolutionDate;
		this.reason = reason;
		this.manual = manual;
		this.app = app;
		this.iadmin = iadmin;
		this.ibuy = ibuy;
		this.iexpense = iexpense;
		this.invoice = invoice;
		this.myhr = myhr;
		this.myreports = myreports;
		this.mytools = mytools;
		this.wtr = wtr;
		this.countFiles = countFiles;
		this.setUserFullName(userFullName);
		this.setUserPhoto(userPhoto);
		this.setUserInternal(userInternal);
		this.setUserCompanyType(userCompanyType);
		this.setUserCompanyName(userCompanyName);
		this.setUserCustomerName(userCustomerName);
		this.setUserSupplierName(userSupplierName);
		this.setRequesterFullName(requesterFullName);

	}

	@Override
	public boolean filter(String query) {
		return contains(query, getUserFullName(), type != null ? type.getValue() : null, type != null ? type.getSystem() : null, reason);
	}

	@Transient
	public Boolean getIsActive() {
		Date currentDate = new Date();
		return startDate.compareTo(currentDate) <= 0 && (endDate == null || endDate.compareTo(currentDate) >= 0);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(RestrictionFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(RestrictionFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(RestrictionHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(RestrictionHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(RestrictionComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(RestrictionComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<RestrictionComment>> map = new HashMap<>();
		for (RestrictionComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<RestrictionComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<RestrictionComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<RestrictionComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@Transient
	public String getUserUsername() {
		return user != null ? user.getUsername() : null;
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null || !userUsername.equals(user.getUsername()))
			user = new User();
		user.setUsername(userUsername);
	}

	@Transient
	public Boolean getUserInternal() {
		return user != null ? user.getInternal() : null;
	}

	@Transient
	public void setUserInternal(Boolean userInternal) {
		if (user == null)
			user = new User();
		user.setInternal(userInternal);
	}

	@Transient
	public String getUserCustomerName() {
		return user != null ? user.getCustomerName() : null;
	}

	@Transient
	public void setUserCustomerName(String userCustomerName) {
		if (user == null)
			user = new User();
		user.setCustomerName(userCustomerName);
	}

	@Transient
	public String getUserSupplierName() {
		return user != null ? user.getSupplierName() : null;
	}

	@Transient
	public void setUserSupplierName(String userSupplierName) {
		if (user == null)
			user = new User();
		user.setSupplierName(userSupplierName);
	}

	@Transient
	public String getUserCompanyName() {
		return user != null ? user.getCompanyName() : null;
	}

	@Transient
	public void setUserCompanyName(String userCompanyName) {
		if (user == null)
			user = new User();
		user.setCompanyName(userCompanyName);
	}
	
	@Transient
	public CompanyType getUserCompanyType() {
		return user != null ? user.getCompanyType() : null;
	}

	@Transient
	public void setUserCompanyType(CompanyType userCompanyType) {
		if (user == null)
			user = new User();
		user.setCompanyType(userCompanyType);
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
	public String getRequesterUsername() {
		return requester == null ? null : requester.getUsername();
	}

	@Transient
	public void setRequesterUsername(String requesterUsername) {
		if (requester == null)
			requester = new User();
		requester.setUsername(requesterUsername);
	}

	@Transient
	public String getRequesterFullName() {
		return requester == null ? null : requester.getFullName();
	}

	@Transient
	public void setRequesterFullName(String requesterFullName) {
		if (requester == null)
			requester = new User();
		requester.setFullName(requesterFullName);
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

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public RestrictionType getType() {
		return type;
	}

	public void setType(RestrictionType type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getResolutionComment() {
		return resolutionComment;
	}

	public void setResolutionComment(String resolutionComment) {
		this.resolutionComment = resolutionComment;
	}

	public Boolean getManual() {
		return manual;
	}

	public void setManual(Boolean manual) {
		this.manual = manual;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Boolean getIadmin() {
		return iadmin;
	}

	public void setIadmin(Boolean iadmin) {
		this.iadmin = iadmin;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	public Boolean getIexpense() {
		return iexpense;
	}

	public void setIexpense(Boolean iexpense) {
		this.iexpense = iexpense;
	}

	public Boolean getInvoice() {
		return invoice;
	}

	public void setInvoice(Boolean invoice) {
		this.invoice = invoice;
	}

	public Boolean getMyhr() {
		return myhr;
	}

	public void setMyhr(Boolean myhr) {
		this.myhr = myhr;
	}

	public Boolean getMyreports() {
		return myreports;
	}

	public void setMyreports(Boolean myreports) {
		this.myreports = myreports;
	}

	public Boolean getMytools() {
		return mytools;
	}

	public void setMytools(Boolean mytools) {
		this.mytools = mytools;
	}

	public Boolean getWtr() {
		return wtr;
	}

	public void setWtr(Boolean wtr) {
		this.wtr = wtr;
	}

	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id", nullable = true)
	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<RestrictionFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<RestrictionFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<RestrictionHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<RestrictionHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<RestrictionComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<RestrictionComment> commentList) {
		this.commentList = commentList;
	}

}
