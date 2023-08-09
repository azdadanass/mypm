package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Text;
import ma.azdad.repos.TextRepos;
import ma.azdad.service.TextService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class TextView extends GenericView<Integer, Text, TextRepos, TextService> {

	@Autowired
	private SessionView sessionView;

	@Value("${application}")
	private String application;

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
			initLists(service.find());
	}

	@Override
	protected void addPage() {
		super.addPage();
		model.setApp(application);
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
		return addParameters(listPage, "faces-redirect=true");
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return sessionView.getIsInternalAdmin();
	}

	public void delete() {
		if (!canDelete())
			return;
		try {
			service.delete(model.getId());
			refreshList();
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	// generic
	public List<String> findValueList(String beanName, String type) {
		return service.findValueList(beanName, type);
	}

	public List<String> findValueList(String beanName) {
		return service.findValueList(beanName);
	}

	// getters & setters
	public Text getModel() {
		return model;
	}

	public void setModel(Text model) {
		this.model = model;
	}

}
