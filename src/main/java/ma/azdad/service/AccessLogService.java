package ma.azdad.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import ma.azdad.model.AccessLog;
import ma.azdad.model.AccessLogType;
import ma.azdad.repos.AccessLogRepos;

@Component
public class AccessLogService extends GenericService<Integer,AccessLog, AccessLogRepos> {

	public List<AccessLog> findAllOrderByDateDesc() {
		return repos.findAllOrderByDateDesc();
	}
	
	public List<AccessLog> findLastByLogin(String login){
		return repos.findLastByLogin("%"+login+"%");
	}

	public void add(AccessLogType type, String message, String ip) {
		save(new AccessLog(new Date(), type, message, ip));
	}

	public void addInfo(String message, String ip) {
		add(AccessLogType.INFO, message, ip);
	}

	public void addSuccess(String message, String ip) {
		add(AccessLogType.SUCCESS, message, ip);
	}

	public void addWarning(String message, String ip) {
		add(AccessLogType.WARNING, message, ip);
	}

	public void addError(String message, String ip) {
		add(AccessLogType.ERROR, message, ip);
	}

}
