package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "request_for_quote")
public class RequestForQuote extends GenericModel<Integer> {

	private String reference;
	private Boolean existing;
	private Customer customer;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Boolean getExisting() {
		return existing;
	}

	public void setExisting(Boolean existing) {
		this.existing = existing;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_idcustomer")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
