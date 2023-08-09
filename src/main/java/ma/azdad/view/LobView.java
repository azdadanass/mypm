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

import ma.azdad.model.Lob;
import ma.azdad.model.LobFile;
import ma.azdad.model.LobHistory;
import ma.azdad.repos.LobRepos;
import ma.azdad.service.BuService;
import ma.azdad.service.LobService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class LobView extends GenericView<Integer, Lob, LobRepos, LobService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	BuService buService;

	@Autowired
	UserService userService;

	private Integer buId;

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
		buId = UtilsFunctions.getIntegerParameter("buId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findLight());
		else if (isPage("viewBu"))
			initLists(service.findLightByBu(id));
	}

	@Override
	protected void addPage() {
		super.addPage();
		if (buId != null)
			model.setBu(buService.findOneLight(buId));
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

		model.setBu(buService.findOneLight(model.getBuId()));
		model.setManager(userService.findOneLight(model.getManagerUsername()));
		model.addHistory(new LobHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));
		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByNameAndBu(model) > 0)
			return FacesContextMessages.ErrorMessages("Name should be unique per BU");
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
	private LobFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		LobFile modelFile = new LobFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (LobView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public void deleteFile() {
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// generic
	@Cacheable("lobView.findLight")
	public List<Lob> findLight() {
		return service.findLight();
	}

	public String findNameByProject(Integer projectId) {
		return service.findNameByProject(projectId);
	}

	// getters & setters
	public Lob getModel() {
		return model;
	}

	public void setModel(Lob model) {
		this.model = model;
	}

	public LobFile getFile() {
		return file;
	}

	public void setFile(LobFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Integer getBuId() {
		return buId;
	}

	public void setBuId(Integer buId) {
		this.buId = buId;
	}

}
