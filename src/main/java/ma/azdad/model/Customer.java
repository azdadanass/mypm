package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import ma.azdad.service.UtilsFunctions;

@Entity

public class Customer extends GenericModel<Integer> {

	private String accountingCode;
	private String photo = "files/no-image.png";
	private String name;
	private Boolean isCustomer;
	private String description;
	private String country;
	private String address;
	private String address2;
	private String pdfAddress;
	private String ri;
	private String vat;
	private String contact;
	private String website;
	private Integer code;
	private String abbreviation;
	private String email;
	private String cnss;
	private String patent;
	private String phone;
	private String fax;
	private String city;
	private String groupName;
	private String structure;
	private Date establishmentDate;
	private String capital;
	private String ice;
	private String comment;
	private Integer countFiles = 0;
	private String oldCategory;

	private CustomerCategory category;
	private CustomerType type;
	private User manager;

	private List<CustomerContact> contactList = new ArrayList<>();
	private List<CustomerCurrency> currencyList = new ArrayList<>();
	private List<CustomerCompany> companyList = new ArrayList<>();
	private List<CustomerIncoterms> incotermsList = new ArrayList<>();
	private List<CustomerAccountingCode> accountingCodeList = new ArrayList<>();
	private List<CustomerServiceCode> serviceCodeList = new ArrayList<>();
	private List<InvoiceTermTemplate> invoiceTermTemplateList = new ArrayList<>();
	private List<CustomerRevenueType> revenueTypeList = new ArrayList<>();
	private List<CustomerFile> fileList = new ArrayList<>();
	private List<CustomerHistory> historyList = new ArrayList<>();
	private List<CustomerComment> commentList = new ArrayList<>();

	private List<CommentGroup<CustomerComment>> commentGroupList;

	// tmp
	private List<String> serviceCodeValuesList;
	private Boolean hasPendingDeductions = false;

	public Customer() {
	}

	// constructor0
	public Customer(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	// constructor1
	public Customer(Integer id, String name, String photo) {
		this.id = id;
		this.name = name;
		this.photo = photo;
	}

	// constructor2
	public Customer(Integer id, String name, String description, CustomerCategory category) {
		super(id);
		this.name = name;
		this.description = description;
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

	@Transient
	public String getDetailedAddress() {
		String result = name + "\n";

		result += Objects.toString(address, "") + "\n";
		result += Objects.toString(address2, "") + "\n";
		result += Objects.toString(city, "") + " " + Objects.toString(country, "") + "\n";
		result += "ICE : " + Objects.toString(ice, "");

		return result;
	}

	@Transient
	public Integer getAccountingCode(Integer companyId) {
		try {
			return accountingCodeList.stream().filter(i -> companyId.equals(i.getCompany().getId())).findFirst().get().getAccountingCode();
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public String getRemainingCustomerDataForPoCreation(Integer companyId, RevenueCategory revenueCategory) {
		List<String> remaining = new ArrayList<>();

		if (getCurrencyValuesList(companyId).size() == 0)
			remaining.add("currency");
		if (getRevenueTypeList(companyId, revenueCategory).size() == 0)
			remaining.add("revenue type");
		if (getServiceCodeValuesList(companyId).size() == 0)
			remaining.add("service code");
		if (getInvoiceTermTemplateList(companyId).size() == 0)
			remaining.add("invoice term template");
		if (getIncotermsValuesList(companyId).size() == 0)
			remaining.add("incoterms");

		if (remaining.isEmpty())
			return null;
		else
			return remaining.stream().collect(Collectors.joining(","));
	}

	@Transient
	public CustomerServiceCode getServiceCode(Integer companyId, String serviceCode) {
		if (serviceCode == null || serviceCode.isEmpty())
			return null;
		return serviceCodeList.stream().filter(i -> i.filter(true, companyId, serviceCode)).findFirst().get();
	}

	@Transient
	public List<InvoiceTermTemplate> getInvoiceTermTemplateList(Integer companyId) {
		return invoiceTermTemplateList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).collect(Collectors.toList());
	}

	@Transient
	public List<Currency> getCurrencyValuesList(Integer companyId) {
		if (companyId == null)
			return null;
		return currencyList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).map(i -> i.getCurrency()).collect(Collectors.toList());
	}

	@Transient
	public List<String> getServiceCodeValuesList(Integer companyId) {
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
	public List<RevenueCategory> getRevenueCategoryList(Integer companyId) {
		return revenueTypeList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId())).map(i -> i.getRevenueCategory()).distinct()
				.collect(Collectors.toList());
	}

	@Transient
	public List<RevenueType> getRevenueTypeList(Integer companyId, RevenueCategory revenueCategory) {
		return revenueTypeList.stream().filter(i -> i.getActive() && companyId.equals(i.getCompany().getId()) && i.getRevenueCategory().equals(revenueCategory))
				.map(i -> i.getRevenueType()).distinct().collect(Collectors.toList());
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

	public void addContact(CustomerContact contact) {
		contact.setCustomer(this);
		contactList.add(contact);
	}

	public void removeContact(CustomerContact contact) {
		contact.setCustomer(null);
		contactList.remove(contact);
	}

	public void addCurrency(CustomerCurrency currency) {
		currency.setCustomer(this);
		currencyList.add(currency);
	}

	public void removeCurrency(CustomerCurrency currency) {
		currency.setCustomer(null);
		currencyList.remove(currency);
	}

	public void addCompany(CustomerCompany company) {
		company.setCustomer(this);
		companyList.add(company);
	}

	public void removeCompany(CustomerCompany company) {
		company.setCustomer(null);
		companyList.remove(company);
	}

	public void addIncoterms(CustomerIncoterms incoterms) {
		incoterms.setCustomer(this);
		incotermsList.add(incoterms);
	}

	public void removeIncoterms(CustomerIncoterms incoterms) {
		incoterms.setCustomer(null);
		incotermsList.remove(incoterms);
	}

	public void addAccountingCode(CustomerAccountingCode accountingCode) {
		accountingCode.setCustomer(this);
		accountingCodeList.add(accountingCode);
	}

	public void removeAccountingCode(CustomerAccountingCode accountingCode) {
		accountingCode.setCustomer(null);
		accountingCodeList.remove(accountingCode);
	}

	public void addServiceCode(CustomerServiceCode serviceCode) {
		serviceCode.setCustomer(this);
		serviceCodeList.add(serviceCode);
	}

	public void removeServiceCode(CustomerServiceCode serviceCode) {
		serviceCode.setCustomer(null);
		serviceCodeList.remove(serviceCode);
	}

	public void addInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setCustomer(this);
		invoiceTermTemplateList.add(invoiceTermTemplate);
	}

	public void addInvoiceTermTemplate(int index, InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setCustomer(this);
		invoiceTermTemplateList.add(index, invoiceTermTemplate);
	}

	public void removeInvoiceTermTemplate(InvoiceTermTemplate invoiceTermTemplate) {
		invoiceTermTemplate.setCustomer(null);
		invoiceTermTemplateList.remove(invoiceTermTemplate);
	}

	public void addRevenueType(CustomerRevenueType revenueType) {
		revenueType.setCustomer(this);
		revenueTypeList.add(revenueType);
	}

	public void removeRevenueType(CustomerRevenueType revenueType) {
		revenueType.setCustomer(null);
		revenueTypeList.remove(revenueType);
	}

	public void addFile(CustomerFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(CustomerFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(CustomerHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(CustomerHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(CustomerComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(CustomerComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<CustomerComment>> map = new HashMap<>();
		for (CustomerComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<CustomerComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<CustomerComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<CustomerComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcustomer")
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
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

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
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
	public CustomerType getType() {
		return type;
	}

	public void setType(CustomerType type) {
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

	@ManyToOne(fetch = FetchType.EAGER)
	public CustomerCategory getCategory() {
		return category;
	}

	public void setCategory(CustomerCategory category) {
		this.category = category;
	}

	@Column(name = "category")
	public String getOldCategory() {
		return oldCategory;
	}

	public void setOldCategory(String oldCategory) {
		this.oldCategory = oldCategory;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerContact> getContactList() {
		return contactList;
	}

	public void setContactList(List<CustomerContact> contactList) {
		this.contactList = contactList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerCurrency> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<CustomerCurrency> currencyList) {
		this.currencyList = currencyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerCompany> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<CustomerCompany> companyList) {
		this.companyList = companyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerIncoterms> getIncotermsList() {
		return incotermsList;
	}

	public void setIncotermsList(List<CustomerIncoterms> incotermsList) {
		this.incotermsList = incotermsList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerAccountingCode> getAccountingCodeList() {
		return accountingCodeList;
	}

	public void setAccountingCodeList(List<CustomerAccountingCode> accountingCodeList) {
		this.accountingCodeList = accountingCodeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerServiceCode> getServiceCodeList() {
		return serviceCodeList;
	}

	public void setServiceCodeList(List<CustomerServiceCode> serviceCodeList) {
		this.serviceCodeList = serviceCodeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<InvoiceTermTemplate> getInvoiceTermTemplateList() {
		return invoiceTermTemplateList;
	}

	public void setInvoiceTermTemplateList(List<InvoiceTermTemplate> invoiceTermTemplateList) {
		this.invoiceTermTemplateList = invoiceTermTemplateList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerRevenueType> getRevenueTypeList() {
		return revenueTypeList;
	}

	public void setRevenueTypeList(List<CustomerRevenueType> revenueTypeList) {
		this.revenueTypeList = revenueTypeList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<CustomerFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<CustomerHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CustomerComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CustomerComment> commentList) {
		this.commentList = commentList;
	}

	@Transient
	public List<String> getServiceCodeValuesList() {
		return serviceCodeValuesList;
	}

	@Transient
	public void setServiceCodeValuesList(List<String> serviceCodeValuesList) {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_idmanager")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@Transient
	public String getManagerUsername() {
		if (manager == null)
			return null;
		return manager.getUsername();
	}

	public void setManagerUsername(String managerUsername) {
		if (manager == null || !managerUsername.equals(managerUsername))
			manager = new User();
		manager.setUsername(managerUsername);
	}

	@Transient
	public String getManagerFullName() {
		if (manager == null)
			return null;
		return manager.getFullName();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPdfAddress() {
		return pdfAddress;
	}

	public void setPdfAddress(String pdfAddress) {
		this.pdfAddress = pdfAddress;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Column(name = "new_type")
	public Boolean getIsCustomer() {
		return isCustomer;
	}

	public void setIsCustomer(Boolean isCustomer) {
		this.isCustomer = isCustomer;
	}

}
