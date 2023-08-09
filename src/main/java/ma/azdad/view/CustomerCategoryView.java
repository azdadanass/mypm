package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.CustomerCategory;
import ma.azdad.repos.CustomerCategoryRepos;
import ma.azdad.service.CustomerCategoryService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class CustomerCategoryView extends GenericView<Integer, CustomerCategory, CustomerCategoryRepos, CustomerCategoryService> {

	@Autowired
	private SessionView sessionView;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = service.findAll();
		else if (currentPath.equals("/customerList.xhtml")) {
			list2 = list1 = new ArrayList<>(service.find());
			list1.add(new CustomerCategory(null, "All", "customerCategory/all.jpg"));
		}

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

	// photos
	public Boolean canUploadPhoto() {
		return sessionView.getIsInternalAdmin();
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handlePhotoUpload(event, getClassName2(), 400 * 1024);
		model.setPhoto("files/" + getClassName2() + "/" + file.getName());
		synchronized (CustomerCategoryView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// generic
	public List<CustomerCategory> findAll() {
		return service.findAll();
	}

	public List<CustomerCategory> find() {
		return service.find();
	}

	// getters & setters
	public CustomerCategory getModel() {
		return model;
	}

	public void setModel(CustomerCategory model) {
		this.model = model;
	}

}
