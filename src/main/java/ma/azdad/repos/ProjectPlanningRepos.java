package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.azdad.model.MonthYear;
import ma.azdad.model.ProjectPlanning;

@Repository
public interface ProjectPlanningRepos extends JpaRepository<ProjectPlanning, Integer> {

	@Query("select new ma.azdad.model.MonthYear(month,year) from ProjectPlanning where project.id = :projectId")
	public Set<MonthYear> findMonthYearByProject(@Param("projectId") Integer projectId);

	@Query("from ProjectPlanning where project.id = :projectId")
	public List<ProjectPlanning> findByProject(@Param("projectId") Integer projectId);

	@Query("from ProjectPlanning where project.id = :projectId and month = :month and year = :year")
	public ProjectPlanning findByProjectAndMonthAndYear(@Param("projectId") Integer projectId, @Param("month") Integer month, @Param("year") Integer year);

	@Query("select new ProjectPlanning( a.id, a.month, a.year, a.sales, a.revenue, a.cost, a.cashIn, a.cashOut,(select sum(b.cashIn-b.cashOut) from ProjectPlanning b where b.project.id = :projectId and STR_TO_DATE(concat(concat(concat(concat(b.year,'-'),b.month),'-'),1),'%Y-%m-%d') <= STR_TO_DATE(concat(concat(concat(concat(a.year,'-'),a.month),'-'),1),'%Y-%m-%d')), project.id,project.name) from ProjectPlanning a where a.project.id = :projectId and a.year = :year order by a.month")
	public List<ProjectPlanning> findProjectPlanningsByProjectAndYear(@Param("projectId") Integer projectId, @Param("year") Integer year);

	@Query("select max(year) from ProjectPlanning where project.id = :projectId")
	public Integer getMaxYearByProject(@Param("projectId") Integer projectId);

	@Query("select min(year) from ProjectPlanning where project.id = :projectId")
	public Integer getMinYearByProject(@Param("projectId") Integer projectId);

	@Query("select max(month) from ProjectPlanning where project.id = :projectId and year = :year")
	public Integer getMaxMonthByProjectAndMonth(@Param("projectId") Integer projectId, @Param("year") Integer year);

	@Query("select min(month) from ProjectPlanning where project.id = :projectId and year = :year")
	public Integer getMinMonthByProjectAndMonth(@Param("projectId") Integer projectId, @Param("year") Integer year);

}
