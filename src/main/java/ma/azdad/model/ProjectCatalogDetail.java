package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ProjectCatalogDetail extends GenericModel<Integer> {

	private Integer reference;
	private String name;
	private String category;
	private String description;
	private String code;
	private String unit = "unit";
	private Double minQuantity = 0.0;
	private Double maxQuantity = 0.0;
	private ProjectCatalogDetailType type;
	private Boolean subcontracting = false;
	private Boolean supplierPo = false;
	private Boolean customerPo = false;

	private ProjectCatalog projectCatalog;
	private ProjectMilestone projectMilestone;

	@Transient
	public Integer getProjectMilestoneId() {
		return projectMilestone == null ? null : projectMilestone.getId();
	}

	@Transient
	public void setProjectMilestoneId(Integer projectMilestoneId) {
		if (projectMilestone == null || (projectMilestoneId != null && !projectMilestoneId.equals(projectMilestone.getId())))
			projectMilestone = new ProjectMilestone();
		projectMilestone.setId(projectMilestoneId);
	}

	@Transient
	public String getReferenceStr() {
		return String.format("%03d", reference);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReference() {
		return reference;
	}

	public void setReference(Integer reference) {
		this.reference = reference;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(Double minQuantity) {
		this.minQuantity = minQuantity;
	}

	public Double getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(Double maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public ProjectCatalogDetailType getType() {
		return type;
	}

	public void setType(ProjectCatalogDetailType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ProjectCatalog getProjectCatalog() {
		return projectCatalog;
	}

	public void setProjectCatalog(ProjectCatalog projectCatalog) {
		this.projectCatalog = projectCatalog;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public ProjectMilestone getProjectMilestone() {
		return projectMilestone;
	}

	public void setProjectMilestone(ProjectMilestone projectMilestone) {
		this.projectMilestone = projectMilestone;
	}

	public Boolean getSubcontracting() {
		return subcontracting;
	}

	public void setSubcontracting(Boolean subcontracting) {
		this.subcontracting = subcontracting;
	}

	public Boolean getSupplierPo() {
		return supplierPo;
	}

	public void setSupplierPo(Boolean supplierPo) {
		this.supplierPo = supplierPo;
	}

	public Boolean getCustomerPo() {
		return customerPo;
	}

	public void setCustomerPo(Boolean customerPo) {
		this.customerPo = customerPo;
	}

}
