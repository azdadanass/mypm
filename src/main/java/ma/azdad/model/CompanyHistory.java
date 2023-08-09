package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CompanyHistory extends GenericHistory<Company> {

	public CompanyHistory() {
	}

	public CompanyHistory(String status, User user) {
		super(status, user);
	}

	public CompanyHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public CompanyHistory(String status, User user, String description, Company parent) {
		super(status, user, description, parent);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
