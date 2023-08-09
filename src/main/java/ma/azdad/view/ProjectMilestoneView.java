package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectMilestone;
import ma.azdad.repos.ProjectMilestoneRepos;
import ma.azdad.service.ProjectMilestoneService;

@ManagedBean
@Component
@Scope("view")
public class ProjectMilestoneView extends GenericView<Integer, ProjectMilestone, ProjectMilestoneRepos, ProjectMilestoneService> {

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	// generic

	public List<ProjectMilestone> findByProject(Integer projectId) {
		return service.findByProject(projectId);
	}

	// getters & setters
	public ProjectMilestone getModel() {
		return model;
	}

	public void setModel(ProjectMilestone model) {
		this.model = model;
	}

}
