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
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity

public class Project extends GenericModel<Integer> {

	private String name;
	private Date startDate;
	private Date endDate;
	private String duration;
	private String status;
	private String type;
	private String category;
	private String subType;
	private ProjectCustomerType customerType;

	private Double revenue = 0.0;
	private Double cogs = 0.0; // Cost of goods sold
	private Double risk = 0.0;
	private Double grossMargin = 0.0; // revenue - cogs (1+risk)

	// Sales Cost
	private Double directSalesCost = 0.0;
	private Double globalSalesCost = 0.0;
	private Double presalesCost = 0.0;
	private Double marketingCost = 0.0;
	private Double totalSalesCost = 0.0;

	// Support Cost
	private Double adminCost = 0.0;
	private Double hrCost = 0.0;
	private Double financeCost = 0.0;
	private Double itCost = 0.0;
	private Double totalSupportCost = 0.0;

	private Double tpsr = 0.0;

	private Double ebida = 0.0; // Earnings Before Interest, Depreciation and
								// Amortization grossMargin - SUM(otherCosts)

	private Double financingCost = 0.0;
	private Double tax = 0.0;
	private Double amortization = 0.0;
	private Double depreciation = 0.0;

	private Double netMargin = 0.0; // netMargin * (1+tax)

	private Boolean companyWarehousing = true;
	private Boolean companyStockManagement = false;

	private Boolean supplierWarehousing = false;
	private Boolean supplierStockManagement = false;

	private Boolean customerWarehousing = false;
	private Boolean customerStockManagement = false;

	private Boolean sdm = false;

	private Currency currency;
	private Costcenter costcenter;
	private User manager;
	private Customer customer;
	private RequestForQuote requestForQuote;
	private Contract contract;

	private Integer countFiles = 0;

	private List<Task> taskList = new ArrayList<>();
	private List<ProjectMilestone> milestoneList = new ArrayList<>();
	private List<ProjectManager> managerList = new ArrayList<>();
	private List<ProjectFile> fileList = new ArrayList<>();
	private List<ProjectHistory> historyList = new ArrayList<>();
	private List<ProjectComment> commentList = new ArrayList<>();

	private List<CommentGroup<ProjectComment>> commentGroupList;

	public Project() {
	}

	public Project(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Project(Integer id, String name, String type, String managerFullName) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.setManagerFullName(managerFullName);
	}

	public Project(Integer id, String name, Date startDate, Date endDate, String status, String type, String category, String subType, Integer countFiles, String lobName, Integer costcenterYear, String companyName, String currencyName, String managerFullName) {
		super(id);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.type = type;
		this.category = category;
		this.subType = subType;
		this.countFiles = countFiles;
		this.setLobName(lobName);
		this.setCostcenterYear(costcenterYear);
		this.setCompanyName(companyName);
		this.setCurrencyName(currencyName);
		this.setManagerFullName(managerFullName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, name, status, category, type, subType, getCompanyName(),getManagerFullName());
	}

	@Transient
	public Boolean getIsProject() {
		return ProjectCategory.PROJECT.getValue().equals(category);
	}

	@Transient
	public Boolean getIsCostCenter() {
		return ProjectCategory.COSTCENTER.getValue().equals(category);
	}
	
	
	@Transient
	public Boolean getIsCustomerTypeCustomer() {
		return ProjectCustomerType.CUSTOMER.equals(customerType);
	}
	
	@Transient
	public Boolean getIsCustomerTypeCompany() {
		return ProjectCustomerType.COMPANY.equals(customerType);
	}

	@Transient
	public void calculateDuration() {
		if (startDate != null && endDate != null && startDate.compareTo(endDate) <= 0)
			duration = String.valueOf(UtilsFunctions.getDateDifferneceInMonths(startDate, endDate)) + " Months";
	}

	@Transient
	public String getLobName() {
		return costcenter == null ? null : costcenter.getLobName();
	}

	@Transient
	public void setLobName(String lobName) {
		if (costcenter == null)
			costcenter = new Costcenter();
		costcenter.setLobName(lobName);
	}

	@Transient
	public Integer getCostcenterId() {
		return costcenter == null ? null : costcenter.getId();
	}

	@Transient
	public void setCostcenterId(Integer costcenterId) {
		if (costcenter == null)
			costcenter = new Costcenter();
		costcenter.setId(costcenterId);
	}

	@Transient
	public Integer getRequestForQuoteId() {
		return requestForQuote == null ? null : requestForQuote.getId();
	}

	@Transient
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customerId.equals(customer.getId()))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Integer getContractId() {
		return contract == null ? null : contract.getIdcontract();
	}

	@Transient
	public void setContractId(Integer contractId) {
		if (contract == null || (contractId != null && !contractId.equals(contract.getIdcontract())))
			contract = new Contract();
		contract.setIdcontract(contractId);
	}

	@Transient
	public Integer getCostcenterYear() {
		return costcenter == null ? null : costcenter.getYear();
	}

	@Transient
	public void setCostcenterYear(Integer costcenterYear) {
		if (costcenter == null)
			costcenter = new Costcenter();
		costcenter.setYear(costcenterYear);
	}

	@Transient
	public String getCompanyName() {
		return costcenter == null ? null : costcenter.getCompanyName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (costcenter == null)
			costcenter = new Costcenter();
		costcenter.setCompanyName(companyName);
	}

	@Transient
	public String getCurrencyName() {
		return currency == null ? null : currency.getName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setName(currencyName);
	}

	@Transient
	public String getManagerUsername() {
		return manager == null ? null : manager.getUsername();
	}

	@Transient
	public void setManagerUsername(String managerUsername) {
		if (manager == null || !managerUsername.contentEquals(manager.getUsername()))
			manager = new User();
		manager.setUsername(managerUsername);
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

	public void addFile(ProjectFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(ProjectFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addTask(Task task) {
		task.setProject(this);
		taskList.add(task);
	}

	public void removeTask(Task task) {
		task.setProject(null);
		taskList.remove(task);
	}

	public void addMilestone(ProjectMilestone milestone) {
		milestone.setProject(this);
		milestoneList.add(milestone);
		updateMilestoneListNumeros();
	}

	public void removeMilestone(ProjectMilestone milestone) {
		milestone.setProject(null);
		milestoneList.remove(milestone);
		updateMilestoneListNumeros();
	}

	public void updateMilestoneListNumeros() {
		int numero = 1;
		for (ProjectMilestone pm : milestoneList)
			pm.setNumero(numero++);
	}

	public void addManager(ProjectManager manager) {
		manager.setProject(this);
		managerList.add(manager);
	}

	public void removeManager(ProjectManager manager) {
		manager.setProject(null);
		managerList.remove(manager);
	}

	public void addHistory(ProjectHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(ProjectHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(ProjectComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(ProjectComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<ProjectComment>> map = new HashMap<>();
		for (ProjectComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<ProjectComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<ProjectComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<ProjectComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@Transient
	public String getManagerFullName() {
		return manager == null ? null : manager.getFullName();
	}

	@Transient
	public void setManagerFullName(String managerFullName) {
		if (manager == null)
			manager = new User();
		manager.setFullName(managerFullName);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idproject")
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ProjectFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectManager> getManagerList() {
		return managerList;
	}

	public void setManagerList(List<ProjectManager> managerList) {
		this.managerList = managerList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ProjectHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ProjectComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<ProjectComment> commentList) {
		this.commentList = commentList;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "project_type", length = 45)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "type", length = 45)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	@Column(name = "plannedrevenue", precision = 22, scale = 0)
	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	@Column(name = "baselinecost", precision = 22, scale = 0)
	public Double getCogs() {
		return cogs;
	}

	public void setCogs(Double cogs) {
		this.cogs = cogs;
	}

	public Double getRisk() {
		return risk;
	}

	public void setRisk(Double risk) {
		this.risk = risk;
	}

	@Column(name = "plannedmargin", precision = 22, scale = 0)
	public Double getGrossMargin() {
		return grossMargin;
	}

	public void setGrossMargin(Double grossMargin) {
		this.grossMargin = grossMargin;
	}

	public Double getDirectSalesCost() {
		return directSalesCost;
	}

	public void setDirectSalesCost(Double directSalesCost) {
		this.directSalesCost = directSalesCost;
	}

	public Double getGlobalSalesCost() {
		return globalSalesCost;
	}

	public void setGlobalSalesCost(Double globalSalesCost) {
		this.globalSalesCost = globalSalesCost;
	}

	public Double getPresalesCost() {
		return presalesCost;
	}

	public void setPresalesCost(Double presalesCost) {
		this.presalesCost = presalesCost;
	}

	public Double getMarketingCost() {
		return marketingCost;
	}

	public void setMarketingCost(Double marketingCost) {
		this.marketingCost = marketingCost;
	}

	public Double getTotalSalesCost() {
		return totalSalesCost;
	}

	public void setTotalSalesCost(Double totalSalesCost) {
		this.totalSalesCost = totalSalesCost;
	}

	public Double getAdminCost() {
		return adminCost;
	}

	public void setAdminCost(Double adminCost) {
		this.adminCost = adminCost;
	}

	public Double getHrCost() {
		return hrCost;
	}

	public void setHrCost(Double hrCost) {
		this.hrCost = hrCost;
	}

	public Double getFinanceCost() {
		return financeCost;
	}

	public void setFinanceCost(Double financeCost) {
		this.financeCost = financeCost;
	}

	public Double getItCost() {
		return itCost;
	}

	public void setItCost(Double itCost) {
		this.itCost = itCost;
	}

	public Double getTotalSupportCost() {
		return totalSupportCost;
	}

	public void setTotalSupportCost(Double totalSupportCost) {
		this.totalSupportCost = totalSupportCost;
	}

	public Double getTpsr() {
		return tpsr;
	}

	public void setTpsr(Double tpsr) {
		this.tpsr = tpsr;
	}

	public Double getEbida() {
		return ebida;
	}

	public void setEbida(Double ebida) {
		this.ebida = ebida;
	}

	public Double getFinancingCost() {
		return financingCost;
	}

	public void setFinancingCost(Double financingCost) {
		this.financingCost = financingCost;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getAmortization() {
		return amortization;
	}

	public void setAmortization(Double amortization) {
		this.amortization = amortization;
	}

	public Double getDepreciation() {
		return depreciation;
	}

	public void setDepreciation(Double depreciation) {
		this.depreciation = depreciation;
	}

	public Double getNetMargin() {
		return netMargin;
	}

	public void setNetMargin(Double netMargin) {
		this.netMargin = netMargin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Costcenter getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(Costcenter costcenter) {
		this.costcenter = costcenter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_idmanager", nullable = false)
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public RequestForQuote getRequestForQuote() {
		return requestForQuote;
	}

	public void setRequestForQuote(RequestForQuote requestForQuote) {
		this.requestForQuote = requestForQuote;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("numero ASC")
	public List<ProjectMilestone> getMilestoneList() {
		return milestoneList;
	}

	public void setMilestoneList(List<ProjectMilestone> milestoneList) {
		this.milestoneList = milestoneList;
	}

	public Boolean getCustomerWarehousing() {
		return customerWarehousing;
	}

	public void setCustomerWarehousing(Boolean customerWarehousing) {
		this.customerWarehousing = customerWarehousing;
	}

	public Boolean getCustomerStockManagement() {
		return customerStockManagement;
	}

	public void setCustomerStockManagement(Boolean customerStockManagement) {
		this.customerStockManagement = customerStockManagement;
	}

	public Boolean getSdm() {
		return sdm;
	}

	public void setSdm(Boolean sdm) {
		this.sdm = sdm;
	}

	@Enumerated(EnumType.STRING)
	public ProjectCustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(ProjectCustomerType customerType) {
		this.customerType = customerType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Boolean getCompanyWarehousing() {
		return companyWarehousing;
	}

	public void setCompanyWarehousing(Boolean companyWarehousing) {
		this.companyWarehousing = companyWarehousing;
	}

	public Boolean getSupplierWarehousing() {
		return supplierWarehousing;
	}

	public void setSupplierWarehousing(Boolean supplierWarehousing) {
		this.supplierWarehousing = supplierWarehousing;
	}

	public Boolean getCompanyStockManagement() {
		return companyStockManagement;
	}

	public void setCompanyStockManagement(Boolean companyStockManagement) {
		this.companyStockManagement = companyStockManagement;
	}

	public Boolean getSupplierStockManagement() {
		return supplierStockManagement;
	}

	public void setSupplierStockManagement(Boolean supplierStockManagement) {
		this.supplierStockManagement = supplierStockManagement;
	}

}
