package ma.azdad.model;

import java.util.ArrayList;
import java.util.Collections;
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
public class InvoiceTerm extends GenericModel<Integer> implements Comparable<InvoiceTerm> {

	private Boolean ibuy = true;
	private Integer numero;
	private String phase;
	private Integer percentage = 0;
	private Integer duration;

	private InvoiceTermTemplate template;

	private List<PaymentTerm> detailList = new ArrayList<>();

	public InvoiceTerm() {
	}

	public InvoiceTerm(Integer numero, Integer percentage) {
		this.numero = numero;
		this.percentage = percentage;
	}

	public void orderDetailListAndCalculateDuration() {
		if (!detailList.isEmpty()) {
			Collections.sort(detailList);
			duration = detailList.get(0).getDuration();
		}
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	public void addDetail(PaymentTerm detail) {
		detail.setInvoiceTerm(this);
		detailList.add(detail);
	}

	public void addDetail(int index, PaymentTerm detail) {
		detail.setInvoiceTerm(this);
		detailList.add(index, detail);
	}

	public void removeDetail(PaymentTerm detail) {
		detail.setInvoiceTerm(null);
		detailList.remove(detail);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invoiceTerm", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<PaymentTerm> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<PaymentTerm> detailList) {
		this.detailList = detailList;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public InvoiceTermTemplate getTemplate() {
		return template;
	}

	public void setTemplate(InvoiceTermTemplate template) {
		this.template = template;
	}

	@Override
	public int compareTo(InvoiceTerm o) {
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
