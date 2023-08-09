package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Costcenter;

@Repository
public interface CostcenterRepos extends JpaRepository<Costcenter, Integer> {

	String c1 = "select new Costcenter(id,year,budget,status,countFiles,lob.name,lob.bu.company.name,currency.name) ";

	@Query(c1 + "from Costcenter")
	List<Costcenter> findLight();

	@Query(c1 + "from Costcenter where lob.id = ?1")
	List<Costcenter> findLightByLob(Integer lobId);

	@Query("select count(*) from Costcenter where year = ?1 and lob.id = ?2 and (?3 is null or id != ?3)")
	Long countByYearAndLob(Integer year, Integer lobId, Integer id);

}
