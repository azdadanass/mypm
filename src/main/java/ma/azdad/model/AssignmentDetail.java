package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AssignmentDetail extends GenericModel<Integer> implements Serializable {

	private Project project;
	private Assignment assignment;

	public AssignmentDetail() {
		super();
	}

	public AssignmentDetail(Project project, Assignment assignment) {
		super();
		this.project = project;
		this.assignment = assignment;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

}
