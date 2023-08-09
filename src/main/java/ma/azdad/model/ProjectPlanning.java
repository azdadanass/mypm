package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ProjectPlanning extends GenericModel<Integer> {

	private Integer month;
	private Integer year;
	private Double sales;
	private Double revenue;
	private Double cost;
	private Double cashIn;
	private Double cashOut;

	private Project project;

	// tmp
	private Double cumulative;

	public ProjectPlanning() {
	}

	public ProjectPlanning(Integer month, Integer year, Double sales, Double revenue, Double cost, Double cashIn, Double cashOut, Project project) {
		this.month = month;
		this.year = year;
		this.sales = sales;
		this.revenue = revenue;
		this.cost = cost;
		this.cashIn = cashIn;
		this.cashOut = cashOut;
		this.project = project;
	}

	public ProjectPlanning(Integer id, Integer month, Integer year, Double sales, Double revenue, Double cost, Double cashIn, Double cashOut, Double cumulative, Integer projectId, String projectName) {
		this.id = id;
		this.month = month;
		this.year = year;
		this.sales = sales;
		this.revenue = revenue;
		this.cost = cost;
		this.cashIn = cashIn;
		this.cashOut = cashOut;
		this.cumulative = cumulative;
		this.setProjectId(projectId);
		this.setProjectName(projectName);
	}

	@Transient
	public Double getMargin() {
		return revenue - cost;
	}

	@Transient
	public Double getDelta() {
		return cashIn - cashOut;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getProjectName() {
		return project == null ? null : project.getName();
	}

	@Transient
	public void setProjectName(String projectName) {
		if (project == null)
			project = new Project();
		project.setName(projectName);
	}

	@Transient
	public Integer getProjectId() {
		return project == null ? null : project.getId();
	}

	@Transient
	public void setProjectId(Integer projectId) {
		if (project == null)
			project = new Project();
		project.setId(projectId);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idprojectplanning")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getSales() {
		return sales;
	}

	public void setSales(Double sales) {
		this.sales = sales;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getCashIn() {
		return cashIn;
	}

	public void setCashIn(Double cashIn) {
		this.cashIn = cashIn;
	}

	public Double getCashOut() {
		return cashOut;
	}

	public void setCashOut(Double cashOut) {
		this.cashOut = cashOut;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Transient
	public Double getCumulative() {
		return cumulative;
	}

	@Transient
	public void setCumulative(Double cumulative) {
		this.cumulative = cumulative;
	}

}
