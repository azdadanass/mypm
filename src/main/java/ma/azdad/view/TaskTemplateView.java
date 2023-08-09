package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.TaskTemplate;
import ma.azdad.model.TaskTemplateDetail;
import ma.azdad.repos.TaskTemplateRepos;
import ma.azdad.service.TaskTemplateService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class TaskTemplateView extends GenericView<Integer, TaskTemplate, TaskTemplateRepos, TaskTemplateService> {

	@Autowired
	private SessionView sessionView;

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

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
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

	// details
	public Boolean canAddDetail() {
		return sessionView.getIsInternalAdmin();
	}

	public void addDetail() {
		if (canAddDetail())
			model.addDetail(new TaskTemplateDetail());
	}

	public Boolean canDeleteDetail() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteDetail(TaskTemplateDetail detail) {
		if (canDeleteDetail())
			model.removeDetail(detail);
	}

	// generic
	public List<TaskTemplate> findAll() {
		return service.findAll();
	}

	// getters & setters
	public TaskTemplate getModel() {
		return model;
	}

	public void setModel(TaskTemplate model) {
		this.model = model;
	}

}
