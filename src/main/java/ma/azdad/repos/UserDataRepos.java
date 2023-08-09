package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.UserData;

@Repository
public interface UserDataRepos extends JpaRepository<UserData, Integer> {

	@Modifying
	@Query("update UserData set id = ?2 where user.username = ?1")
	void updateId(String username, Integer id);

	@Modifying
	@Query("delete from  UserData where user.username = ?1")
	void deleteByUsername(String username);

}
