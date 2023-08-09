package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerHistory extends GenericHistory<Customer> {

	public CustomerHistory() {
	}

	public CustomerHistory(String status, User user) {
		super(status, user);
	}

	public CustomerHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public CustomerHistory(String status, User user, String description, Customer parent) {
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
