package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.User;

@Repository
public interface AppraisalsRepos extends JpaRepository<Appraisals, Integer>{
	
	@Query("from User  u where u.active=?1 and u.internal=?2  and u.user=?3 and u.username in (select r.user.username from UserRole r where r.user.username=u.username and r.role='ROLE_MYPM')")
	List<User> findByHr(Boolean act,Boolean inter,User u3);
	
}
