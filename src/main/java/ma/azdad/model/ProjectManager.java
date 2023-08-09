package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity

public class ProjectManager extends GenericModel<Integer> {

	private ProjectManagerType type;
	private User user;
	private Project project;
	
	

	public ProjectManager() {
		super();
	}

	public ProjectManager(ProjectManagerType type, User user) {
		super();
		this.type = type;
		this.user = user;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.getIdStr();
	}

	@Transient
	public String getUserUsername() {
		return user == null ? null : user.getUsername();
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null)
			user = new User();
		user.setUsername(userUsername);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	public ProjectManagerType getType() {
		return type;
	}

	public void setType(ProjectManagerType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
