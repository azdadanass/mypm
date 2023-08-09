package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Project;
import ma.azdad.model.ProjectComment;
import ma.azdad.model.ProjectFile;
import ma.azdad.model.ProjectHistory;
import ma.azdad.model.ProjectManager;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.ProjectMilestone;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.Task;
import ma.azdad.model.TaskTemplate;
import ma.azdad.repos.ProjectRepos;
import ma.azdad.service.ContractService;
import ma.azdad.service.CostcenterService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.ProjectCatalogService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class ProjectView extends GenericView<Integer, Project, ProjectRepos, ProjectService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	UserService userService;

	@Autowired
	CostcenterService costcenterService;

	@Autowired
	CustomerService customerService;

	@Autowired
	ContractService contractService;

	@Autowired
	UserView userView;

	@Autowired
	CacheView cacheView;

	@Autowired
	ProjectCatalogService projectCatalogService;

	private String status = ProjectStatus.OPEN.getValue();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin() || sessionView.isTheConnectedUser(model.getManagerUsername())
				|| model.getManagerList().stream().map(i -> i.getUserUsername()).filter(u -> u.equalsIgnoreCase(sessionView.getUsername())).count() > 0;
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	protected void viewPage() {
		super.viewPage();
		evictCache();
		old = service.findOne(id);
	}

	@Override
	public void refreshList() {
		if (isListPage) {
			if (pageIndex == null)
				initLists(service.find(status));
			else
				switch (pageIndex) {
				case 1:
					initLists(service.findWithoutTasks());
					break;
				case 2:
					initLists(service.findByUser(sessionView.getUsername()));
					break;
				default:
					break;
				}
		} else if (isPage("viewUser"))
			initLists(service.findByManager(userView.getModel().getUsername(), status));
		else if (isPage("viewCostcenter"))
			initLists(service.findByCostcenter(id));
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.setManager(userService.findOneLight(model.getManagerUsername()));
		model.setCostcenter(costcenterService.findOneLight(model.getCostcenterId()));
		if (model.getCustomerId() != null) {
			model.setCustomer(customerService.findOneLight(model.getCustomerId()));
			if (model.getContractId() != null)
				model.setContract(contractService.findOneLight(model.getContractId()));
			else
				model.setContract(null);
		}

		if (isAddPage) {
			model.addManager(new ProjectManager(ProjectManagerType.HARDWARE_MANAGER, model.getManager()));
			model.addManager(new ProjectManager(ProjectManagerType.SYSTEM_ENGINEER, model.getManager()));
		}

		model.addHistory(new ProjectHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);

		if (model.getIsCustomerTypeCompany())
			projectCatalogService.updateProjectCatalogDetailCustomerPoByProject(false, model.getId());

		cacheView.updateProjectReport(model.getId());
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than End Date");
		if (service.isNameExists(model))
			return FacesContextMessages.ErrorMessages("Name alreday exists in database");

		// TODO project assignement validation dates
		// TODO wtr assignement validation dates

		return true;
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.getIsInternalAdmin();
	}

	public void toggle() {
		if (!canToggle())
			return;
		if ("Open".equals(model.getStatus()))
			model.setStatus("Closed");
		else
			model.setStatus("Open");
		model = service.saveAndRefresh(model);
	}

	// inplance
	public Boolean canEditInplace() {
		return sessionView.getIsInternalAdmin();
	}

	public void updateSdm() {
		if (!canEditInplace())
			return;
		service.updateSdm(model.getId(), model.getSdm());
	}

	public void updateCustomerWarehousing() {
		if (!canEditInplace())
			return;
		service.updateCustomerWarehousing(model.getId(), model.getCustomerWarehousing());
		if (!model.getCustomerWarehousing() && model.getCustomerStockManagement()) {
			model.setCustomerStockManagement(false);
			service.updateCustomerStockManagement(model.getId(), model.getCustomerStockManagement());
		}
	}

	public void updateCustomerStockManagement() {
		if (!canEditInplace())
			return;
		service.updateCustomerStockManagement(model.getId(), model.getCustomerStockManagement());
		if (model.getCustomerStockManagement() && !model.getCustomerWarehousing()) {
			model.setCustomerWarehousing(true);
			service.updateCustomerWarehousing(model.getId(), model.getCustomerWarehousing());
		}
	}

	public void updateCompanyWarehousing() {
		if (!canEditInplace())
			return;
		service.updateCompanyWarehousing(model.getId(), model.getCompanyWarehousing());
		if (!model.getCompanyWarehousing() && model.getCompanyStockManagement()) {
			model.setCompanyStockManagement(false);
			service.updateCompanyStockManagement(model.getId(), model.getCompanyStockManagement());
		}
	}

	public void updateCompanyStockManagement() {
		if (!canEditInplace())
			return;
		service.updateCompanyStockManagement(model.getId(), model.getCompanyStockManagement());
		if (model.getCompanyStockManagement() && !model.getCompanyWarehousing()) {
			model.setCompanyWarehousing(true);
			service.updateCompanyWarehousing(model.getId(), model.getCompanyWarehousing());
		}
	}

	public void updateSupplierWarehousing() {
		if (!canEditInplace())
			return;
		service.updateSupplierWarehousing(model.getId(), model.getSupplierWarehousing());
		if (!model.getSupplierWarehousing() && model.getSupplierStockManagement()) {
			model.setSupplierStockManagement(false);
			service.updateSupplierStockManagement(model.getId(), model.getSupplierStockManagement());
		}
	}

	public void updateSupplierStockManagement() {
		if (!canEditInplace())
			return;
		service.updateSupplierStockManagement(model.getId(), model.getSupplierStockManagement());
		if (model.getSupplierStockManagement() && !model.getSupplierWarehousing()) {
			model.setSupplierWarehousing(true);
			service.updateSupplierWarehousing(model.getId(), model.getSupplierWarehousing());
		}
	}

	// delete
	public Boolean canDelete() {
		return sessionView.getIsInternalAdmin();
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// tasks
	public Boolean editTaskList = false;

	public Boolean canEditTaskList() {
		return sessionView.getIsInternalAdmin() && !editTaskList;
	}

	public Boolean canAddTask() {
		return sessionView.getIsInternalAdmin() && editTaskList;
	}

	public void addTask() {
		if (!canAddTask())
			return;
		model.addTask(new Task());
	}

	public Boolean canSaveTaskList() {
		return sessionView.getIsInternalAdmin() && editTaskList;
	}

	public Boolean validateSaveTaskList() {
		Set<String> keySet = new HashSet<String>();
		for (Task task : model.getTaskList()) {
			if (task.getName() == null || task.getName().isEmpty())
				return FacesContextMessages.ErrorMessages("Name should not be null");
			if (!keySet.add(task.getName()))
				return FacesContextMessages.ErrorMessages("Duplicate Line !");
		}

		return true;
	}

	public void saveTaskList() {
		if (!canSaveTaskList() || !validateSaveTaskList())
			return;
		model = service.saveAndRefresh(model);
		editTaskList = false;
	}

	public Boolean canDeleteTask() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteTask(Task task) {
		if (!canDeleteTask())
			return;
		model.removeTask(task);
		model = service.saveAndRefresh(model);
	}

	public void generateTaskListFromTemplate(TaskTemplate template) {
		System.out.println(template);
		System.out.println(template.getDetailList());

		template.getDetailList().forEach(i -> System.out.println(i.getName()));
		template.getDetailList().forEach(i -> model.addTask(new Task(i.getName())));
	}

	// milestone list
	public Boolean editMilestoneList = false;

	public Boolean canEditMilestoneList() {
		return sessionView.getIsInternalAdmin() && !editMilestoneList;
	}

	public Boolean canAddMilestone() {
		return sessionView.getIsInternalAdmin() && editMilestoneList;
	}

	public void addMilestone() {
		if (!canAddMilestone())
			return;
		model.addMilestone(new ProjectMilestone());
	}

	public Boolean canSaveMilestoneList() {
		return sessionView.getIsInternalAdmin() && editMilestoneList;
	}

	public Boolean validateSaveMilestoneList() {
		Set<String> nameKeySet = new HashSet<String>();
		if (model.getMilestoneList().stream().filter(m -> m.getNumero() != null).map(m -> m.getNumero()).distinct().count() != model.getMilestoneList().size())
			return FacesContextMessages.ErrorMessages("Please to correct numeros");
		Collections.sort(model.getMilestoneList());
		Double percentage = 0.0;
		for (ProjectMilestone milestone : model.getMilestoneList()) {
			if (milestone.getNumero() == null)
				return FacesContextMessages.ErrorMessages("Numero should not be null");
			if (StringUtils.isBlank(milestone.getName()))
				return FacesContextMessages.ErrorMessages("Name should not be null");
			if (milestone.getType() == null)
				return FacesContextMessages.ErrorMessages("Type should not be null");
			if (milestone.getCompletionPercentage() == null)
				return FacesContextMessages.ErrorMessages("Completion percentage should not be null");
			if (!nameKeySet.add(milestone.getName()))
				return FacesContextMessages.ErrorMessages("Duplicate Line !");
			if (milestone.getCompletionPercentage() <= percentage)
				return FacesContextMessages.ErrorMessages("Completion percentage must be increasing");
			percentage = milestone.getCompletionPercentage();
		}

		return true;
	}

	public void saveMilestoneList() {
		if (!canSaveMilestoneList() || !validateSaveMilestoneList())
			return;
		model = service.saveAndRefresh(model);
		editMilestoneList = false;
	}

	public Boolean canDeleteMilestone() {
		return sessionView.getIsInternalAdmin() && sessionView.getInternal();
	}

	public void deleteMilestone(ProjectMilestone milestone) {
		if (!canDeleteMilestone())
			return;
		model.removeMilestone(milestone);
		model = service.saveAndRefresh(model);
	}

	// managers
	public Boolean editManagerList = false;

	public Boolean canEditManagerList() {
		return sessionView.getIsInternalAdmin() && !editManagerList;
	}

	public Boolean canAddManager() {
		return sessionView.getIsInternalAdmin() && editManagerList;
	}

	public void addManager() {
		if (!canAddManager())
			return;
		model.addManager(new ProjectManager());
	}

	public Boolean canSaveManagerList() {
		return sessionView.getIsInternalAdmin() && editManagerList;
	}

	public Boolean validateSaveManagerList() {
		Set<String> keySet = new HashSet<String>();
		for (ProjectManager manager : model.getManagerList()) {
			if (manager.getUserUsername() == null || manager.getUserUsername().isEmpty())
				return FacesContextMessages.ErrorMessages("User should not be null");
			if (manager.getType() == null)
				return FacesContextMessages.ErrorMessages("Type should not be null");
			if (!keySet.add(manager.getType().ordinal() + manager.getUserUsername()))
				return FacesContextMessages.ErrorMessages("Duplicate Line !");
		}

		return true;
	}

	public void saveManagerList() {
		if (!canSaveManagerList() || !validateSaveManagerList())
			return;
		model.getManagerList().forEach(i -> i.setUser(userService.findOneLight(i.getUserUsername())));
		model = service.saveAndRefresh(model);
		editManagerList = false;
	}

	public Boolean canDeleteManager() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteManager(ProjectManager manager) {
		if (!canDeleteManager())
			return;
		model.removeManager(manager);
		model = service.saveAndRefresh(model);
	}

	// files
	private ProjectFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		ProjectFile modelFile = new ProjectFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (ProjectView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// comments
	private ProjectComment comment = new ProjectComment();

	public Boolean canAddComment() {
		return true;
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	// project P&L
	public void updateDuration() {
		model.calculateDuration();
	}

	public void updateNetMargin() {
		model.setNetMargin(model.getEbida() * (1 - (model.getTax() + model.getAmortization() + model.getDepreciation())) - model.getFinancingCost() * model.getRevenue());
		model.addHistory(new ProjectHistory("Edited", sessionView.getUser(), UtilsFunctions.getChanges(model, old)));
		old = model;
		model = service.save(model);
	}

	public void updateEbida() {
		model.setEbida(model.getGrossMargin() - model.getRevenue() * (model.getDirectSalesCost() + model.getGlobalSalesCost() + model.getPresalesCost() + model.getMarketingCost()
				+ model.getHrCost() + model.getFinanceCost() + model.getItCost() + model.getAdminCost() + model.getTpsr()));
		updateNetMargin();
	}

	public void updateGrossMargin() {
		model.setGrossMargin(model.getRevenue() - (model.getCogs() * (1 + model.getRisk())));
		updateEbida();
	}

	// generic
	public List<Project> find() {
		return service.find();
	}

	// getters & setters
	public Project getModel() {
		return model;
	}

	public void setModel(Project model) {
		this.model = model;
	}

	public ProjectFile getFile() {
		return file;
	}

	public void setFile(ProjectFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public ProjectComment getComment() {
		return comment;
	}

	public void setComment(ProjectComment comment) {
		this.comment = comment;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CostcenterService getCostcenterService() {
		return costcenterService;
	}

	public void setCostcenterService(CostcenterService costcenterService) {
		this.costcenterService = costcenterService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public Boolean getEditTaskList() {
		return editTaskList;
	}

	public void setEditTaskList(Boolean editTaskList) {
		this.editTaskList = editTaskList;
	}

	public Boolean getEditManagerList() {
		return editManagerList;
	}

	public void setEditManagerList(Boolean editManagerList) {
		this.editManagerList = editManagerList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getEditMilestoneList() {
		return editMilestoneList;
	}

	public void setEditMilestoneList(Boolean editMilestoneList) {
		this.editMilestoneList = editMilestoneList;
	}

}
