package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.AccessLog;
import ma.azdad.repos.AccessLogRepos;
import ma.azdad.service.AccessLogService;

@ManagedBean
@Component
@Scope("view")
public class AccessLogView extends GenericView<Integer, AccessLog, AccessLogRepos, AccessLogService> {

	@Autowired
	private UserView userView;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findAllOrderByDateDesc());
		if (userView.getIsViewPage()) {

			initLists(service.findLastByLogin(userView.getModel().getLogin()));
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	// getters & setters
	public AccessLog getModel() {
		return model;
	}

	public void setModel(AccessLog model) {
		this.model = model;
	}

}
