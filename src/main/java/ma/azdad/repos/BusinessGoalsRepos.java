package ma.azdad.repos;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;

@Repository
public interface BusinessGoalsRepos extends JpaRepository<BusinessGoals, Integer>{

	
	@Query("from Sections  s where s.sectionsNumber=0 and s.userappraisal.id=?1")
	Sections findSectionId(Integer userappraisal);
	
	@Query("from BusinessGoals b where b.sections=?1")
	List<BusinessGoals> findBySections(Sections sections);
}
