package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectCatalog;
import ma.azdad.model.ProjectCatalogComment;
import ma.azdad.model.ProjectCatalogDetail;
import ma.azdad.model.ProjectCatalogFile;
import ma.azdad.model.ProjectCatalogHistory;
import ma.azdad.model.Supplier;
import ma.azdad.repos.ProjectCatalogRepos;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.ProjectCatalogService;
import ma.azdad.service.ProjectMilestoneService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.RestTemplateService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.App;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class ProjectCatalogView extends GenericView<Integer, ProjectCatalog, ProjectCatalogRepos, ProjectCatalogService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProjectMilestoneService projectMilestoneService;

	@Autowired
	private RestTemplateService restTemplateService;

	private Integer supplierId;

	private DualListModel<Supplier> supplierDualList;

	private Boolean active = true;

	@Override
	@PostConstruct
	public void init() {
		super.init();

		time();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin() || sessionView.isCfo();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = service.find(active);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		supplierId = UtilsFunctions.getIntegerParameter("supplierId");
	}

	@Override
	public String evictCache() {
		super.evictCache();

		return addParameters(listPage, "faces-redirect=true", "supplierId" + supplierId);
	}

	// save
	public Boolean canSave() {
		if (isAddPage || isListPage)
			return sessionView.getIsInternalAdmin();
		if (isEditPage || isViewPage)
			return sessionView.getIsInternalAdmin();
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true", "supplierId" + supplierId);
		if (!validate())
			return null;

		model.setProject(projectService.findOne(model.getProjectId()));
		model.setCurrency(currencyService.findOne(model.getCurrencyId()));
		model.setEndCustomer(customerService.findOne(model.getEndCustomerId()));
		model.addHistory(new ProjectCatalogHistory(isAddPage ? "Created" : "Edited", sessionView.getUser(), isAddPage ? null : UtilsFunctions.getChanges(model, old)));

		for (ProjectCatalogDetail cd : model.getDetailList()) {
			if (cd.getProjectMilestoneId() != null)
				cd.setProjectMilestone(projectMilestoneService.findOneLight(cd.getProjectMilestoneId()));
			else
				cd.setProjectMilestone(null);

		}

		model = service.save(model);

		restTemplateService.consumRestAsync(App.SDM.getLink() + "/rest/updateProjectMilestoneInPlanAndJobRequestScript", String.class);

		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByName(model) > 0)
			return FacesContextMessages.ErrorMessages("Name already exits on database");

		return true;
	}

	public Boolean canEditCurrency() {
		// TODO
		return isAddPage;
	}

	public void changeProjectListener() {
		if (model.getProjectId() != null)
			model.setProject(projectService.findOneLight(model.getProjectId()));
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.getIsInternalAdmin();
	}

	public void toggle() {
		if (!canToggle())
			return;
		model = service.findOne(model.getId());
		model.setActive(!model.getActive());
		model = service.saveAndRefresh(model);

		System.out.println(model.getActive());
		refreshList();
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
		return addParameters(listPage, "faces-redirect=true", "supplierId" + supplierId);
	}

	// details
	public Boolean canAddDetail() {
		return sessionView.getIsInternalAdmin();
	}

	public void addDetail() {
		if (canAddDetail())
			model.addDetail(new ProjectCatalogDetail());
	}

	public Boolean canDeleteDetail() {
		return true;
	}

	public void deleteDetail(ProjectCatalogDetail detail) {
		if (canDeleteDetail())
			model.removeDetail(detail);
	}

	public void changeSubcontractingListener(ProjectCatalogDetail detail) {
		if (!detail.getSubcontracting())
			detail.setSupplierPo(false);
	}

	// files
	private ProjectCatalogFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public Boolean validateAddFile() {
		return model.getFileList().stream().filter(i -> fileType.equals(i.getType())).count() == 0;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		if (!validateAddFile()) {
			FacesContextMessages.ErrorMessages("File Type (" + fileType + ") already added !");
			return;
		}
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		ProjectCatalogFile modelFile = new ProjectCatalogFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (ProjectCatalogView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return canAddFile();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// comments
	private ProjectCatalogComment comment = new ProjectCatalogComment();

	public Boolean canAddComment() {
		return canAccess();
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	// generic

	// getters & setters
	public ProjectCatalog getModel() {
		return model;
	}

	public void setModel(ProjectCatalog model) {
		this.model = model;
	}

	public ProjectCatalogFile getFile() {
		return file;
	}

	public void setFile(ProjectCatalogFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public ProjectCatalogComment getComment() {
		return comment;
	}

	public void setComment(ProjectCatalogComment comment) {
		this.comment = comment;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public DualListModel<Supplier> getSupplierDualList() {
		return supplierDualList;
	}

	public void setSupplierDualList(DualListModel<Supplier> supplierDualList) {
		this.supplierDualList = supplierDualList;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
