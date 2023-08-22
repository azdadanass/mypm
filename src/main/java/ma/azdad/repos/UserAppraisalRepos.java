package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;

@Repository
public interface UserAppraisalRepos extends JpaRepository<UserAppraisal, Integer> {

	List<UserAppraisal> findByEmployOrAppraisee(User employ, User apraisee);

	@Query("from UserAppraisal u where u.employ=?1")
	List<UserAppraisal> findUserAppraisalByUser(User employe);

	@Query("from UserAppraisal u where u.appraisee=?1")
	List<UserAppraisal> findAppraisalByManager(User employe);
	
	@Query("from UserAppraisal u where u.appraisal=?1")
	List<UserAppraisal> findUserAppraisalByAppraisal(Appraisals appraisal);

	@Query("from Sections  s where s.userappraisal=?1 and s.eligible='true' ")
	List<Sections> findSectionByEligible(UserAppraisal ua);

	@Query("from SectionsData sd where sd.goaldId=?1 ")
	List<SectionsData> findSectionDataByGoalId(Integer goaldid);

	@Query("from Sections  s where s.userappraisal=?1 ")
	List<Sections> findSectionByUserAppraisal(UserAppraisal userappraisal);

	@Query("from Sections  s where s.userappraisal=?1 and s.sectionsNumber=?2 ")
	Sections findSectionByUserAppraisalAndNumber(UserAppraisal userappraisal, Integer number);
		
	@Query("from UserAppraisal u where u.employ.active=?1 and u.employ.internal=?2  and u.employ.affectation.hrManager=?3")
	List<UserAppraisal> findUserAppraisalByHR(Boolean act,Boolean inter,User u3);
	
	@Query("from UserAppraisal u where u.employ.active=?1 and u.employ.internal=?2  and u.employ.affectation.lineManager=?3")
	List<UserAppraisal> findUserAppraisalByLM(Boolean act,Boolean inter,User u3);
	
	@Query("from BusinessGoals b where b.sections.sectionsNumber=0 and b.sections.userappraisal=?1")
	List<BusinessGoals> findBusinessGoalsBySection0(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sectionsData.goaldId=1 and s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppBySection1(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sectionsData.goaldId=2 and s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppBySection2(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sectionsData.goaldId=3 and s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppBySection3(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sectionsData.goaldId=4 and s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppBySection4(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sectionsData.goaldId=5 and s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppBySection5(UserAppraisal u);
	
	@Query("from SupplementaryGoals s where s.sections.sectionsNumber=?1 and s.sections.userappraisal=?2")
	List<SupplementaryGoals> findSuppBySection(int number,UserAppraisal u);
	
	@Query("from SupplementaryGoals s where  s.sections.userappraisal=?1")
	List<SupplementaryGoals> findSuppByUser(UserAppraisal u);
	
	
	/*
	 * //not used
	 * 
	 * @Query("from SupplementaryGoals s where s.sectionsData.goaldId=?1")
	 * List<SupplementaryGoals> findSupplementaryByGoaldId(Integer goaldid);
	 */

}
