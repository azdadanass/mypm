package ma.azdad.model;

import java.util.ArrayList;
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
import javax.persistence.Transient;

@Entity

public class Bu extends GenericModel<Integer> {

	private String name;
	private String description;
	private String operatingSector;
	private Integer countFiles = 0;

	private Company company;
	private User director;
	private User operationsDirector;
	private User financeManager;
	private User technicalDirector;
	private User hrDirector;

//	private List<Lob> lobList = new ArrayList<>();
	private List<BuFile> fileList = new ArrayList<>();
	private List<BuHistory> historyList = new ArrayList<>();

	public Bu() {
	}

	public Bu(Integer id, String name, String operatingSector, Integer countFiles, String companyName, String directorFullName, String operationsDirectorFullName, String financeManagerFullName, String technicalDirectorFullName, String hrDirectorFullName) {
		super(id);
		this.name = name;
		this.operatingSector = operatingSector;
		this.countFiles = countFiles;
		this.setCompanyName(companyName);
		this.setDirectorFullName(hrDirectorFullName);
		this.setOperationsDirectorFullName(operationsDirectorFullName);
		this.setFinanceManagerFullName(financeManagerFullName);
		this.setTechnicalDirectorFullName(technicalDirectorFullName);
		this.setHrDirectorFullName(hrDirectorFullName);
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
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null)
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public String getDirectorFullName() {
		return director == null ? null : director.getFullName();
	}

	@Transient
	public void setDirectorFullName(String directorFullName) {
		if (director == null)
			director = new User();
		director.setFullName(directorFullName);
	}

	@Transient
	public String getDirectorUsername() {
		return director == null ? null : director.getUsername();
	}

	@Transient
	public void setDirectorUsername(String directorUsername) {
		if (director == null)
			director = new User();
		director.setUsername(directorUsername);
	}

	@Transient
	public String getFinanceManagerFullName() {
		return financeManager == null ? null : financeManager.getFullName();
	}

	@Transient
	public void setFinanceManagerFullName(String financeManagerFullName) {
		if (financeManager == null)
			financeManager = new User();
		financeManager.setFullName(financeManagerFullName);
	}

	@Transient
	public String getOperationsDirectorFullName() {
		return operationsDirector == null ? null : operationsDirector.getFullName();
	}

	@Transient
	public void setOperationsDirectorFullName(String operationsDirectorFullName) {
		if (operationsDirector == null)
			operationsDirector = new User();
		operationsDirector.setFullName(operationsDirectorFullName);
	}

	@Transient
	public String getTechnicalDirectorFullName() {
		return technicalDirector == null ? null : technicalDirector.getFullName();
	}

	@Transient
	public void setTechnicalDirectorFullName(String technicalDirectorFullName) {
		if (technicalDirector == null)
			technicalDirector = new User();
		technicalDirector.setFullName(technicalDirectorFullName);
	}

	@Transient
	public String getHrDirectorFullName() {
		return hrDirector == null ? null : hrDirector.getFullName();
	}

	@Transient
	public void setHrDirectorFullName(String hrDirectorFullName) {
		if (hrDirector == null)
			hrDirector = new User();
		hrDirector.setFullName(hrDirectorFullName);
	}

	@Transient
	public String getFinanceManagerUsername() {
		return financeManager == null ? null : financeManager.getUsername();
	}

	@Transient
	public void setFinanceManagerUsername(String financeManagerUsername) {
		if (financeManager == null)
			financeManager = new User();
		financeManager.setUsername(financeManagerUsername);
	}

	@Transient
	public String getOperationsDirectorUsername() {
		return operationsDirector == null ? null : operationsDirector.getUsername();
	}

	@Transient
	public void setOperationsDirectorUsername(String operationsDirectorUsername) {
		if (operationsDirector == null)
			operationsDirector = new User();
		operationsDirector.setUsername(operationsDirectorUsername);
	}

	@Transient
	public String getTechnicalDirectorUsername() {
		return technicalDirector == null ? null : technicalDirector.getUsername();
	}

	@Transient
	public void setTechnicalDirectorUsername(String technicalDirectorUsername) {
		if (technicalDirector == null)
			technicalDirector = new User();
		technicalDirector.setUsername(technicalDirectorUsername);
	}

	@Transient
	public String getHrDirectorUsername() {
		return hrDirector == null ? null : hrDirector.getUsername();
	}

	@Transient
	public void setHrDirectorUsername(String hrDirectorUsername) {
		if (hrDirector == null)
			hrDirector = new User();
		hrDirector.setUsername(hrDirectorUsername);
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(BuFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(BuFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}
	
	public void addHistory(BuHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(BuHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

//	public void addLob(Lob lob) {
//		lob.setBu(this);
//		lobList.add(lob);
//	}
//
//	public void removeLob(Lob lob) {
//		lob.setBu(null);
//		lobList.remove(lob);
//	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idbu", unique = true, nullable = false)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BuFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<BuFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bu", cascade = CascadeType.ALL, orphanRemoval = true)
//	public List<Lob> getLobList() {
//		return lobList;
//	}
//
//	public void setLobList(List<Lob> lobList) {
//		this.lobList = lobList;
//	}

	@Column(name = "operatingsector")
	public String getOperatingSector() {
		return operatingSector;
	}

	public void setOperatingSector(String operatingSector) {
		this.operatingSector = operatingSector;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "director_id", nullable = true)
	public User getDirector() {
		return director;
	}

	public void setDirector(User director) {
		this.director = director;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operationsdirector_id", nullable = true)
	public User getOperationsDirector() {
		return operationsDirector;
	}

	public void setOperationsDirector(User operationsDirector) {
		this.operationsDirector = operationsDirector;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "financemanager_id", nullable = true)
	public User getFinanceManager() {
		return financeManager;
	}

	public void setFinanceManager(User financeManager) {
		this.financeManager = financeManager;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "technicaldirector_id", nullable = true)
	public User getTechnicalDirector() {
		return technicalDirector;
	}

	public void setTechnicalDirector(User technicalDirector) {
		this.technicalDirector = technicalDirector;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hrdirector_id", nullable = true)
	public User getHrDirector() {
		return hrDirector;
	}

	public void setHrDirector(User hrDirector) {
		this.hrDirector = hrDirector;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BuHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<BuHistory> historyList) {
		this.historyList = historyList;
	}

}
