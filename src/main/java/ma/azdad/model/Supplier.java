package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
public class Supplier extends GenericModel<Integer> {

	private String name;
	private String code;
	private String photo = "files/no-image.png";
	private String accountingCode;
	private String description;
	private String email;
	private String password;
	private String contact;
	private String ri;
	private String vat;
	private String cnss;
	private String ice;
	private String patent;
	private String website;
	private String phone;
	private String fax;
	private String city;
	private String country = "Morocco";
	private String address1;
	private String address2;
	private String address3;
	private SupplierStatus status = SupplierStatus.EDITED;
	private Boolean active = true;
	private String groupName;
	private SupplierType type;
	private String structure;
	private Date establishmentDate;
	private String capital;
	private Integer countFiles = 0;
	private String comment;
	private String oldCategory;

	private Boolean ibuyAccess = false;
	private Boolean invoiceManagement = false;
	private Boolean receiptNotifications = false;
	private Boolean canViewComments = false;

	private Integer lastPaymentIndex = 0;

	private Date date0; // created buyer
	private Date date1; // approved cfo
	private Date date2; // rejected
	private User user0;
	private User user1;
	private User user2;

	private SupplierCategory category;

	private List<SupplierContact> contactList = new ArrayList<>();
	private List<SupplierCurrency> currencyList = new ArrayList<>();
	private List<SupplierCompany> companyList = new ArrayList<>();
	private List<SupplierIncoterms> incotermsList = new ArrayList<>();
	private List<SupplierAccountingCode> accountingCodeList = new ArrayList<>();
	private List<SupplierServiceCode> serviceCodeList = new ArrayList<>();
	private List<InvoiceTermTemplate> invoiceTermTemplateList = new ArrayList<>();
	private List<SupplierCostType> costTypeList = new ArrayList<>();
	private List<SupplierFile> fileList = new ArrayList<>();
	private List<SupplierHistory> historyList = new ArrayList<>();
	private List<SupplierComment> commentList = new ArrayList<>();

	private List<CommentGroup<SupplierComment>> commentGroupList;

	// tmp
	private String tmpPassword;
	private List<Currency> currencyValuesList;
	private List<Integer> serviceCodeValuesList;
	private Boolean hasPendingDeductions = false;

	public Supplier() {
	}

	// constructor1
	public Supplier(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Supplier(Integer id, String name, String photo, Boolean active) {
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.active = active;
	}

	// constructor2
	public Supplier(Integer id, String name, String description, SupplierStatus status, Boolean active, SupplierCategory category) {
		super(id);
		this.name = name;
		this.description = description;
		this.status = status;
		this.active = active;
		this.category = category;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);

		return result;
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

//	public void generateInvoiceTermTemplateReference(Integer referenceId) {
//		String reference = invoiceTermTemplateList.stream().filter(i -> i.getReferenceId().equals(referenceId)).map(i -> i.getPhase() + "[" + i.getPercentage() + "%]")
//				.collect(Collectors.joining(","));
//		invoiceTermTemplateTemplateList.stream().filter(i -> i.getReferenceId().equals(referenceId)).forEach(i -> i.setReference(reference));
//	}

	@Transient
	public SupplierServiceCode getServiceCode(Integer companyId, Integer serviceCode) {
		if (serviceCode == null)
			return null;
		return serviceCodeList.stream().filter(i -> i.filter(true, companyId, serviceCode)).findFirst().get();
	}

	@Transient
	public List<InvoiceTermTemplate> getInvoiceTermTemplateList(Integer companyId) {
		return invoiceTermTemplateList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).collect(Collectors.toList());
	}

	@Transient
	public List<Currency> getCurrencyValuesList(Integer companyId) {
		if (currencyValuesList == null)
			currencyValuesList = currencyList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).map(i -> i.getCurrency()).collect(Collectors.toList());
		return currencyValuesList;
	}

	@Transient
	public List<Integer> getServiceCodeValuesList(Integer companyId) {
		if (serviceCodeValuesList == null)
			serviceCodeValuesList = serviceCodeList.stream().filter(i -> i.filter(true, companyId)).map(i -> i.getServiceCode()).collect(Collectors.toList());
		return serviceCodeValuesList;
	}

	@Transient
	public List<String> getIncotermsValuesList(Integer companyId) {
		return incotermsList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).map(i -> i.getIncoterms()).collect(Collectors.toList());
	}

	@Transient
	public List<InvoiceTerm> getOldInvoiceTermList() {
		List<InvoiceTerm> result = new ArrayList<>();
		invoiceTermTemplateList.forEach(i -> result.addAll(i.getDetailList()));
		return result;
	}

	@Transient
	public List<CostCategory> getCostCategoryList(Integer companyId) {
		return costTypeList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).map(i -> i.getCostCategory()).distinct().collect(Collectors.toList());
	}

	@Transient
	public List<CostType> getCostTypeList(Integer companyId, CostCategory costCategory) {
		return costTypeList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId()) && i.getCostCategory().equals(costCategory)).map(i -> i.getCostType()).distinct().collect(Collectors.toList());
	}

	@Transient
	public List<Currency> getCurrencyList2() {
		return currencyList.stream().map(i -> i.getCurrency()).distinct().collect(Collectors.toList());
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	public void addContact(SupplierContact contact) {
		contact.setSupplier(this);
		contactList.add(contact);
	}

	public void removeContact(SupplierContact contact) {
		contact.setSupplier(null);
		contactList.remove(contact);
	}

	public void addCurrency(SupplierCurrency currency) {
		currency.setSupplier(this);
		currencyList.add(currency);
	}

	public void removeCurrency(SupplierCurrency currency) {
		currency.setSupplier(null);
		currencyList.remove(currency);
	}

	public void addCompany(SupplierCompany company) {
		company.setSupplier(this);
		companyList.add(company);
	}

	public void removeCompany(SupplierCompany company) {
		company.setSupplier(null);
		companyList.remove(company);
	}

	public void addIncoterms(SupplierIncoterms incoterms) {
		incoterms.setSupplier(this);
		incotermsList.add(incoterms);
	}

	public void removeIncoterms(SupplierIncoterms incoterms) {
		incoterms.setSupplier(null);
		incotermsList.remove(incoterms);
	}

	public void addAccountingCode(SupplierAccountingCode accountingCode) {
		accountingCode.setSupplier(this);
		accountingCodeList.add(accountingCode);
	}

	public void removeAccountingCode(SupplierAccountingCode accountingCode) {
		accountingCode.setSupplier(null);
		accountingCodeList.remove(accountingCode);
	}

	public void addServiceCode(SupplierServiceCode serviceCode) {
		serviceCode.setSupplier(this);
		serviceCodeList.add(serviceCode);
	}

	public void removeServiceCode(SupplierServiceCode serviceCode) {
		serviceCode.setSupplier(null);
		serviceCodeList.remove(serviceCode);
	}

	public void addInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setSupplier(this);
		invoiceTermTemplateList.add(invoiceTermTemplate);
	}

	public void addInvoiceTermTemplate(int index, InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setSupplier(this);
		invoiceTermTemplateList.add(index, invoiceTermTemplate);
	}

	public void removeInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setSupplier(null);
		invoiceTermTemplateList.remove(invoiceTermTemplate);
	}

	public void addCostType(SupplierCostType costType) {
		costType.setSupplier(this);
		costTypeList.add(costType);
	}

	public void removeCostType(SupplierCostType costType) {
		costType.setSupplier(null);
		costTypeList.remove(costType);
	}

	public void addFile(SupplierFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(SupplierFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(SupplierHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(SupplierHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(SupplierComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(SupplierComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<SupplierComment>> map = new HashMap<>();
		for (SupplierComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<SupplierComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<SupplierComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<SupplierComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idsupplier")
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRi() {
		return ri;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getCnss() {
		return cnss;
	}

	public void setCnss(String cnss) {
		this.cnss = cnss;
	}

	public String getIce() {
		return ice;
	}

	public void setIce(String ice) {
		this.ice = ice;
	}

	public String getPatent() {
		return patent;
	}

	public void setPatent(String patent) {
		this.patent = patent;
	}

	@Column(name = "linkged", length = 500)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status_new")
	public SupplierStatus getStatus() {
		return status;
	}

	public void setStatus(SupplierStatus status) {
		this.status = status;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "groupname", length = 100)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "type_new")
	public SupplierType getType() {
		return type;
	}

	public void setType(SupplierType type) {
		this.type = type;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "establishmentdate")
	public Date getEstablishmentDate() {
		return establishmentDate;
	}

	public void setEstablishmentDate(Date establishmentDate) {
		this.establishmentDate = establishmentDate;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getIbuyAccess() {
		return ibuyAccess;
	}

	public void setIbuyAccess(Boolean ibuyAccess) {
		this.ibuyAccess = ibuyAccess;
	}

	public Boolean getInvoiceManagement() {
		return invoiceManagement;
	}

	public void setInvoiceManagement(Boolean invoiceManagement) {
		this.invoiceManagement = invoiceManagement;
	}

	public Boolean getReceiptNotifications() {
		return receiptNotifications;
	}

	public void setReceiptNotifications(Boolean receiptNotifications) {
		this.receiptNotifications = receiptNotifications;
	}

	public Boolean getCanViewComments() {
		return canViewComments;
	}

	public void setCanViewComments(Boolean canViewComments) {
		this.canViewComments = canViewComments;
	}

	@Column(name = "last_parent_payment_index")
	public Integer getLastPaymentIndex() {
		return lastPaymentIndex;
	}

	public void setLastPaymentIndex(Integer lastPaymentIndex) {
		this.lastPaymentIndex = lastPaymentIndex;
	}

	public Date getDate0() {
		return date0;
	}

	public void setDate0(Date date0) {
		this.date0 = date0;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser0() {
		return user0;
	}

	public void setUser0(User user0) {
		this.user0 = user0;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public SupplierCategory getCategory() {
		return category;
	}

	public void setCategory(SupplierCategory category) {
		this.category = category;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierContact> getContactList() {
		return contactList;
	}

	public void setContactList(List<SupplierContact> contactList) {
		this.contactList = contactList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierCurrency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<SupplierCurrency> currencyList) {
		this.currencyList = currencyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierCompany> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<SupplierCompany> companyList) {
		this.companyList = companyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierIncoterms> getIncotermsList() {
		return incotermsList;
	}

	public void setIncotermsList(List<SupplierIncoterms> incotermsList) {
		this.incotermsList = incotermsList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierAccountingCode> getAccountingCodeList() {
		return accountingCodeList;
	}

	public void setAccountingCodeList(List<SupplierAccountingCode> accountingCodeList) {
		this.accountingCodeList = accountingCodeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierServiceCode> getServiceCodeList() {
		return serviceCodeList;
	}

	public void setServiceCodeList(List<SupplierServiceCode> serviceCodeList) {
		this.serviceCodeList = serviceCodeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<InvoiceTermTemplate> getInvoiceTermTemplateList() {
		return invoiceTermTemplateList;
	}

	public void setInvoiceTermTemplateList(List<InvoiceTermTemplate> invoiceTermTemplateList) {
		this.invoiceTermTemplateList = invoiceTermTemplateList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierCostType> getCostTypeList() {
		return costTypeList;
	}

	public void setCostTypeList(List<SupplierCostType> costTypeList) {
		this.costTypeList = costTypeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<SupplierFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<SupplierHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<SupplierComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<SupplierComment> commentList) {
		this.commentList = commentList;
	}

	@Transient
	public String getTmpPassword() {
		return tmpPassword;
	}

	@Transient
	public void setTmpPassword(String tmpPassword) {
		this.tmpPassword = tmpPassword;
	}

	@Transient
	public List<Integer> getServiceCodeValuesList() {
		return serviceCodeValuesList;
	}

	@Transient
	public void setServiceCodeValuesList(List<Integer> serviceCodeValuesList) {
		this.serviceCodeValuesList = serviceCodeValuesList;
	}

	@Transient
	public Boolean getHasPendingDeductions() {
		return hasPendingDeductions;
	}

	@Transient
	public void setHasPendingDeductions(Boolean hasPendingDeductions) {
		this.hasPendingDeductions = hasPendingDeductions;
	}

	@Column(name = "category")
	public String getOldCategory() {
		return oldCategory;
	}

	public void setOldCategory(String oldCategory) {
		this.oldCategory = oldCategory;
	}

}
