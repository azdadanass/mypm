package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;

@Repository
public interface UserAppraisalRepos extends JpaRepository<UserAppraisal, Integer>{

	
	List<UserAppraisal> findByEmployOrAppraisee(User employ,User apraisee);
}
