package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectCatalog;
import ma.azdad.repos.ProjectCatalogRepos;

@Component
public class ProjectCatalogService extends GenericService<Integer, ProjectCatalog, ProjectCatalogRepos> {

	@Cacheable("projectCatalogService.find")
	public List<ProjectCatalog> find(Boolean active) {
		if (active == null)
			return repos.find();
		return repos.findByActive(active);
	}

	@Override
	@Cacheable("projectCatalogService.findAll")
	public List<ProjectCatalog> findAll() {
		return repos.findAll();
	}

	@Override
	public ProjectCatalog save(ProjectCatalog model) {
		evictCache("jobRequestService");
		return super.save(model);
	}

	@Override
	@Cacheable("projectCatalogService.findOne")
	public ProjectCatalog findOne(Integer id) {
		ProjectCatalog projectCatalog = super.findOne(id);
		initialize(projectCatalog.getProject().getCustomer());
		initialize(projectCatalog.getEndCustomer());
		initialize(projectCatalog.getDetailList());
		initialize(projectCatalog.getFileList());
		initialize(projectCatalog.getHistoryList());
		initialize(projectCatalog.getCommentList());
		return projectCatalog;
	}

	@Cacheable("projectCatalogService.findOne")
	public ProjectCatalog findOne(Integer id, String... toInitialise) {
		ProjectCatalog projectCatalog = super.findOne(id);
		List<String> list = Arrays.asList(toInitialise);
		if (list.contains("detailList"))
			initialize(projectCatalog.getDetailList());
		return projectCatalog;
	}

	public Long countByName(ProjectCatalog projectCatalog) {
		return repos.countByName(projectCatalog.getName(), projectCatalog.getId());
	}

	public void updateProjectCatalogDetailCustomerPoByProject(Boolean customerPo, Integer projectId) {
		repos.updateProjectCatalogDetailCustomerPoByProject(customerPo, projectId);
	}

}
