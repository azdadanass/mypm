package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ProjectCatalog;

@Repository
public interface ProjectCatalogRepos extends JpaRepository<ProjectCatalog, Integer> {

	String constructor1 = "select new ProjectCatalog(a.id,a.name,a.active,a.description,a.startDate,a.endDate,a.countFiles,a.project.name,a.currency.name) ";

	@Query(constructor1 + "from ProjectCatalog a")
	public List<ProjectCatalog> find();
	
	@Query(constructor1 + "from ProjectCatalog a where a.active = ?1")
	public List<ProjectCatalog> findByActive(Boolean active);

	@Query("select count(*) from ProjectCatalog where name = ?1 and (?2 is null or id != ?2)")
	Long countByName(String name, Integer id);
	
	
	@Modifying
	@Query("update ProjectCatalogDetail a set a.customerPo = ?1 where a.projectCatalog.id in (select b.id from ProjectCatalog b where b.project.id = ?2)")
	void updateProjectCatalogDetailCustomerPoByProject(Boolean customerPo,Integer projectId);
}
