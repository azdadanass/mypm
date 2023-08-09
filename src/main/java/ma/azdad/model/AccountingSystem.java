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

public class AccountingSystem extends GenericModel<Integer> {

	private Integer numero;
	private String name;

	private Company company;
	private AccountingSystem parent;

	public AccountingSystem() {
		super();
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	@Transient
	public Integer getParentId() {
		if (parent == null)
			return null;
		return parent.getId();
	}

	@Transient
	public void setParentId(Integer parentId) {
		if (parent == null)
			parent = new AccountingSystem();
		parent.setId(parentId);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AccountingSystem getParent() {
		return parent;
	}

	public void setParent(AccountingSystem parent) {
		this.parent = parent;
	}

}
