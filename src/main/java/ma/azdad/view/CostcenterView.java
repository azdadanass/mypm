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

import ma.azdad.model.Costcenter;
import ma.azdad.model.CostcenterFile;
import ma.azdad.model.CostcenterHistory;
import ma.azdad.repos.CostcenterRepos;
import ma.azdad.service.CostcenterService;
import ma.azdad.service.LobService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class CostcenterView extends GenericView<Integer, Costcenter, CostcenterRepos, CostcenterService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	LobService lobService;

	@Autowired
	UserService userService;

	private Integer lobId;

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
		lobId = UtilsFunctions.getIntegerParameter("lobId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findLight());
		else if (isPage("viewLob"))
			initLists(service.findLightByLob(id));
	}

	@Override
	protected void addPage() {
		super.addPage();
		if (lobId != null)
			model.setLob(lobService.findOneLight(lobId));
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

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.setLob(lobService.findOneLight(model.getLobId()));
		model.addHistory(new CostcenterHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));


		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByYearAndLob(model) > 0)
			return FacesContextMessages.ErrorMessages("Year should be unique per LOB");
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
	private CostcenterFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		CostcenterFile modelFile = new CostcenterFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (CostcenterView.class) {
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
	@Cacheable("costcenterView.findLight")
	public List<Costcenter> findLight() {
		return service.findLight();
	}

	// getters & setters
	public Costcenter getModel() {
		return model;
	}

	public void setModel(Costcenter model) {
		this.model = model;
	}

	public CostcenterFile getFile() {
		return file;
	}

	public void setFile(CostcenterFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getLobId() {
		return lobId;
	}

	public void setLobId(Integer lobId) {
		this.lobId = lobId;
	}

}
