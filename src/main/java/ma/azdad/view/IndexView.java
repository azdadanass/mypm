package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Role;

@ManagedBean
@Component
@Scope("view")
public class IndexView {

	@Autowired
	private SessionView sessionView;

	private Role role;

	@PostConstruct
	public void init() {
		if (sessionView.getIsMyPm())
			setRole(Role.ROLE_MYPM);
		if (sessionView.getIsMyPmHr())
			setRole(Role.ROLE_MYPM_HR);
		if (sessionView.getIsMyPmLineManager() || sessionView.getIsMyPmHr())
			setRole(Role.ROLE_MYPM_HR);
		if (sessionView.getIsMyPmLineManager())
			setRole(Role.ROLE_MYPM_LINE_MANAGER);
		
		
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		switch (role) {
		case ROLE_MYPM:
			sessionView.setMenu(1);
			break;
		case ROLE_MYPM_HR:
			sessionView.setMenu(2);
			break;
		case ROLE_MYPM_LINE_MANAGER:
			sessionView.setMenu(3);
			break;
		default:
			break;
		}
	}

}
