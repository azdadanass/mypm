package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_supplementary_goals_comment")
public class SupplementaryGoalsComment extends GenericComment<SupplementaryGoals>{

	public SupplementaryGoalsComment() {
		super();
	}

	public SupplementaryGoalsComment(String title, User user, SupplementaryGoals parent) {
		super(title, user, parent);
	}

	public SupplementaryGoalsComment(String title, User user) {
		super(title, user);
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
