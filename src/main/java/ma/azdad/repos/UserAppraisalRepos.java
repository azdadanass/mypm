package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;

@Repository
public interface UserAppraisalRepos extends JpaRepository<UserAppraisal, Integer>{

	
	List<UserAppraisal> findByEmployOrAppraisee(User employ,User apraisee);
	
	@Query("from UserAppraisal  u where u.employ=?1")
	List<UserAppraisal> findByAppraisal( User employe);
	
	@Query("from Sections  s where s.userappraisal=?1 and s.eligible='true' ")
	List<Sections> findSectionByEligible(UserAppraisal ua);
	
	@Query("from SectionsData sd where sd.goaldId=?1 ")
	List<SectionsData> findSectionDataByGoalId(Integer goaldid);
	
	@Query("from Sections  s where s.userappraisal=?1 ")
	List<Sections> findSectionByUserAppraisal(UserAppraisal userappraisal);

	@Query("from Sections  s where s.userappraisal=?1 and s.sectionsNumber=?2 ")
	Sections findSectionByUserAppraisalAndNumber(UserAppraisal userappraisal,Integer number);
	
	/*
	 * //not used
	 * 
	 * @Query("from SupplementaryGoals s where s.sectionsData.goaldId=?1")
	 * List<SupplementaryGoals> findSupplementaryByGoaldId(Integer goaldid);
	 */
	
}
