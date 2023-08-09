package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.ProjectPlanning;
import ma.azdad.repos.ProjectPlanningRepos;
import ma.azdad.service.ProjectPlanningService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class ProjectPlanningView extends GenericView<Integer, ProjectPlanning, ProjectPlanningRepos, ProjectPlanningService> {

	@Autowired
	ProjectView projectView;

	private Integer minYear;
	private Integer maxYear;
	private Integer selectedYear;
	private Integer minMonth;
	private Integer maxMonth;
	private List<Integer> yearList = new ArrayList<Integer>();
	private Boolean isEditPlanningMode = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		maxYear = service.getMaxYearByProject(id);
		if (maxYear == null)
			return;
		minYear = service.getMinYearByProject(id);
		for (int i = minYear; i <= maxYear; i++) {
			yearList.add(i);
		}
		selectedYear = minYear;
		refreshList();
		time();
	}

	@Override
	public void refreshList() {
		minMonth = service.getMinMonthByProjectAndMonth(id, selectedYear);
		maxMonth = service.getMaxMonthByProjectAndMonth(id, selectedYear);
		list2 = list1 = service.findProjectPlanningsByProjectAndYear(id, selectedYear);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return true;
		if (getIsEditPage() || getIsViewPage())
			return true;
		return false;
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

	public void onTabChange(TabChangeEvent event) {
		try {
			selectedYear = Integer.valueOf(event.getTab().getTitle());
			refreshProjectPlanningList();
			System.out.println("selectedYear\t" + selectedYear);
		} catch (Exception e) {
		}
	}

	public void refreshProjectPlanningList() {
		minMonth = service.getMinMonthByProjectAndMonth(projectView.getModel().getId(), selectedYear);
		maxMonth = service.getMaxMonthByProjectAndMonth(projectView.getModel().getId(), selectedYear);
		list1 = service.findProjectPlanningsByProjectAndYear(projectView.getModel().getId(), selectedYear);
	}

	public void saveProjectPlanningList() {
		ProjectPlanning current;
		Boolean hasChanged = false;
		for (ProjectPlanning projectPlanning : list1) {
			hasChanged = false;
			current = service.findOne(projectPlanning.getId());
			if (projectPlanning.getSales() != null && !projectPlanning.getSales().equals(current.getSales())) {
				current.setSales(projectPlanning.getSales());
				hasChanged = true;
			}
			if (projectPlanning.getRevenue() != null && !projectPlanning.getRevenue().equals(current.getRevenue())) {
				current.setRevenue(projectPlanning.getRevenue());
				hasChanged = true;
			}
			if (projectPlanning.getCost() != null && !projectPlanning.getCost().equals(current.getCost())) {
				current.setCost(projectPlanning.getCost());
				hasChanged = true;
			}
			if (projectPlanning.getCashIn() != null && !projectPlanning.getCashIn().equals(current.getCashIn())) {
				current.setCashIn(projectPlanning.getCashIn());
				hasChanged = true;
			}
			if (projectPlanning.getCashOut() != null && !projectPlanning.getCashOut().equals(current.getCashOut())) {
				current.setCashOut(projectPlanning.getCashOut());
				hasChanged = true;
			}
			if (hasChanged)
				service.save(current);
		}
		isEditPlanningMode = false;
		refreshProjectPlanningList();
	}

	public void refreshSelectedYear() {
		System.out.println("refreshSelectedYear ");
		Integer year = minYear;
		try {
			year = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("year"));
		} catch (Exception e) {
		}
		selectedYear = year;
		refreshProjectPlanningList();
	}

	public String generateProjectPlanning() {
		service.generateProjectPlanning(projectView.getModel().getId());
		return "viewProject.xhtml?faces-redirect=true&idproject=" + projectView.getModel().getId();
	}

	// getters & setters
	public ProjectPlanning getModel() {
		return model;
	}

	public void setModel(ProjectPlanning model) {
		this.model = model;
	}

	public ProjectView getProjectView() {
		return projectView;
	}

	public void setProjectView(ProjectView projectView) {
		this.projectView = projectView;
	}

	public Integer getMinYear() {
		return minYear;
	}

	public void setMinYear(Integer minYear) {
		this.minYear = minYear;
	}

	public Integer getMaxYear() {
		return maxYear;
	}

	public void setMaxYear(Integer maxYear) {
		this.maxYear = maxYear;
	}

	public Integer getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(Integer selectedYear) {
		this.selectedYear = selectedYear;
	}

	public Integer getMinMonth() {
		return minMonth;
	}

	public void setMinMonth(Integer minMonth) {
		this.minMonth = minMonth;
	}

	public Integer getMaxMonth() {
		return maxMonth;
	}

	public void setMaxMonth(Integer maxMonth) {
		this.maxMonth = maxMonth;
	}

	public List<Integer> getYearList() {
		return yearList;
	}

	public void setYearList(List<Integer> yearList) {
		this.yearList = yearList;
	}

	public Boolean getIsEditPlanningMode() {
		return isEditPlanningMode;
	}

	public void setIsEditPlanningMode(Boolean isEditPlanningMode) {
		this.isEditPlanningMode = isEditPlanningMode;
	}

}
