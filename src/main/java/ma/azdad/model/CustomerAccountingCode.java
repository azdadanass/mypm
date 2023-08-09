package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class CustomerAccountingCode extends GenericModel<Integer> {

	private Integer accountingCode;
	private Integer advPaymentAccountingCode = 44210000;
	private Company company;
	private Customer customer;

	public CustomerAccountingCode() {
	}

	public CustomerAccountingCode(Integer accountingCode, Company company) {
		this.accountingCode = accountingCode;
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

	public Integer getAccountingCode() {
		return accountingCode;
	}

	public void setAccountingCode(Integer accountingCode) {
		this.accountingCode = accountingCode;
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

	public Integer getAdvPaymentAccountingCode() {
		return advPaymentAccountingCode;
	}

	public void setAdvPaymentAccountingCode(Integer advPaymentAccountingCode) {
		this.advPaymentAccountingCode = advPaymentAccountingCode;
	}

}
