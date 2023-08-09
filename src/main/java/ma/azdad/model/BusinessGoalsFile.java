package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_business_goals_file")
public class BusinessGoalsFile extends GenericFile<BusinessGoals> {

	public BusinessGoalsFile() {
		super();
	}

	public BusinessGoalsFile(File file, String type, String name, User user, BusinessGoals parent) {
		super(file, type, name, user, parent);
	}

	public BusinessGoalsFile(File file, String type, String name, User user) {
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
