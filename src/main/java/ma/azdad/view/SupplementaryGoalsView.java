package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.SupplementaryGoalsComment;
import ma.azdad.model.SupplementaryGoalsFile;
import ma.azdad.model.SupplementaryGoalsHistory;
import ma.azdad.repos.SupplementaryGoalsRepos;
import ma.azdad.service.SupplementaryGoalsService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class SupplementaryGoalsView extends GenericView<Integer, SupplementaryGoals, SupplementaryGoalsRepos, SupplementaryGoalsService>{

	
	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

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
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin();
	}

	@Override
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;

		filterBean(searchBean);
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
		model.addHistory(new SupplementaryGoalsHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(),
				getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));
	
		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
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

	public List<SupplementaryGoals> findAll() {
		return service.findAll();
	}

	// getters & setters
	public SupplementaryGoals getModel() {
		return model;
	}

	public void setModel(SupplementaryGoals model) {
		this.model = model;
	}


	// files
	private SupplementaryGoalsFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		SupplementaryGoalsFile modelFile = new SupplementaryGoalsFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (SupplementaryGoalsView.class) {
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

	public SupplementaryGoalsFile getFile() {
		return file;
	}

	public void setFile(SupplementaryGoalsFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// comments
	private SupplementaryGoalsComment comment = new SupplementaryGoalsComment();

	public Boolean canAddComment() {
		return sessionView.getIsInternalAdmin();
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	public SupplementaryGoalsComment getComment() {
		return comment;
	}

	public void setComment(SupplementaryGoalsComment comment) {
		this.comment = comment;
	}
}

