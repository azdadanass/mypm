package ma.azdad.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Company;
import ma.azdad.model.CompanyHistory;
import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;
import ma.azdad.repos.CompanyProfileRepos;
import ma.azdad.service.CompanyProfileService;
import ma.azdad.service.CompanyService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class CompanyProfileView extends GenericView<Integer, CompanyProfile, CompanyProfileRepos, CompanyProfileService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private CompanyView companyView;

	@Autowired
	private CompanyService companyService;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	public void updateModel(Integer id) {
		model = service.findOne(id);
	}

	public List<Profile> getProfileList() {
		System.out.println("getProfileList");
		if (model == null || model.getErp() == null)
			return null;
		switch (model.getErp()) {
		case "ibuy":
			return Arrays.asList(Profile.ASSISTANT, Profile.BUYER, Profile.PAYMENT_MANAGER, Profile.INVOICE_MANAGER, Profile.CFO, Profile.CASH_CONTROLLER, Profile.SYSTEM_ENGINEER);
		case "invoice":
			return Arrays.asList(Profile.ASSISTANT, Profile.BUYER, Profile.ACCOUNTANT, Profile.CFO, Profile.CASH_CONTROLLER);
		case "crm":
			return Arrays.asList(Profile.USER, Profile.PRESALES_OWNER, Profile.CFO, Profile.DELIVERY_MANAGER, Profile.CONTRACT_MANAGER, Profile.SALES);
		case "ifinance":
			return Arrays.asList(Profile.USER, Profile.BANK_STATEMENT, Profile.TAX_MANAGER, Profile.BANK_GUARANTEE, Profile.LETTER_OF_CREDIT, Profile.CFO);
		case "ilogistics":
			return Arrays.asList(Profile.CFO);
		default:
			return null;
		}
	}

	public List<Profile> getRemainingProfileList() {
		if (model == null || model.getErp() == null || model.getUser() == null)
			return null;
		List<Profile> userProfileList = service.findProfileListByCompanyAndErpAndUser(companyView.getId(), model.getErp(), model.getUserUsername());
		return getProfileList().stream().filter(i -> !userProfileList.contains(i)).collect(Collectors.toList());
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (companyView.getIsViewPage())
			initLists(service.findByCompany(companyView.getId()));
	}

	// save
	public void initModel() {
		model = new CompanyProfile();
		model.setCompany(companyView.getModel());
	}

	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public void save() {
		if (!canSave() || !validate())
			return;
		model = service.save(model);
		model = service.findOne(model.getId());
		if (companyView.getIsViewPage()) {
			Company company = companyService.findOne(model.getCompany().getId());
			company.addHistory(
					new CompanyHistory("Edited", sessionView.getUser(), "add profile : " + model.getUserFullName() + " (" + model.getErp() + " / " + model.getProfile() + ")"));
			companyService.save(company);
			companyView.refreshModel();
			refreshList();
		}

	}

	public Boolean validate() {
		if (service.countByErpAndProfileAndUserAndCompany(model.getErp(), model.getProfile(), model.getUserUsername(), model.getCompany().getId()) > 0)
			return FacesContextMessages.ErrorMessages("Already exists !");
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
			Company company = companyService.findOne(model.getCompany().getId());
			company.addHistory(new CompanyHistory("Edited", sessionView.getUser(), "delete profile : " + model.getUserFullName() + " (" + model.getErp() + " / " + model.getProfile() + ")"));
			company.removeProfile(model);
			companyService.save(company);
			evictCache();
			refreshList();
			companyView.refreshModel();
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

	// getters & setters
	public CompanyProfile getModel() {
		return model;
	}

	public void setModel(CompanyProfile model) {
		this.model = model;
	}

}
