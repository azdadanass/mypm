package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;

@Repository
public interface AppraisalsRepos extends JpaRepository<Appraisals, Integer>{
	
	@Query("from User u where u.active=?1 and u.internal=?2  and u.affectation.hrManager=?3 and u.username in (select r.user.username from UserRole r where r.user.username=u.username and r.role='ROLE_MYPM')")
	List<User> findByHr(Boolean act,Boolean inter,User u3);
	
	@Query("from User u where u.active=?1 and u.internal=?2  and u.affectation.lineManager=?3 and u.username in (select r.user.username from UserRole r where r.user.username=u.username and r.role='ROLE_MYPM')")
	List<User> findByLineManager(Boolean act,Boolean inter,User u3);

	
	@Query("from UserAppraisal u where u.appraisee=?1 and u.appraisal=?2")
	List<UserAppraisal> findByAppraisalAndManager(User employe,Appraisals appraisal);
	
}
