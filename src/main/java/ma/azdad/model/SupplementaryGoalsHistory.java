package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_supplementary_goals_history")
public class SupplementaryGoalsHistory extends GenericHistory<SupplementaryGoals>{

	public SupplementaryGoalsHistory() {
		super();
	}

	public SupplementaryGoalsHistory(String status, User user, String description, SupplementaryGoals parent) {
		super(status, user, description, parent);
	}

	public SupplementaryGoalsHistory(String status, User user, String description) {
		super(status, user, description);
	}

	public SupplementaryGoalsHistory(String status, User user) {
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
