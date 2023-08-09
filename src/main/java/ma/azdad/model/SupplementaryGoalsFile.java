package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_supplementary_goals_file")
public class SupplementaryGoalsFile extends GenericFile<SupplementaryGoals> {

	public SupplementaryGoalsFile() {
		super();
	}

	public SupplementaryGoalsFile(File file, String type, String name, User user, SupplementaryGoals parent) {
		super(file, type, name, user, parent);
	}

	public SupplementaryGoalsFile(File file, String type, String name, User user) {
		super(file, type, name, user);
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
