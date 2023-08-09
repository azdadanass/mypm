package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_sections_data_new")
public class SectionsData extends GenericModel<Integer>{

	private String goalTitle;
	private String goalDescription;
	private String sectionTitle;
	private Integer goaldId;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "goalTitle")
	public String getGoalTitle() {
		return goalTitle;
	}

	public void setGoalTitle(String goalTitle) {
		this.goalTitle = goalTitle;
	}
	
	@Column(name = "goalDescription")
	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}
	
	@Column(name = "sectionTitle")
	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	
	public Integer getGoaldId() {
		return goaldId;
	}

	public void setGoaldId(Integer goaldId) {
		this.goaldId = goaldId;
	}	
}