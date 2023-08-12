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

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.BusinessGoalsComment;
import ma.azdad.model.BusinessGoalsFile;
import ma.azdad.model.BusinessGoalsHistory;
import ma.azdad.repos.BusinessGoalsRepos;
import ma.azdad.service.BusinessGoalsService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class BusinessGoalsView extends GenericView<Integer, BusinessGoals, BusinessGoalsRepos, BusinessGoalsService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

	@Autowired
	BusinessGoalsRepos businessGoalsRepos;

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
		return sessionView.getIsMyPm();
	}

	@Override
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;

		filterBean(searchBean);
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsMyPm();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.addHistory(new BusinessGoalsHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(),
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

	public List<BusinessGoals> findAll() {
		return service.findAll();
	}

	// files
	private BusinessGoalsFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		BusinessGoalsFile modelFile = new BusinessGoalsFile(file, fileType, event.getFile().getFileName(),
				sessionView.getUser());
		model.addFile(modelFile);
		synchronized (BusinessGoalsView.class) {
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

	public BusinessGoalsFile getFile() {
		return file;
	}

	public void setFile(BusinessGoalsFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// comments
	private BusinessGoalsComment comment = new BusinessGoalsComment();

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

	public BusinessGoalsComment getComment() {
		return comment;
	}

	public void setComment(BusinessGoalsComment comment) {
		this.comment = comment;
	}

	// getters & setters
	public BusinessGoals getModel() {
		return model;
	}

	public void setModel(BusinessGoals model) {
		this.model = model;
	}

}
