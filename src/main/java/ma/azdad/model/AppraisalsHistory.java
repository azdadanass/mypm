package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_appraisals_history")
public class AppraisalsHistory extends GenericHistory<Appraisals>{

	public AppraisalsHistory() {
		super();
	}

	public AppraisalsHistory(String status, User user, String description, Appraisals parent) {
		super(status, user, description, parent);
	}

	public AppraisalsHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public AppraisalsHistory(String status, User user) {
		super(status, user);
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
