package ma.azdad.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.MonthYear;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectPlanning;
import ma.azdad.repos.ProjectPlanningRepos;
import ma.azdad.repos.ProjectRepos;

@Component
public class ProjectPlanningService extends GenericService<Integer,ProjectPlanning, ProjectPlanningRepos> {

	@Autowired
	ProjectRepos projectRepos;

	@Override
	@Cacheable("projectPlanningService.findAll")
	public List<ProjectPlanning> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("projectPlanningService.findOne")
	public ProjectPlanning findOne(Integer id) {
		ProjectPlanning projectPlanning = super.findOne(id);

		return projectPlanning;
	}

	public void generateProjectPlanning(Integer projectId) {
		Project project = projectRepos.findById(projectId).get();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(project.getStartDate());
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Set<MonthYear> existingProjectPlanningSet = repos.findMonthYearByProject(projectId);

		Set<MonthYear> newPlanning = new HashSet<MonthYear>();

		Integer month, year;

		while (calendar.getTime().compareTo(project.getEndDate()) <= 0) {
			month = calendar.get(Calendar.MONTH) + 1;
			year = calendar.get(Calendar.YEAR);
			if (!existingProjectPlanningSet.contains(new MonthYear(month, year)))
				repos.save(new ProjectPlanning(month, year, 0.0, 0.0, 0.0, 0.0, 0.0, project));
			newPlanning.add(new MonthYear(month, year));
			calendar.add(Calendar.MONTH, 1);
		}
		// FIND TO DELETE SET
		existingProjectPlanningSet.removeAll(newPlanning);

		for (MonthYear monthYear : existingProjectPlanningSet) {
			repos.delete(repos.findByProjectAndMonthAndYear(project.getId(), monthYear.getMonth(), monthYear.getYear()));
		}
	}

	public List<ProjectPlanning> findByProject(Integer projectId) {
		return repos.findByProject(projectId);
	}

	public Integer getMaxYearByProject(Integer projectId) {
		return repos.getMaxYearByProject(projectId);
	}

	public Integer getMinYearByProject(Integer projectId) {
		return repos.getMinYearByProject(projectId);
	}

	public Integer getMaxMonthByProjectAndMonth(Integer projectId, Integer year) {
		return repos.getMaxMonthByProjectAndMonth(projectId, year);
	}

	public Integer getMinMonthByProjectAndMonth(Integer projectId, Integer year) {
		return repos.getMinMonthByProjectAndMonth(projectId, year);
	}

	public List<ProjectPlanning> findProjectPlanningsByProjectAndYear(Integer projectId, Integer year) {
		return repos.findProjectPlanningsByProjectAndYear(projectId, year);
	}

}
