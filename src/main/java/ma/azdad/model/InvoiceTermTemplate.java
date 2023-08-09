package ma.azdad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

public class InvoiceTermTemplate extends GenericModel<Integer> {

	private Boolean ibuy;
	private Integer numero;
	private String name;
	private Boolean active = true;
	private String description;
	private String detailedDescription;

	private Company company;
	private Supplier supplier;
	private Customer customer;

	private List<InvoiceTerm> detailList = new ArrayList<>();

	public InvoiceTermTemplate() {
	}

	public InvoiceTermTemplate(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		return result;
	}

	public void calculateDescription() {
		description = detailList.stream().map(i -> i.getPhase() + "[" + i.getPercentage() + "%]").collect(Collectors.joining(","));
	}

	public void calculateDetailedDescription() {
		detailedDescription = "";
		for (InvoiceTerm invoiceTerm : detailList) {
			detailedDescription += invoiceTerm.getNumero() + "- " + invoiceTerm.getPhase() + " [" + invoiceTerm.getPercentage() + "%] <br/>";
			for (PaymentTerm paymentTerm : invoiceTerm.getDetailList())
				detailedDescription += "&nbsp;&nbsp;&nbsp;PT" + paymentTerm.getNumero() + " : " + paymentTerm.getPercentage() + "%, " + paymentTerm.getDuration() + " Days from " + paymentTerm.getStartPoint().getValue() + "<br/>";
		}
	}

	@Transient
	public String getInvoiceTermStringKey() {
		return company.getId() + ";" + detailList.stream().map(i -> i.getNumero() + ";" + i.getPhase() + ";" + i.getDuration() + ";" + i.getPercentage()).collect(Collectors.joining(","));
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return "template " + numero;
	}

	public void addDetail(InvoiceTerm detail) {
		detail.setTemplate(this);
		detailList.add(detail);
	}

	public void removeDetail(InvoiceTerm detail) {
		detail.setTemplate(null);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<InvoiceTerm> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<InvoiceTerm> detailList) {
		this.detailList = detailList;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

}
