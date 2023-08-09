package ma.azdad.repos;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;

@Repository
public interface BusinessGoalsRepos extends JpaRepository<BusinessGoals, Integer>{

	
	@Query("from Sections  s where s.sectionsNumber=0 and s.userappraisal.id=?1")
	Sections findSectionId(Integer userappraisal);
}
