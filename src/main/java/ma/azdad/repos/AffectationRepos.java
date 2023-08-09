package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Affectation;
import ma.azdad.model.User;

@Repository
public interface AffectationRepos extends JpaRepository<Affectation, Integer> {

	@Query("select user.username,lineManager.username from Affectation")
	public List<Object[]> getLineManagers();

	@Query("select a.user.username,b.lineManager.username from Affectation a,Affectation b where a.lineManager.username = b.user.username")
	public List<Object[]> getSuperLineManagers();

	@Query("select user.username,logisticManager.username from Affectation where logisticManager is not null")
	public List<Object[]> getLogisticManagers();

	@Query("select user.username,hrManager.username from Affectation where hrManager is not null")
	public List<Object[]> getHrManagers();

	@Query("select a.lineManager from Affectation a where a.user.username = ?1")
	User findLineManager(String username);

	@Query("select new User(a.user.username,a.user.fullName) from Affectation a where a.lineManager.username = ?1 and a.user.active = ?2")
	List<User> findLightByLineManager(String lineManagerUsername, Boolean active);

}
