package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "bankaccount")
public class BankAccount extends GenericModel<Integer> {

	private String name;
	private String logo;
	private String fullName;
	private Boolean active = true;
	private String oldStatus; // TODO to remove from all projects
	private String description;
	private String address;
	private String branchCode;
	private String phone;
	private String rib;
	private String accountNumber;
	private String type;
	private String swiftCode;
	private String correspondentBankName;
	private String correspondentBankSwiftCode;
	private String fax;
	private String pdfAddress;

	private Double bankChargeVatPercentage;

	private Integer accountingCode;
	private Integer bankChargeAccountingCode;
	private Integer vatAccountingCode;
	private Integer cashAccountingCode;
	private Integer caisseAccountingCode;

	private Integer myhrPaymentIndex = 0;
	private Integer ifinancePaymentIndex = 0;
	private Integer iexpensePaymentIndex = 0;
	private Integer invoicePaymentIndex = 0;

	private Currency currency;
	private Supplier supplier;
	private Customer customer;
	private Company company;
	private User user;

	private Integer countFiles = 0;

	private List<BankAccountFile> fileList = new ArrayList<>();
	private List<BankAccountHistory> historyList = new ArrayList<>();
	private List<BankAccountComment> commentList = new ArrayList<>();

	private List<CommentGroup<BankAccountComment>> commentGroupList;

	public BankAccount() {
	}

	public BankAccount(User user) {
		this.user = user;
	}

	public BankAccount(Integer id, String fullName) {
		super(id);
		this.fullName = fullName;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getCurrencyName() {
		if (currency == null)
			return null;
		return currency.getName();
	}

	@Transient
	public void setCurrencyName(String currencyName) {
		if (currency == null)
			currency = new Currency();
		currency.setName(currencyName);
	}

	@Transient
	public Integer getCurrencyId() {
		if (currency == null)
			return null;
		return currency.getId();
	}

	@Transient
	public void setCurrencyId(Integer currencyId) {
		if (currency == null)
			currency = new Currency();
		currency.setId(currencyId);
	}

	public void generateFullName() {
		if (company != null)
			this.fullName = company.getName() + ", " + name + ", " + currency.getName();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Transient
	public String getCompanyName() {
		if (company == null)
			return null;
		return company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public Integer getCompanyId() {
		if (company == null)
			return null;
		return company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null)
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public String getSupplierName() {
		if (supplier == null)
			return null;
		return supplier.getName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(supplierName);
	}

	@Transient
	public String getCustomerName() {
		if (customer == null)
			return null;
		return customer.getName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (customer == null)
			customer = new Customer();
		customer.setName(customerName);
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

	public void addFile(BankAccountFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(BankAccountFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(BankAccountHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(BankAccountHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(BankAccountComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(BankAccountComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<BankAccountComment>> map = new HashMap<>();
		for (BankAccountComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<BankAccountComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<BankAccountComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<BankAccountComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idbankaccount")
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BankAccountFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<BankAccountFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@Column(name = "status")
	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRib() {
		return rib;
	}

	public void setRib(String rib) {
		this.rib = rib;
	}

	@Column(name = "accountnumber", length = 100)
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "swiftcode", length = 450)
	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	@Column(name = "correspondentbankname", length = 100)
	public String getCorrespondentBankName() {
		return correspondentBankName;
	}

	public void setCorrespondentBankName(String correspondentBankName) {
		this.correspondentBankName = correspondentBankName;
	}

	@Column(name = "correspondentbankswiftcode", length = 100)
	public String getCorrespondentBankSwiftCode() {
		return correspondentBankSwiftCode;
	}

	public void setCorrespondentBankSwiftCode(String correspondentBankSwiftCode) {
		this.correspondentBankSwiftCode = correspondentBankSwiftCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPdfAddress() {
		return pdfAddress;
	}

	public void setPdfAddress(String pdfAddress) {
		this.pdfAddress = pdfAddress;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BankAccountHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<BankAccountHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BankAccountComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<BankAccountComment> commentList) {
		this.commentList = commentList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_idcompany", nullable = true)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Integer getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(Integer accountingCode) {
		this.accountingCode = accountingCode;
	}

	public Integer getBankChargeAccountingCode() {
		return bankChargeAccountingCode;
	}

	public void setBankChargeAccountingCode(Integer bankChargeAccountingCode) {
		this.bankChargeAccountingCode = bankChargeAccountingCode;
	}

	public Double getBankChargeVatPercentage() {
		return bankChargeVatPercentage;
	}

	public void setBankChargeVatPercentage(Double bankChargeVatPercentage) {
		this.bankChargeVatPercentage = bankChargeVatPercentage;
	}

	public Integer getVatAccountingCode() {
		return vatAccountingCode;
	}

	public void setVatAccountingCode(Integer vatAccountingCode) {
		this.vatAccountingCode = vatAccountingCode;
	}

	@OneToOne
	@JoinColumn(name = "resource_idresource")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getMyhrPaymentIndex() {
		return myhrPaymentIndex;
	}

	public void setMyhrPaymentIndex(Integer myhrPaymentIndex) {
		this.myhrPaymentIndex = myhrPaymentIndex;
	}

	public Integer getCashAccountingCode() {
		return cashAccountingCode;
	}

	public void setCashAccountingCode(Integer cashAccountingCode) {
		this.cashAccountingCode = cashAccountingCode;
	}

	public Integer getCaisseAccountingCode() {
		return caisseAccountingCode;
	}

	public void setCaisseAccountingCode(Integer caisseAccountingCode) {
		this.caisseAccountingCode = caisseAccountingCode;
	}

	public Integer getIfinancePaymentIndex() {
		return ifinancePaymentIndex;
	}

	public void setIfinancePaymentIndex(Integer ifinancePaymentIndex) {
		this.ifinancePaymentIndex = ifinancePaymentIndex;
	}

	public Integer getIexpensePaymentIndex() {
		return iexpensePaymentIndex;
	}

	public void setIexpensePaymentIndex(Integer iexpensePaymentIndex) {
		this.iexpensePaymentIndex = iexpensePaymentIndex;
	}

	public Integer getInvoicePaymentIndex() {
		return invoicePaymentIndex;
	}

	public void setInvoicePaymentIndex(Integer invoicePaymentIndex) {
		this.invoicePaymentIndex = invoicePaymentIndex;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
