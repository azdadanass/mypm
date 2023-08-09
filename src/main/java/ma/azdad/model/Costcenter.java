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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Costcenter extends GenericModel<Integer> {

	private Integer year;
	private Double budget;
	private String status;
	private Integer countFiles = 0;

	private Lob lob;
	private Currency currency;

	private List<CostcenterFile> fileList = new ArrayList<>();
	private List<CostcenterHistory> historyList = new ArrayList<>();


	public Costcenter() {
	}

	public Costcenter(Integer id, Integer year, Double budget, String status, Integer countFiles, String lobName, String companyName, String currencyName) {
		super(id);
		this.year = year;
		this.budget = budget;
		this.status = status;
		this.countFiles = countFiles;
		this.setLobName(lobName);
		this.setCompanyName(companyName);
		this.setCurrencyName(currencyName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getLobName(), year);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getLobName() + "/" + year;
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public String getLobName() {
		return lob == null ? null : lob.getName();
	}

	@Transient
	public void setLobName(String lobName) {
		if (lob == null)
			lob = new Lob();
		lob.setName(lobName);
	}

	@Transient
	public Integer getLobId() {
		return lob == null ? null : lob.getId();
	}

	@Transient
	public void setLobId(Integer lobId) {
		if (lob == null)
			lob = new Lob();
		lob.setId(lobId);
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
	public String getCompanyName() {
		return lob == null ? null : lob.getCompanyName();
	}

	@Transient
	public void setCompanyName(String companyName) {
		if (lob == null)
			lob = new Lob();
		lob.setCompanyName(companyName);
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(CostcenterFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(CostcenterFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	public void addHistory(CostcenterHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(CostcenterHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}
	
	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcostcenter")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Lob getLob() {
		return lob;
	}

	public void setLob(Lob lob) {
		this.lob = lob;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CostcenterFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<CostcenterFile> fileList) {
		this.fileList = fileList;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CostcenterHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<CostcenterHistory> historyList) {
		this.historyList = historyList;
	}

}
