package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class CustomerServiceCode extends GenericModel<Integer> {

	private String serviceCode;
	private Double vat;
	private Integer vatAccountingCode = 44551000;
	private Boolean active = true;
	private Company company;
	private Customer customer;

	public CustomerServiceCode() {
	}

	public Boolean filter(Boolean active, Integer companyId) {
		return this.active.equals(active) && this.company.getId().equals(companyId);
	}

	public Boolean filter(Boolean active, Integer companyId, String serviceCode) {
		return filter(active, companyId) && this.serviceCode.equals(serviceCode);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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

	public Double getVat() {
		return vat;
	}

	public void setVat(Double vat) {
		this.vat = vat;
	}

	public Integer getVatAccountingCode() {
		return vatAccountingCode;
	}

	public void setVatAccountingCode(Integer vatAccountingCode) {
		this.vatAccountingCode = vatAccountingCode;
	}

}
