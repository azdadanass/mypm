package ma.azdad.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "e_general_data")
public class UserData extends GenericModel<Integer> {

	private Boolean internal;
	private String firstName;
	private String lastName;
	private String fullName;
	private String photo;
	private String phone2;
	private String phone;
	private String email;
	private String email2;
	private Date birthday;
	private String gender = "Male";
	private String job;
	private String description;
	private String status;

	private String cin;
	private Date cinExpireDate;

	private String cnss;
	private String martialStatus;
	private String cimr;
	private String country;
	private String region;
	private String iesourcing;
	private String grade;
	private String ladder;
	private String tercel;
	private String matricule;
	private String matriculeInssurance;
	private String matriculeAMC;
	private String officeStatus;
	private Date lastModified;
	private String aboutMe;
	private Double lastGrossSalary = 0.0;

	private String passportId;
	private Date passportExpireDate;

	private String driveLicenceId;
	private String driveLicenceType;
	private Date driveLicenceIssuedDate;
	private Date driveLicenceExpireDate;

	private String bankName;
	private String bankAccountRib;

	private String homeAddress;
	private String homePhone;
	private String businessPhone;
	private String businessFax;
	private String emergencyName1;
	private String emergencyPhone1;
	private String emergencyName2;
	private String emergencyPhone2;
	private String skypeId;
	private String imId;

	private Integer phoneExtension;
	private Boolean inTheOffice;
	private Boolean isWatched = false;

	private User user;

	public UserData() {
	}

	public UserData(User user) {
		super();
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnss() {
		return this.cnss;
	}

	public void setCnss(String cnss) {
		this.cnss = cnss;
	}

	public String getMartialStatus() {
		return this.martialStatus;
	}

	public void setMartialStatus(String martialStatus) {
		this.martialStatus = martialStatus;
	}

	public String getCimr() {
		return this.cimr;
	}

	public void setCimr(String cimr) {
		this.cimr = cimr;
	}

	public String getBusinessFax() {
		return this.businessFax;
	}

	public void setBusinessFax(String businessFax) {
		this.businessFax = businessFax;
	}

	public String getSkypeId() {
		return this.skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getImId() {
		return this.imId;
	}

	public void setImId(String imId) {
		this.imId = imId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIesourcing() {
		return this.iesourcing;
	}

	public void setIesourcing(String iesourcing) {
		this.iesourcing = iesourcing;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLadder() {
		return this.ladder;
	}

	public void setLadder(String ladder) {
		this.ladder = ladder;
	}

	public String getTercel() {
		return this.tercel;
	}

	public void setTercel(String tercel) {
		this.tercel = tercel;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "lastmodified", length = 10)
	public Date getLastModified() {
		return this.lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getMatricule() {
		return this.matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getMatriculeInssurance() {
		return this.matriculeInssurance;
	}

	public void setMatriculeInssurance(String matriculeInssurance) {
		this.matriculeInssurance = matriculeInssurance;
	}

	@Column(name = "matricule_axa", length = 200)
	public String getMatriculeAMC() {
		return matriculeAMC;
	}

	public void setMatriculeAMC(String matriculeAMC) {
		this.matriculeAMC = matriculeAMC;
	}

	public String getOfficeStatus() {
		return officeStatus;
	}

	public void setOfficeStatus(String officeStatus) {
		this.officeStatus = officeStatus;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	@Column(columnDefinition = "DOUBLE default '0'")
	public Double getLastGrossSalary() {
		return lastGrossSalary;
	}

	public void setLastGrossSalary(Double lastGrossSalary) {
		this.lastGrossSalary = lastGrossSalary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "passportid", length = 500)
	public String getPassportId() {
		return passportId;
	}

	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}

	@Temporal(TemporalType.DATE)
	public Date getPassportExpireDate() {
		return passportExpireDate;
	}

	public void setPassportExpireDate(Date passportExpireDate) {
		this.passportExpireDate = passportExpireDate;
	}

	public String getDriveLicenceId() {
		return driveLicenceId;
	}

	public void setDriveLicenceId(String driveLicenceId) {
		this.driveLicenceId = driveLicenceId;
	}

	public String getDriveLicenceType() {
		return driveLicenceType;
	}

	public void setDriveLicenceType(String driveLicenceType) {
		this.driveLicenceType = driveLicenceType;
	}

	@Temporal(TemporalType.DATE)
	public Date getDriveLicenceIssuedDate() {
		return driveLicenceIssuedDate;
	}

	public void setDriveLicenceIssuedDate(Date driveLicenceIssuedDate) {
		this.driveLicenceIssuedDate = driveLicenceIssuedDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getDriveLicenceExpireDate() {
		return driveLicenceExpireDate;
	}

	public void setDriveLicenceExpireDate(Date driveLicenceExpireDate) {
		this.driveLicenceExpireDate = driveLicenceExpireDate;
	}

	@Column(name = "business_phone_number", length = 100)
	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	@Column(name = "home_address", length = 100)
	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name = "pec_name", length = 500)
	public String getEmergencyName1() {
		return emergencyName1;
	}

	public void setEmergencyName1(String emergencyName1) {
		this.emergencyName1 = emergencyName1;
	}

	@Column(name = "pec_number", length = 500)
	public String getEmergencyPhone1() {
		return emergencyPhone1;
	}

	public void setEmergencyPhone1(String emergencyPhone1) {
		this.emergencyPhone1 = emergencyPhone1;
	}

	@Column(name = "sec_name", length = 500)
	public String getEmergencyName2() {
		return emergencyName2;
	}

	public void setEmergencyName2(String emergencyName2) {
		this.emergencyName2 = emergencyName2;
	}

	@Column(name = "sec_number", length = 500)
	public String getEmergencyPhone2() {
		return emergencyPhone2;
	}

	public void setEmergencyPhone2(String emergencyPhone2) {
		this.emergencyPhone2 = emergencyPhone2;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "name")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "surname")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	@Column(name = "p_mobile", length = 500)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "jobe_title", length = 200)
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public Date getCinExpireDate() {
		return cinExpireDate;
	}

	public void setCinExpireDate(Date cinExpireDate) {
		this.cinExpireDate = cinExpireDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "s_mobile")
	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Column(name = "secondary_mail_adresse", length = 200)
	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public Integer getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(Integer phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public Boolean getInTheOffice() {
		return inTheOffice;
	}

	public void setInTheOffice(Boolean inTheOffice) {
		this.inTheOffice = inTheOffice;
	}

	@Column(columnDefinition = "boolean default false")
	public Boolean getIsWatched() {
		return isWatched;
	}

	public void setIsWatched(Boolean isWatched) {
		this.isWatched = isWatched;
	}

	@Column(name = "bank_account", length = 200)
	public String getBankAccountRib() {
		return bankAccountRib;
	}

	public void setBankAccountRib(String bankAccountRib) {
		this.bankAccountRib = bankAccountRib;
	}

	@OneToOne
	@JoinColumn(name = "resurce_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}

}
