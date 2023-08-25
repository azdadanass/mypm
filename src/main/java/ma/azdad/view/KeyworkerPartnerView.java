package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.KeyworkerPartner;
import ma.azdad.repos.KeyworkerPartnerRepos;
import ma.azdad.service.KeyworkerPartnerService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class KeyworkerPartnerView
		extends GenericView<Integer, KeyworkerPartner, KeyworkerPartnerRepos, KeyworkerPartnerService> {

	@Autowired
	private SessionView sessionView;

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

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

//			model.addHistory(new SectionsHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(),
//					getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

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

	public List<KeyworkerPartner> findAll() {
		return service.findAll();
	}

	// getters & setters
	public KeyworkerPartner getModel() {
		return model;
	}

	public void setModel(KeyworkerPartner model) {
		this.model = model;
	}
}
