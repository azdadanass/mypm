package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payment_term")

public class PaymentTerm extends GenericModel<Integer> implements Comparable<PaymentTerm> {

	private Boolean ibuy = true;
	private Integer numero;
	private Integer percentage = 100;
	private Integer duration = 0;
	private PaymentTermStartPoint startPoint;

	private InvoiceTerm invoiceTerm;

	public PaymentTerm() {
	}

	public PaymentTerm(Integer numero, Integer percentage) {
		this.numero = numero;
		this.percentage = percentage;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Enumerated(EnumType.STRING)
	public PaymentTermStartPoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(PaymentTermStartPoint startPoint) {
		this.startPoint = startPoint;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public InvoiceTerm getInvoiceTerm() {
		return invoiceTerm;
	}

	public void setInvoiceTerm(InvoiceTerm invoiceTerm) {
		this.invoiceTerm = invoiceTerm;
	}

	@Override
	public int compareTo(PaymentTerm o) {
		if (numero != null)
			return numero.compareTo(o.getNumero());
		else if (id != null)
			return id.compareTo(o.getNumero());
		else
			return -1;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

}
