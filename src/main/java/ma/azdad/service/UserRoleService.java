package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Role;
import ma.azdad.repos.UserRoleRepos;

@Component
@Transactional
public class UserRoleService {

	@Autowired
	private UserRoleRepos userRoleRepos;

	public Boolean isHavingRole(String username, Role role) {
		Long l = userRoleRepos.count(username, role);
		return l != null && l > 0;
	}

}
