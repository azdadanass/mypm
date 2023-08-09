package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Lob;

@Repository
public interface LobRepos extends JpaRepository<Lob, Integer> {

	@Query("select new Lob(id,name,description,type,countFiles,bu.name,bu.company.name,manager.fullName) from Lob")
	List<Lob> findLight();

	@Query("select new Lob(id,name,description,type,countFiles,bu.name,bu.company.name,manager.fullName) from Lob where bu.id = ?1")
	List<Lob> findLightByBu(Integer buId);

	@Query("select count(*) from Lob where name = ?1 and bu.id = ?2 and (?3 is null or id != ?3)")
	Long countByNameAndBu(String name, Integer buId, Integer id);

	@Query("select a.costcenter.lob.name from Project a where a.id = ?1")
	String findNameByProject(Integer projectId);
	
	@Query("select new Lob(id,name) from Lob where manager.username = ?1 ")
	List<Lob> findLightByManager(String managerUsername);
}

