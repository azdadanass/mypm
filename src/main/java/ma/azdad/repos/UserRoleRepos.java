package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Role;
import ma.azdad.model.UserRole;

@Repository
public interface UserRoleRepos extends JpaRepository<UserRole, Integer> {

	@Query("select count(*) from UserRole where user.id = ?1 and role = ?2")
	public Long count(String username, Role role);

}
