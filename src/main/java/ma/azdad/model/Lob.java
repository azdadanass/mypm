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

public class Lob extends GenericModel<Integer> {

	private String name;
	private String description;
	private String type;
	private Integer countFiles = 0;

	private Bu bu;
	private User manager;
//	private List<Costcenter> costcenterList = new ArrayList<>();
	private List<LobFile> fileList = new ArrayList<>();
	private List<LobHistory> historyList = new ArrayList<>();

	public Lob() {
		super();
	}
	
	

	public Lob(Integer id,String name) {
		super(id);
		this.name = name;
	}



	public Lob(Integer id, String name, String description, String type, Integer countFiles, String buName, String companyName, String managerFullName) {
		super(id);
		this.name = name;
		this.description = description;
		this.type = type;
		this.countFiles = countFiles;
		this.setBuName(buName);
		this.setCompanyName(companyName);
		this.setManagerFullName(managerFullName);
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
		return bu == null ? null : bu.getCompanyName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (bu == null)
			bu = new Bu();
		bu.setCompanyName(companyName);
	}

	@Transient
	public String getBuName() {
		return bu == null ? null : bu.getName();
	}

	@Transient
	public void setBuName(String buName) {
		if (bu == null)
			bu = new Bu();
		bu.setName(buName);
	}

	@Transient
	public Integer getBuId() {
		return bu == null ? null : bu.getId();
	}

	@Transient
	public void setBuId(Integer buId) {
		if (bu == null)
			bu = new Bu();
		bu.setId(buId);
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

	@Transient
	public String getManagerUsername() {
		return manager == null ? null : manager.getUsername();
	}

	@Transient
	public void setManagerUsername(String managerUsername) {
		if (manager == null)
			manager = new User();
		manager.setUsername(managerUsername);
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(LobFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(LobFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}
	
	public void addHistory(LobHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(LobHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

//	public void addCostcenter(Costcenter costcenter) {
//		costcenter.setLob(this);
//		costcenterList.add(costcenter);
//	}
//
//	public void removeCostcenter(Costcenter costcenter) {
//		costcenter.setLob(null);
//		costcenterList.remove(costcenter);
//	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlob")
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
	public List<LobFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<LobFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lob", cascade = CascadeType.ALL, orphanRemoval = true)
//	public List<Costcenter> getCostcenterList() {
//		return costcenterList;
//	}
//
//	public void setCostcenterList(List<Costcenter> costcenterList) {
//		this.costcenterList = costcenterList;
//	}

	@Column(name = "lob_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Bu getBu() {
		return bu;
	}

	public void setBu(Bu bu) {
		this.bu = bu;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "manager_idmanager")
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<LobHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<LobHistory> historyList) {
		this.historyList = historyList;
	}
}
