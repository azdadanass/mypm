package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.UserAppraisal;

@Repository
public interface SupplementaryGoalsRepos extends JpaRepository<SupplementaryGoals, Integer>{

	
	@Query("from SupplementaryGoals s where s.sections.userappraisal=?1 and s.sectionsData.goaldId=?2")
	List<SupplementaryGoals> findByUserAppraisal(UserAppraisal u, int id);
}
