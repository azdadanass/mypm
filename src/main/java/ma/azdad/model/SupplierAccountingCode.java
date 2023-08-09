package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class SupplierAccountingCode extends GenericModel<Integer> {

	private Integer accountingCode;
	private Integer advPaymentAccountingCode = 34110000;
	private Company company;
	private Supplier supplier;

	public SupplierAccountingCode() {
	}

	public SupplierAccountingCode(Integer accountingCode, Company company) {
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
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Integer getAdvPaymentAccountingCode() {
		return advPaymentAccountingCode;
	}

	public void setAdvPaymentAccountingCode(Integer advPaymentAccountingCode) {
		this.advPaymentAccountingCode = advPaymentAccountingCode;
	}

}
