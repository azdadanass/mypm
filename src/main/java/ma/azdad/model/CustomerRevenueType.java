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

@Entity

public class CustomerRevenueType extends GenericModel<Integer> {

	private RevenueCategory revenueCategory;
	private RevenueType revenueType;
	private Boolean active = true;
	private Company company;
	private Customer customer;

	public CustomerRevenueType() {
	}

	public CustomerRevenueType(RevenueCategory revenueCategory, RevenueType revenueType, Company company) {
		this.revenueCategory = revenueCategory;
		this.revenueType = revenueType;
		this.company = company;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public RevenueCategory getRevenueCategory() {
		return revenueCategory;
	}

	public void setRevenueCategory(RevenueCategory revenueCategory) {
		this.revenueCategory = revenueCategory;
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public RevenueType getRevenueType() {
		return revenueType;
	}

	public void setRevenueType(RevenueType revenueType) {
		this.revenueType = revenueType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
