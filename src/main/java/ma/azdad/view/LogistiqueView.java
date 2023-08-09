package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Logistique;
import ma.azdad.repos.LogistiqueRepos;
import ma.azdad.service.LogistiqueService;
import ma.azdad.utils.FacesContextMessages;


@ManagedBean
@Component
@Scope("view")
public class LogistiqueView extends GenericView<Integer, Logistique, LogistiqueRepos, LogistiqueService>{

	
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
	
	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin();
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

		
	public List<Logistique> findAll() {
		return service.findAll();
	}
	// getters & setters
		public Logistique getModel() {
			return model;
		}

		public void setModel(Logistique model) {
			this.model = model;
		}
	
}
