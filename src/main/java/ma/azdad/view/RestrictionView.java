package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Restriction;
import ma.azdad.model.RestrictionComment;
import ma.azdad.model.RestrictionFile;
import ma.azdad.model.RestrictionHistory;
import ma.azdad.repos.RestrictionRepos;
import ma.azdad.service.RestrictionService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class RestrictionView extends GenericView<Integer, Restriction, RestrictionRepos, RestrictionService> {

	@Autowired
	private SessionView sessionView;

	private Boolean active = true;

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

	@Override
	public void refreshList() {
		if (isListPage)
			if (pageIndex == null)
				initLists(service.findLightByUser(active, sessionView.getUsername()));
			else
				switch (pageIndex) {
				case 1:
					initLists(service.findLight(active));
					break;
				}
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

		model.addHistory(new RestrictionHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (model.getEndDate() != null && model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than end date");
		return true;
	}

	// resolve
	public Boolean canResolve() {
		return sessionView.getIsInternalAdmin() && model.getIsActive();
	}

	public void resolve() {
		if (!canResolve())
			return;
		model.setResolutionDate(new Date());
		model.setEndDate(new Date());
		model = service.saveAndRefresh(model);
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
	private RestrictionFile file;
	private String fileType;

	public Boolean canAddFile() {
		return true;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		RestrictionFile modelFile = new RestrictionFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (RestrictionView.class) {
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
	private RestrictionComment comment = new RestrictionComment();

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

	// getters & setters
	public Restriction getModel() {
		return model;
	}

	public void setModel(Restriction model) {
		this.model = model;
	}

	public RestrictionFile getFile() {
		return file;
	}

	public void setFile(RestrictionFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public RestrictionComment getComment() {
		return comment;
	}

	public void setComment(RestrictionComment comment) {
		this.comment = comment;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
