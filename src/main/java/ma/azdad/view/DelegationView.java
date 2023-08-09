package ma.azdad.view;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Delegation;
import ma.azdad.model.DelegationDetail;
import ma.azdad.model.Lob;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectStatus;
import ma.azdad.model.User;
import ma.azdad.repos.DelegationRepos;
import ma.azdad.service.AffectationService;
import ma.azdad.service.DelegationService;
import ma.azdad.service.LobService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class DelegationView extends GenericView<Integer, Delegation, DelegationRepos, DelegationService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	ProjectService projectService;

	@Autowired
	LobService lobService;

	@Autowired
	AffectationService affectationService;

	@Autowired
	UserService userService;

	private DualListModel<Project> projectDualList = new DualListModel<Project>();
	private DualListModel<Lob> lobDualList = new DualListModel<Lob>();
	private DualListModel<User> userDualList = new DualListModel<User>();

	private Boolean delegate = true;
	private Boolean active = true;
	private Boolean showDetailList = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void addPage() {
		super.addPage();
		projectDualList.setSource(projectService.findLightByManagerAndStatus(sessionView.getUsername(), ProjectStatus.OPEN.getValue()));
		lobDualList.setSource(lobService.findLightByManager(sessionView.getUsername()));
		userDualList.setSource(affectationService.findLightByLineManager(sessionView.getUsername(), true));
	}

	@Override
	protected void editPage() {
		super.editPage();
		switch (model.getType()) {
		case PROJECT:
			projectDualList.setSource(projectService.findLightByManagerAndStatus(sessionView.getUsername(), ProjectStatus.OPEN.getValue()));
			projectDualList.setTarget(model.getDetailList().stream().map(i -> new Project(i.getProject().getId(), i.getProject().getName())).collect(Collectors.toList()));
			projectDualList.getSource().removeAll(projectDualList.getTarget());
			break;
		case LOB:
			lobDualList.setSource(lobService.findLightByManager(sessionView.getUsername()));
			lobDualList.setTarget(model.getDetailList().stream().map(i -> new Lob(i.getLob().getId(), i.getLob().getName())).collect(Collectors.toList()));
			lobDualList.getSource().removeAll(lobDualList.getTarget());
			break;
		case USER:
			userDualList.setSource(affectationService.findLightByLineManager(sessionView.getUsername(), true));
			userDualList.setTarget(model.getDetailList().stream().map(i -> new User(i.getUser().getUsername(), i.getUser().getFullName())).collect(Collectors.toList()));
			userDualList.getSource().removeAll(userDualList.getTarget());
			break;

		default:
			break;
		}

	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.find(sessionView.getUsername(), delegate, active));
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin() || sessionView.getIsUser();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return sessionView.getIsUser();
		if (getIsEditPage() || getIsViewPage())
			return sessionView.isTheConnectedUser(model.getDelegator());
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;
		model.calculateStatus();
		model.setDelegator(sessionView.getUser());
		model.setDelegate(userService.findOneLight(model.getDelegate().getUsername()));
		generateDetailList();

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than End Date ");

		switch (model.getType()) {
		case PROJECT:
			if (projectDualList.getTarget().isEmpty())
				return FacesContextMessages.ErrorMessages("You should select at least one project ");
			break;
		case LOB:
			if (lobDualList.getTarget().isEmpty())
				return FacesContextMessages.ErrorMessages("You should select at least one lob ");
			break;
		case USER:
			if (userDualList.getTarget().isEmpty())
				return FacesContextMessages.ErrorMessages("You should select at least one user ");
			break;

		default:
			break;
		}

		return true;
	}

	private void generateDetailList() {
		model.removeDetailList();
		switch (model.getType()) {
		case PROJECT:
			projectDualList.getTarget().forEach(i -> model.addDetail(new DelegationDetail(projectService.findOneLight(i.getId()))));
			break;
		case LOB:
			lobDualList.getTarget().forEach(i -> model.addDetail(new DelegationDetail(lobService.findOneLight(i.getId()))));
			break;
		case USER:
			userDualList.getTarget().forEach(i -> model.addDetail(new DelegationDetail(userService.findOneLight(i.getUsername()))));
			break;
		default:
			break;
		}

	}

	// delete
	public Boolean canDelete() {
		return sessionView.isTheConnectedUser(model.getDelegator());
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

//	// details
//	public Boolean canAddDetail() {
//		return true;
//	}
//
//	public void addDetail() {
//		if (canAddDetail())
//			model.addDetail(new DelegationDetail());
//	}
//
//	public Boolean canDeleteDetail() {
//		return true;
//	}
//
//	public void deleteDetail(DelegationDetail detail) {
//		if (canDeleteDetail())
//			model.removeDetail(detail);
//	}

	// getters & setters
	public Delegation getModel() {
		return model;
	}

	public void setModel(Delegation model) {
		this.model = model;
	}

	public DualListModel<Project> getProjectDualList() {
		return projectDualList;
	}

	public void setProjectDualList(DualListModel<Project> projectDualList) {
		this.projectDualList = projectDualList;
	}

	public DualListModel<User> getUserDualList() {
		return userDualList;
	}

	public void setUserDualList(DualListModel<User> userDualList) {
		this.userDualList = userDualList;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public AffectationService getAffectationService() {
		return affectationService;
	}

	public void setAffectationService(AffectationService affectationService) {
		this.affectationService = affectationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Boolean getDelegate() {
		return delegate;
	}

	public void setDelegate(Boolean delegate) {
		this.delegate = delegate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getShowDetailList() {
		return showDetailList;
	}

	public void setShowDetailList(Boolean showDetailList) {
		this.showDetailList = showDetailList;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public DualListModel<Lob> getLobDualList() {
		return lobDualList;
	}

	public void setLobDualList(DualListModel<Lob> lobDualList) {
		this.lobDualList = lobDualList;
	}

}
