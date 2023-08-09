package ma.azdad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "users")
public class User extends GenericModel<String> {

	private Boolean internal = true;
	private String username;
	private String firstName;
	private String lastName;
	private String fullName;
	private String email;
	private String email2;
	private String status; // old --> active?'Active','Inactive'

	private String photo = "files/user/none.png";
	private String phone;
	private String phone2;
	private String job;
	private String cin;
	private Boolean gender;
	private Boolean contractActive;
	private Date birthday;

	private String login;
	private String password;
	private Boolean active = true;
	private Boolean vpnAccess = false;
	private Boolean accountNonLocked = true;
	private Integer failedAttempt = 0;
	private Date lockTime;
	private Boolean isUsing2FA = true;
	private String secret;

	// external systems
	private Boolean myhr = false;
	private Boolean mytools = false;
	private Boolean iexpense = false;
	private Boolean ilogistics = false;
	private Boolean sdm = false;
	private Boolean ibuy = false;
	private Boolean mypm= false;

	private Integer erId;
	private Integer merId;

	private UserData userData = new UserData(this);
	private BankAccount bankAccount = new BankAccount(this);
	private Affectation affectation = new Affectation(this);
	private CompanyType companyType;

	private Company company;
	private Customer customer;
	private Supplier supplier;
	private String other;
	private Transporter transporter;

	private User user;
	private Date date;

	private Integer countFiles = 0;

	private List<UserFile> fileList = new ArrayList<>();
	private List<UserHistory> historyList = new ArrayList<>();
	private List<UserRole> roleList = new ArrayList<>();

	// tmp
	private Long countUsers = 0l;
	private Long countProjects = 0l;
	private User lineManager;

	public User() {
	}

	public User(String username, String fullName) {
		this.username = username;
		this.fullName = fullName;
	}

	public User(String username, String fullName, String photo, String email, String job) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.photo = photo;
		this.email = email;
		this.job = job;
	}

	public User(String username, String login, Boolean internal, String fullName, String photo, String email, String job, String phone, String cin, Boolean active,
			Boolean contractActive, Integer countFiles, //
			String lobName, CompanyType companyType, String companyName, String customerName, String supplierName, String other, Long countUsers, Long countProjects) {
		super();
		this.username = username;
		this.login = login;
		this.internal = internal;
		this.fullName = fullName;
		this.photo = photo;
		this.email = email;
		this.job = job;
		this.phone = phone;
		this.cin = cin;
		this.active = active;
		this.contractActive = contractActive;
		this.countFiles = countFiles;
		this.countUsers = countUsers;
		this.countProjects = countProjects;
		this.setLobName(lobName);
		this.companyType = companyType;
		this.setCompanyName(companyName);
		this.setCustomerName(customerName);
		this.setSupplierName(supplierName);
		this.other = other;
	}

//	public User(String username, String photo, String fullName, String job, String email, String phone, Boolean active, CompanyType companyType, String company, String customerName, String supplierName) {
//		this.username = username;
//		this.photo = photo;
//		this.fullName = fullName;
//		this.job = job;
//		this.email = email;
//		this.phone = phone;
//		this.active = active;
//		this.companyType = companyType;
//		this.company = customerName != null ? customerName : supplierName != null ? supplierName : company;
//	}

	@Override
	public boolean filter(String query) {
		return contains(query, login, fullName, job, cin, getInternalStr(), getEntityName());
	}

	@Transient
	public String getInternalStr() {
		return internal == null ? null : internal ? "Internal" : "External";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public void unlock() {
		accountNonLocked = true;
		lockTime = null;
		failedAttempt = 0;
	}

	protected Boolean contains(String string, String query) {
		return string != null && string.toLowerCase().contains(query);
	}

	protected Boolean contains(Double d, String query) {
		return d != null && String.valueOf(d).toLowerCase().contains(query);
	}

	protected Boolean contains(Date date, String query) {
		return date != null && UtilsFunctions.getFormattedDate(date).toLowerCase().contains(query);
	}

	@Transient
	public String getEntityName() {
		if (companyType == null)
			return null;
		switch (companyType) {
		case COMPANY:
			return getCompanyName();
		case CUSTOMER:
			return getCustomerName();
		case SUPPLIER:
			return getSupplierName();
		case OTHER:
			return other;
		default:
			return null;
		}
	}

	@Transient
	public String getLobName() {
		return affectation == null ? null : affectation.getLobName();
	}

	@Transient
	public void setLobName(String lobName) {
		if (affectation == null)
			affectation = new Affectation();
		affectation.setLobName(lobName);
	}

	@Transient
	public String getCompanyName() {
		return company == null ? null : company.getName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (company == null)
			company = new Company();
		company.setName(companyName);
	}

	@Transient
	public String getCustomerName() {
		return customer == null ? null : customer.getName();
	}

	@Transient
	public void setCustomerName(String customerName) {
		if (customer == null)
			customer = new Customer();
		customer.setName(customerName);
	}

	@Transient
	public String getSupplierName() {
		return supplier == null ? null : supplier.getName();
	}

	@Transient
	public void setSupplierName(String supplierName) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(supplierName);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !companyId.equals(company.getId()))
			company = new Company();
		company.setId(companyId);
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
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplierId.equals(supplier.getId()))
			supplier = new Supplier();
		supplier.setId(supplierId);
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	@Transient
	public void setCompany(Customer customer, Supplier supplier, String other) {
		switch (companyType) {
		case CUSTOMER:
			this.customer = customer;
			this.supplier = null;
			this.other = null;
			break;
		case SUPPLIER:
			this.customer = null;
			this.supplier = supplier;
			this.other = null;
			break;
		case OTHER:
			this.customer = null;
			this.supplier = null;
			this.other = other;
		}
	}

	public void addRole(Role role) {
		if (!hasRole(role))
			addRole(new UserRole(role));
	}

	public void addRole(UserRole userRole) {
		if (!hasRole(userRole.getRole())) {
			userRole.setUser(this);
			roleList.add(userRole);
		}
	}

	public void removeRole(UserRole userRole) {
		roleList.remove(userRole);
		userRole.setUser(null);
	}

	public void toggleRole(Role role) {
		if (hasRole(role)) {
			roleList.stream().filter(i -> i.getRole().equals(role)).forEach(i -> i.setUser(null));
			roleList.removeIf(i -> i.getRole().equals(role));
			roleList.removeIf(i -> i.getRole().name().contains(role.name()));
		} else {
			if (role.getRole().startsWith("MR_")) {
				roleList.stream().filter(i -> i.getRole().getRole().startsWith("MR_")).forEach(i -> i.setUser(null));
				roleList.removeIf(i -> i.getRole().getRole().startsWith("MR_"));
			}
			Stream.of(Role.values()).filter(r -> role.name().contains(r.name())).forEach(r -> addRole(new UserRole(r)));
		}
	}

	public void toggleVpnAccess() {
		this.vpnAccess = !this.vpnAccess;
	}

	public void addFile(UserFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(UserFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addUserData(UserData userData) {
		this.userData = userData;
		userData.setUser(this);
	}

	public void addHistory(UserHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(UserHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean hasRole(Role role) {
		return roleList.stream().filter(i -> i.getRole().equals(role)).count() > 0;
	}

	@Transient
	public Boolean getIsUser() {
		return hasRole(Role.ROLE_IADMIN);
	}

	@Transient
	public Boolean getIsAdmin() {
		return hasRole(Role.ROLE_IADMIN_ADMIN);
	}
	
	@Transient
	public Boolean getIsMyPm() {
		return hasRole(Role.ROLE_MYPM);
	}
	@Transient
	public Boolean getIsMyPmHr() {
		return hasRole(Role.ROLE_MYPM_HR);
	}
	@Transient
	public Boolean getIsMyPmLineManager() {
		return hasRole(Role.ROLE_MYPM_LINE_MANAGER);
	}
	


	@Column(name = "username", insertable = false, updatable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Id
	@Column(nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "info")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Transient
	public String getName() {
		return fullName;
	}

	@Column(name = "fullname")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "id_photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<UserRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		return fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	@Column(name = "info2")
	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Enumerated(EnumType.STRING)
	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Column(name = "company")
	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Column(name = "firstname")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "lastname")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getContractActive() {
		return contractActive;
	}

	public void setContractActive(Boolean contractActive) {
		this.contractActive = contractActive;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<UserHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<UserFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	public Affectation getAffectation() {
		return affectation;
	}

	public void setAffectation(Affectation affectation) {
		this.affectation = affectation;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

	@Transient
	public User getLineManager() {
		return lineManager;
	}

	@Transient
	public void setLineManager(User lineManager) {
		this.lineManager = lineManager;
	}

	@Transient
	public Long getCountUsers() {
		return countUsers;
	}

	@Transient
	public void setCountUsers(Long countUsers) {
		this.countUsers = countUsers;
	}

	@Transient
	public Long getCountProjects() {
		return countProjects;
	}

	@Transient
	public void setCountProjects(Long countProjects) {
		this.countProjects = countProjects;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public Integer getErId() {
		return erId;
	}

	public void setErId(Integer erId) {
		this.erId = erId;
	}

	public Integer getMerId() {
		return merId;
	}

	public void setMerId(Integer merId) {
		this.merId = merId;
	}

	@Column(unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Integer getFailedAttempt() {
		return failedAttempt;
	}

	public void setFailedAttempt(Integer failedAttempt) {
		this.failedAttempt = failedAttempt;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	@Transient
	public Boolean isEnabled() {
		return this.active;
	}

	@Transient
	public Boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public Boolean getMyhr() {
		return myhr;
	}

	public void setMyhr(Boolean myhr) {
		this.myhr = myhr;
	}

	public Boolean getMytools() {
		return mytools;
	}

	public void setMytools(Boolean mytools) {
		this.mytools = mytools;
	}

	public Boolean getIexpense() {
		return iexpense;
	}

	public void setIexpense(Boolean iexpense) {
		this.iexpense = iexpense;
	}

	public Boolean getIlogistics() {
		return ilogistics;
	}

	public void setIlogistics(Boolean ilogistics) {
		this.ilogistics = ilogistics;
	}

	public Boolean getSdm() {
		return sdm;
	}

	public void setSdm(Boolean sdm) {
		this.sdm = sdm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getVpnAccess() {
		return vpnAccess;
	}

	public void setVpnAccess(Boolean vpnAccess) {
		this.vpnAccess = vpnAccess;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	public Boolean getIsUsing2FA() {
		return isUsing2FA;
	}

	public void setIsUsing2FA(Boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
