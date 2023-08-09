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
public class ProjectCatalog extends GenericModel<Integer> {

	private String name;
	private Boolean active = true;
	private String description;
	private Integer countFiles = 0;
	private Date startDate;
	private Date endDate;

	private Project project;
	private Currency currency;
	private Customer endCustomer;

	private List<ProjectCatalogDetail> detailList = new ArrayList<>();
	private List<ProjectCatalogFile> fileList = new ArrayList<>();
	private List<ProjectCatalogHistory> historyList = new ArrayList<>();
	private List<ProjectCatalogComment> commentList = new ArrayList<>();

	private List<CommentGroup<ProjectCatalogComment>> commentGroupList;

	public ProjectCatalog() {
		super();
	}

	public ProjectCatalog(Integer id, String name, Boolean active, String description, Date startDate, Date endDate, Integer countFiles, String projectName, String currencyName) {
		super(id);
		this.name = name;
		this.active = active;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.countFiles = countFiles;
		this.setProjectName(projectName);
		this.setCurrencyName(currencyName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, name, description, getProjectName(), getCurrencyName());
	}

	@Transient
	public String getProjectName() {
		if (project != null)
			return project.getName();
		return null;
	}

	@Transient
	public void setProjectName(String name) {
		if (project == null)
			project = new Project();
		project.setName(name);
	}

	@Transient
	public String getCurrencyName() {
		if (currency != null)
			return currency.getName();
		return null;
	}

	@Transient
	public void setCurrencyName(String name) {
		if (currency == null)
			currency = new Currency();
		currency.setName(name);
	}

	@Transient
	public Integer getCurrencyId() {
		if (currency == null)
			return null;
		return currency.getId();
	}

	@Transient
	public void setCurrencyId(Integer currencyId) {
		if (currency == null || !currencyId.equals(currency.getId()))
			currency = new Currency();
		currency.setId(currencyId);
	}

	@Transient
	public Integer getProjectId() {
		if (project == null)
			return null;
		return project.getId();
	}

	@Transient
	public void setProjectId(Integer projectId) {
		if (project == null || !projectId.equals(project.getId()))
			project = new Project();
		project.setId(projectId);
	}

	@Transient
	public Integer getEndCustomerId() {
		if (endCustomer == null)
			return null;
		return endCustomer.getId();
	}

	@Transient
	public void setEndCustomerId(Integer endCustomerId) {
		if (endCustomer == null || !endCustomerId.equals(endCustomer.getId()))
			endCustomer = new Customer();
		endCustomer.setId(endCustomerId);
	}

	@Transient
	public String getEndCustomerName() {
		if (endCustomer != null)
			return endCustomer.getName();
		return null;
	}

	@Transient
	public void setEndCustomerName(String name) {
		if (endCustomer == null)
			endCustomer = new Customer();
		endCustomer.setName(name);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(ProjectCatalogFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(ProjectCatalogFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addDetail(ProjectCatalogDetail detail) {
		detail.setProjectCatalog(this);
		detail.setReference(detailList.stream().mapToInt(i -> i.getReference()).max().orElse(0) + 1);
		detailList.add(detail);
	}

	public void removeDetail(ProjectCatalogDetail detail) {
		detail.setProjectCatalog(null);
		detailList.remove(detail);
	}

	public void addHistory(ProjectCatalogHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(ProjectCatalogHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(ProjectCatalogComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(ProjectCatalogComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<ProjectCatalogComment>> map = new HashMap<>();
		for (ProjectCatalogComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<ProjectCatalogComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<ProjectCatalogComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<ProjectCatalogComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectCatalogFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ProjectCatalogFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectCatalog", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectCatalogDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ProjectCatalogDetail> detailList) {
		this.detailList = detailList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectCatalogHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ProjectCatalogHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectCatalogComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<ProjectCatalogComment> commentList) {
		this.commentList = commentList;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(Customer endCustomer) {
		this.endCustomer = endCustomer;
	}

}
