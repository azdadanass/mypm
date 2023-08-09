package ma.azdad.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectManager;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.repos.ProjectRepos;

@Component
public class ProjectService extends GenericService<Integer, Project, ProjectRepos> {

	@Autowired
	RequestForQuoteService requestForQuoteService;

	@Override
	@Cacheable("projectService.findAll")
	public List<Project> findAll() {
		return repos.findAll();
	}

	@Cacheable("projectService.find")
	public List<Project> find() {
		return repos.find();
	}

	@Cacheable("projectService.findWithoutTasks")
	public List<Project> findWithoutTasks() {
		return repos.findWithoutTasks();
	}

	@Cacheable("projectService.countWithoutTasks")
	public Long countWithoutTasks() {
		return repos.countWithoutTasks();
	}

	@Cacheable("projectService.findByUser")
	public List<Project> findByUser(String username) {
		return repos.findByUser(username);
	}

	@Override
	public Project findOne(Integer id) {
		Project project = super.findOne(id);
		initialize(project.getManager());
		initialize(project.getCustomer());
		initialize(project.getContract());
		initialize(project.getRequestForQuote());
		initialize(project.getCostcenter().getLob());
		initialize(project.getCurrency());
		initialize(project.getTaskList());
		initialize(project.getMilestoneList());
		initialize(project.getFileList());
		initialize(project.getHistoryList());
		initialize(project.getCommentList());
		initialize(project.getManagerList());
		return project;
	}

	@Cacheable("projectService.find")
	public List<Project> find(String status) {
		if (StringUtils.isBlank(status))
			return repos.find();
		return repos.find(status);
	}

	@Cacheable("projectService.findByCostcenter")
	public List<Project> findByCostcenter(Integer costcenterId) {
		return repos.findByCostcenter(costcenterId);
	}

	@Cacheable("projectService.findLight")
	public List<Project> findLight() {
		return repos.findLight();
	}

	@Cacheable("projectService.findByManager")
	public List<Project> findByManager(String managerUsername, String status) {
		if (StringUtils.isBlank(status))
			return repos.findByManager(managerUsername);
		return repos.findByManager(managerUsername, status);
	}

	@Cacheable("projectService.getManagerFullNameMap")
	public Map<Integer, Project> getManagerFullNameMap() {
		return repos.find().stream().collect(Collectors.toMap(i -> i.getId(), i -> i));
	}

	@Cacheable("projectService.findLightByManager")
	public List<Project> findLightByManagerAndStatus(String managerUsername, String status) {
		return repos.findLightByManagerAndStatus(managerUsername, status);
	}

	public Boolean isNameExists(Project project) {
		return repos.countByName(project.getName(), project.getId()) > 0;
	}

	@Override
	public Project save(Project model) {
		if (model.getRequestForQuote() != null && !model.getRequestForQuote().getExisting())
			requestForQuoteService.updateExistingAndCustomer(model.getRequestForQuoteId(), true, model.getCustomerId());
		return super.save(model);
	}

	public void updateSdm(Integer projectId, Boolean sdm) {
		repos.updateSdm(projectId, sdm);
		evictCache();
	}

	public void updateCustomerWarehousing(Integer projectId, Boolean customerWarehousing) {
		repos.updateCustomerWarehousing(projectId, customerWarehousing);
		evictCache();
	}

	public void updateCustomerStockManagement(Integer projectId, Boolean customerStockManagement) {
		repos.updateCustomerStockManagement(projectId, customerStockManagement);
		evictCache();
	}

	public void updateCompanyWarehousing(Integer projectId, Boolean companyWarehousing) {
		repos.updateCompanyWarehousing(projectId, companyWarehousing);
		evictCache();
	}

	public void updateCompanyStockManagement(Integer projectId, Boolean companyStockManagement) {
		repos.updateCompanyStockManagement(projectId, companyStockManagement);
		evictCache();
	}

	public void updateSupplierWarehousing(Integer projectId, Boolean supplierWarehousing) {
		repos.updateSupplierWarehousing(projectId, supplierWarehousing);
		evictCache();
	}

	public void updateSupplierStockManagement(Integer projectId, Boolean supplierStockManagement) {
		repos.updateSupplierStockManagement(projectId, supplierStockManagement);
		evictCache();
	}

	public void initProjectManagerScript() {

		repos.findIlogisticsProjectList().forEach(p -> {
			if (p.getManagerList().stream().filter(i -> ProjectManagerType.HARDWARE_MANAGER.equals(i.getType())).count() == 0)
				p.addManager(new ProjectManager(ProjectManagerType.HARDWARE_MANAGER, p.getManager()));
			if (p.getManagerList().stream().filter(i -> ProjectManagerType.SYSTEM_ENGINEER.equals(i.getType())).count() == 0)
				p.addManager(new ProjectManager(ProjectManagerType.SYSTEM_ENGINEER, p.getManager()));
			repos.save(p);
		});
		evictCache();
	}

}
