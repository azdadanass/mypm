package ma.azdad.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CostcenterHistory extends GenericHistory<Costcenter> {

	public CostcenterHistory() {
	}
	
	public CostcenterHistory(String status, User user) {
		super(status, user);
	}
	
	public CostcenterHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public CostcenterHistory(String status, User user, String description, Costcenter parent) {
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