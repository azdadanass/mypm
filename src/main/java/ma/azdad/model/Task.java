package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class Task extends GenericModel<Integer> {

	private String name;
	private Double effort;

	private Project project;

	public Task() {
		super();
	}

	public Task(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, name);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtask")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getEffort() {
		return effort;
	}

	public void setEffort(Double effort) {
		this.effort = effort;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
