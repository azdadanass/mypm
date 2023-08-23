package ma.azdad.repos;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.UserAppraisal;

@Repository
public interface BusinessGoalsRepos extends JpaRepository<BusinessGoals, Integer>{

	
	@Query("from Sections  s where s.sectionsNumber=0 and s.userappraisal.id=?1")
	Sections findSectionId(Integer userappraisal);
	
	@Query("from BusinessGoals b where b.sections=?1")
	List<BusinessGoals> findBySections(Sections sections);
	
	@Query("from Sections s where s.sectionsNumber=?1 and s.userappraisal=?2")
	Sections findSectionByNumberAndUserAppraisal(Integer number,UserAppraisal us);
	
	@Query("from BusinessGoals b where b.sections.userappraisal=?1")
	List<BusinessGoals> findBySectionsUserAppraisal(UserAppraisal us);
}
