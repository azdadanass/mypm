package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectMilestone;
import ma.azdad.repos.ProjectMilestoneRepos;

@Component
public class ProjectMilestoneService extends GenericService<Integer, ProjectMilestone, ProjectMilestoneRepos> {

	@Override
	public List<ProjectMilestone> findAll() {
		return repos.findAll();
	}

	@Override
	public ProjectMilestone findOne(Integer id) {
		ProjectMilestone projectMilestone = super.findOne(id);

		return projectMilestone;
	}

	@Cacheable("projectService.findByProject")
	public List<ProjectMilestone> findByProject(Integer projectId) {
		return repos.findByProject(projectId);
	}

}
