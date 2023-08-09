package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Bu;
import ma.azdad.model.BuFile;
import ma.azdad.model.BuHistory;
import ma.azdad.repos.BuRepos;
import ma.azdad.service.BuService;
import ma.azdad.service.CompanyService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class BuView extends GenericView<Integer, Bu, BuRepos, BuService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	CompanyService companyService;

	@Autowired
	UserService userService;

	private Integer companyId;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		companyId = UtilsFunctions.getIntegerParameter("companyId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findLight());
		else if (isPage("viewCompany"))
			initLists(service.findLightByCompany(id));
	}

	@Override
	protected void addPage() {
		super.addPage();
		if (companyId != null)
			model.setCompany(companyService.findOne(companyId));

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

		model.setCompany(companyService.findOne(model.getCompanyId()));
		model.setDirector(userService.findOne(model.getDirectorUsername()));
		model.setOperationsDirector(userService.findOne(model.getOperationsDirectorUsername()));
		model.setFinanceManager(userService.findOne(model.getFinanceManagerUsername()));
		model.setTechnicalDirector(userService.findOne(model.getTechnicalDirectorUsername()));
		model.setHrDirector(userService.findOne(model.getHrDirectorUsername()));
		model.addHistory(new BuHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByNameAndCompany(model) > 0)
			return FacesContextMessages.ErrorMessages("Name should be unique per company");
		return true;
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

	// files
	private BuFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		BuFile modelFile = new BuFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (BuView.class) {
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

	// generic
	@Cacheable("buView.findLight")
	public List<Bu> findLight() {
		return service.findLight();
	}

	// getters & setters
	public Bu getModel() {
		return model;
	}

	public void setModel(Bu model) {
		this.model = model;
	}

	public BuFile getFile() {
		return file;
	}

	public void setFile(BuFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
