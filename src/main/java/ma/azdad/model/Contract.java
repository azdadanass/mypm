package ma.azdad.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = Contract.TABLE_NAME)

public class Contract extends GenericModel<Integer> {
	public final static String TABLE_NAME = "e_contract_data";

	private Integer idcontract;
	private String contractType;
	private String legalEntity;
	private Date hireDate;
	private Date contractStartDate;
	private Date contractEndDate;
	private String contractStatus;
	private String description;
	private Date probationPeriodStartDate;
	private Date probationPeriodEndDate;
	private String probationExtended;
	private String confirmed;
	private Date probationPeriodExtensionStartDate;
	private Date probationPeriodExtensionEndDate;
	private Date confirmationDate;
	private String noticeWthProbation;
	private String noticeAftProbation;
	private Date resign;
	private Date fire;
	private String contractEndingComment;
	private Double daysVacation = 18.0;
	private String extensionReason;
	private String leaveStatus;
	private String resignReason;
	private String fireReason;

	private EGeneralData resource;
	private Company company;

	private Boolean stc = false;
	private Boolean stcIncludeLeaves = false;
	private Double stcAmount;
	private Date stcDate;
	private String stcDescription;
	private Currency stcCurrency;
	private Integer stcVacationYear1;
	private Integer stcPaidDaysVacation1 = 0;
	private Integer stcVacationYear2;
	private Integer stcPaidDaysVacation2 = 0;
	private Integer stcPaidDaysOvertime = 0;


	public Contract() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CONTRAT_Id", unique = true, nullable = false)
	public Integer getIdcontract() {
		return idcontract;
	}

	public void setIdcontract(Integer idcontract) {
		this.idcontract = idcontract;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESURCE_ID", nullable = false)
	public EGeneralData getResource() {
		return resource;
	}

	public void setResource(EGeneralData resource) {
		this.resource = resource;
	}

	@Column(name = "CONTRACT_TYPE", length = 100)
	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "HIRE_DATE", length = 10)
	public Date getHireDate() {
		return this.hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CONTRACT_START_DATE", length = 10)
	public Date getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CONTRACT_END_DATE", length = 10)
	public Date getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	@Column(name = "CONTRACT_STATUS", length = 100)
	public String getContractStatus() {
		return this.contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	@Column(name = "DESCRIPTION", length = 500)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ProbationPeriodStartDate", length = 10)
	public Date getProbationPeriodStartDate() {
		return this.probationPeriodStartDate;
	}

	public void setProbationPeriodStartDate(Date probationPeriodStartDate) {
		this.probationPeriodStartDate = probationPeriodStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ProbationPeriodEndDate", length = 10)
	public Date getProbationPeriodEndDate() {
		return this.probationPeriodEndDate;
	}

	public void setProbationPeriodEndDate(Date probationPeriodEndDate) {
		this.probationPeriodEndDate = probationPeriodEndDate;
	}

	@Column(name = "ProbationExtended", length = 100)
	public String getProbationExtended() {
		return this.probationExtended;
	}

	public void setProbationExtended(String probationExtended) {
		this.probationExtended = probationExtended;
	}

	@Column(name = "Confirmed", length = 100)
	public String getConfirmed() {
		return this.confirmed;
	}

	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ProbationPeriodExtensionStartDate", length = 10)
	public Date getProbationPeriodExtensionStartDate() {
		return this.probationPeriodExtensionStartDate;
	}

	public void setProbationPeriodExtensionStartDate(Date probationPeriodExtensionStartDate) {
		this.probationPeriodExtensionStartDate = probationPeriodExtensionStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ProbationPeriodExtensionEndDate", length = 10)
	public Date getProbationPeriodExtensionEndDate() {
		return this.probationPeriodExtensionEndDate;
	}

	public void setProbationPeriodExtensionEndDate(Date probationPeriodExtensionEndDate) {
		this.probationPeriodExtensionEndDate = probationPeriodExtensionEndDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ConfirmationDate", length = 10)
	public Date getConfirmationDate() {
		return this.confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	@Column(name = "NoticeWthProbation", length = 200)
	public String getNoticeWthProbation() {
		return this.noticeWthProbation;
	}

	public void setNoticeWthProbation(String noticeWthProbation) {
		this.noticeWthProbation = noticeWthProbation;
	}

	@Column(name = "NoticeAftProbation", length = 200)
	public String getNoticeAftProbation() {
		return this.noticeAftProbation;
	}

	public void setNoticeAftProbation(String noticeAftProbation) {
		this.noticeAftProbation = noticeAftProbation;
	}

	@Column(name = "LegalEntity", length = 100)
	public String getLegalEntity() {
		return this.legalEntity;
	}

	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Resign", length = 10)
	public Date getResign() {
		return this.resign;
	}

	public void setResign(Date resign) {
		this.resign = resign;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Fire", length = 10)
	public Date getFire() {
		return this.fire;
	}

	public void setFire(Date fire) {
		this.fire = fire;
	}

	@Column(name = "ContractEndingComment", length = 200)
	public String getContractEndingComment() {
		return this.contractEndingComment;
	}

	public void setContractEndingComment(String contractEndingComment) {
		this.contractEndingComment = contractEndingComment;
	}

	@Column(name = "days_vacation", length = 11, columnDefinition = "INT(11) default 18 ", nullable = false)
	public Double getDaysVacation() {
		return daysVacation;
	}

	public void setDaysVacation(Double daysVacation) {
		this.daysVacation = daysVacation;
	}

	@Column(name = "extension_reason", length = 500)
	public String getExtensionReason() {
		return extensionReason;
	}

	public void setExtensionReason(String extensionReason) {
		this.extensionReason = extensionReason;
	}

	@Column(name = "leave_status", length = 45, columnDefinition = "VARCHAR(45) default 'Paid'")
	public String getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(String leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	@Column(name = "resign_reason", length = 500)
	public String getResignReason() {
		return resignReason;
	}

	public void setResignReason(String resignReason) {
		this.resignReason = resignReason;
	}

	@Column(name = "fire_reason", length = 500)
	public String getFireReason() {
		return fireReason;
	}

	public void setFireReason(String fireReason) {
		this.fireReason = fireReason;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_idcompany", nullable = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stc_currency_id", nullable = true)
	public Currency getStcCurrency() {
		return stcCurrency;
	}

	public void setStcCurrency(Currency stcCurrency) {
		this.stcCurrency = stcCurrency;
	}

	

	

	@Column(name = "stc", columnDefinition = "boolean default false")
	public Boolean getStc() {
		return stc;
	}

	public void setStc(Boolean stc) {
		this.stc = stc;
	}

	@Column(name = "stc_include_leaves", columnDefinition = "boolean default false")
	public Boolean getStcIncludeLeaves() {
		return stcIncludeLeaves;
	}

	public void setStcIncludeLeaves(Boolean stcIncludeLeaves) {
		this.stcIncludeLeaves = stcIncludeLeaves;
	}

	@Column(name = "stc_amount")
	public Double getStcAmount() {
		return stcAmount;
	}

	public void setStcAmount(Double stcAmount) {
		this.stcAmount = stcAmount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "stc_date", length = 10)
	public Date getStcDate() {
		return stcDate;
	}

	public void setStcDate(Date stcDate) {
		this.stcDate = stcDate;
	}

	@Column(name = "stc_description", columnDefinition = "TEXT")
	public String getStcDescription() {
		return stcDescription;
	}

	public void setStcDescription(String stcDescription) {
		this.stcDescription = stcDescription;
	}

	@Column(name = "stc_vacation_year_1", length = 4)
	public Integer getStcVacationYear1() {
		return stcVacationYear1;
	}

	public void setStcVacationYear1(Integer stcVacationYear1) {
		this.stcVacationYear1 = stcVacationYear1;
	}

	@Column(name = "stc_paid_days_vacation_1", columnDefinition = "INT(11) default 0 ")
	public Integer getStcPaidDaysVacation1() {
		return stcPaidDaysVacation1;
	}

	public void setStcPaidDaysVacation1(Integer stcPaidDaysVacation1) {
		this.stcPaidDaysVacation1 = stcPaidDaysVacation1;
	}

	@Column(name = "stc_vacation_year_2", length = 4)
	public Integer getStcVacationYear2() {
		return stcVacationYear2;
	}

	public void setStcVacationYear2(Integer stcVacationYear2) {
		this.stcVacationYear2 = stcVacationYear2;
	}

	@Column(name = "stc_paid_days_vacation_2", columnDefinition = "INT(11) default 0 ")
	public Integer getStcPaidDaysVacation2() {
		return stcPaidDaysVacation2;
	}

	public void setStcPaidDaysVacation2(Integer stcPaidDaysVacation2) {
		this.stcPaidDaysVacation2 = stcPaidDaysVacation2;
	}

	@Column(name = "stc_paid_days_overtime", columnDefinition = "INT(11) default 0 ")
	public Integer getStcPaidDaysOvertime() {
		return stcPaidDaysOvertime;
	}

	public void setStcPaidDaysOvertime(Integer stcPaidDaysOvertime) {
		this.stcPaidDaysOvertime = stcPaidDaysOvertime;
	}

	@Override
	public String toString() {
		return "Contract [idcontract=" + idcontract + ", contractType=" + contractType + ", hireDate=" + hireDate + ", contractStartDate=" + contractStartDate + ", contractEndDate=" + contractEndDate + "," + resource.getUsername() + "]\n";
	}

	private Integer countFiles = 0;

	

	@Column(name = "count_files")
	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	
	

	

}