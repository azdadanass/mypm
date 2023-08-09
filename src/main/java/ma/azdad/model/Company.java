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

public class Company extends GenericModel<Integer> {

	private String name;
	private String shortName;
	private String photo = "files/no-image.png";
	private String country;
	private String city;
	private Integer postalCode;
	private String region;
	private String type;
	private String sector;
	private String ri;
	private String cnss;
	private String vat;
	private String address;
	private String contact;
	private String linkGed;
	private String laborLaw;
	private String phone1;
	private String phone2;
	private String fax;
	private Integer numberFiles;
	private String ice;
	private String description;
	private String patent;
	private String groupName;
	private String structure;
	private Date establishmentDate;
	private String capital;

	private Boolean accounting;
	private AccountingSystemOld accountingSystem;
	private Integer accountNumberOfDigit;
	private Boolean automaticAccounting;
	private Integer fiscalYearStartMonth;
	private Integer fiscalYearStartDay;
	private Integer fiscalYearEndMonth;
	private Integer fiscalYearEndDay;
	private Date systemAccountingStartDate;
	private Currency accountingCurrency;

	private User ceo;
	private User cto;
	private User cfo;
	private User coo;
	private User accountant;
	private User lom;
	private User cashController;
	private User expenseManager;

	private Currency defaultReportingCurrency;

	private Integer countFiles = 0;

//	private List<Bu> buList = new ArrayList<>();
	private List<BankAccount> bankAccountList = new ArrayList<>();
	private List<CompanyFile> fileList = new ArrayList<>();
	private List<CompanyHistory> historyList = new ArrayList<>();
	private List<CompanyComment> commentList = new ArrayList<>();
	private List<CompanyProfile> profileList = new ArrayList<>();

	private List<CommentGroup<CompanyComment>> commentGroupList;

	public Company() {
	}

	public Company(Integer id, String name) {
		super(id);
		this.name = name;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, name, description);
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

	public void addFile(CompanyFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(CompanyFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

//	public void addBu(Bu bu) {
//		bu.setCompany(this);
//		buList.add(bu);
//	}
//
//	public void removeBu(Bu bu) {
//		bu.setCompany(null);
//		buList.remove(bu);
//	}

	public void addBankAccount(BankAccount bankAccount) {
		bankAccount.setCompany(this);
		bankAccountList.add(bankAccount);
	}

	public void removeBankAccount(BankAccount bankAccount) {
		bankAccount.setCompany(null);
		bankAccountList.remove(bankAccount);
	}

	public void addProfile(CompanyProfile profile) {
		profile.setCompany(this);
		profileList.add(profile);
	}

	public void removeProfile(CompanyProfile profile) {
		profile.setCompany(null);
		profileList.remove(profile);
	}

	public void addHistory(CompanyHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(CompanyHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addComment(CompanyComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(CompanyComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<CompanyComment>> map = new HashMap<>();
		for (CompanyComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<CompanyComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<CommentGroup<CompanyComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<CompanyComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcompany", unique = true, nullable = false)
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

	@Column(columnDefinition = "TEXT")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "logo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CompanyFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<CompanyFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//	public List<Bu> getBuList() {
//		return buList;
//	}
//
//	public void setBuList(List<Bu> buList) {
//		this.buList = buList;
//	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BankAccount> getBankAccountList() {
		return bankAccountList;
	}

	public void setBankAccountList(List<BankAccount> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CompanyHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<CompanyHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CompanyComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CompanyComment> commentList) {
		this.commentList = commentList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CompanyProfile> getProfileList() {
		return profileList;
	}

	public void setProfileList(List<CompanyProfile> profileList) {
		this.profileList = profileList;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getRi() {
		return ri;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public String getCnss() {
		return cnss;
	}

	public void setCnss(String cnss) {
		this.cnss = cnss;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLinkGed() {
		return linkGed;
	}

	public void setLinkGed(String linkGed) {
		this.linkGed = linkGed;
	}

	public String getLaborLaw() {
		return laborLaw;
	}

	public void setLaborLaw(String laborLaw) {
		this.laborLaw = laborLaw;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getNumberFiles() {
		return numberFiles;
	}

	public void setNumberFiles(Integer numberFiles) {
		this.numberFiles = numberFiles;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	@Temporal(TemporalType.DATE)
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

	public Boolean getAccounting() {
		return accounting;
	}

	public void setAccounting(Boolean accounting) {
		this.accounting = accounting;
	}

	@Enumerated(EnumType.STRING)
	public AccountingSystemOld getAccountingSystem() {
		return accountingSystem;
	}

	public void setAccountingSystem(AccountingSystemOld accountingSystem) {
		this.accountingSystem = accountingSystem;
	}

	public Integer getAccountNumberOfDigit() {
		return accountNumberOfDigit;
	}

	public void setAccountNumberOfDigit(Integer accountNumberOfDigit) {
		this.accountNumberOfDigit = accountNumberOfDigit;
	}

	public Boolean getAutomaticAccounting() {
		return automaticAccounting;
	}

	public void setAutomaticAccounting(Boolean automaticAccounting) {
		this.automaticAccounting = automaticAccounting;
	}

	public Integer getFiscalYearStartMonth() {
		return fiscalYearStartMonth;
	}

	public void setFiscalYearStartMonth(Integer fiscalYearStartMonth) {
		this.fiscalYearStartMonth = fiscalYearStartMonth;
	}

	public Integer getFiscalYearStartDay() {
		return fiscalYearStartDay;
	}

	public void setFiscalYearStartDay(Integer fiscalYearStartDay) {
		this.fiscalYearStartDay = fiscalYearStartDay;
	}

	public Integer getFiscalYearEndMonth() {
		return fiscalYearEndMonth;
	}

	public void setFiscalYearEndMonth(Integer fiscalYearEndMonth) {
		this.fiscalYearEndMonth = fiscalYearEndMonth;
	}

	public Integer getFiscalYearEndDay() {
		return fiscalYearEndDay;
	}

	public void setFiscalYearEndDay(Integer fiscalYearEndDay) {
		this.fiscalYearEndDay = fiscalYearEndDay;
	}

	@Temporal(TemporalType.DATE)
	public Date getSystemAccountingStartDate() {
		return systemAccountingStartDate;
	}

	public void setSystemAccountingStartDate(Date systemAccountingStartDate) {
		this.systemAccountingStartDate = systemAccountingStartDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Currency getAccountingCurrency() {
		return accountingCurrency;
	}

	public void setAccountingCurrency(Currency accountingCurrency) {
		this.accountingCurrency = accountingCurrency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ceo_id", nullable = true)
	public User getCeo() {
		return ceo;
	}

	public void setCeo(User ceo) {
		this.ceo = ceo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cto_id", nullable = true)
	public User getCto() {
		return cto;
	}

	public void setCto(User cto) {
		this.cto = cto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cfo_id", nullable = true)
	public User getCfo() {
		return cfo;
	}

	public void setCfo(User cfo) {
		this.cfo = cfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coo_id", nullable = true)
	public User getCoo() {
		return coo;
	}

	public void setCoo(User coo) {
		this.coo = coo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountant_id", nullable = true)
	public User getAccountant() {
		return accountant;
	}

	public void setAccountant(User accountant) {
		this.accountant = accountant;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logisticmanager_id", nullable = true)
	public User getLom() {
		return lom;
	}

	public void setLom(User lom) {
		this.lom = lom;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getCashController() {
		return cashController;
	}

	public void setCashController(User cashController) {
		this.cashController = cashController;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getExpenseManager() {
		return expenseManager;
	}

	public void setExpenseManager(User expenseManager) {
		this.expenseManager = expenseManager;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "default_reporting_currency__id", nullable = false)
	public Currency getDefaultReportingCurrency() {
		return defaultReportingCurrency;
	}

	public void setDefaultReportingCurrency(Currency defaultReportingCurrency) {
		this.defaultReportingCurrency = defaultReportingCurrency;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

}
