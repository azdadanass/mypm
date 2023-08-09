package ma.azdad.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Affectation;
import ma.azdad.model.User;
import ma.azdad.repos.AffectationRepos;
import ma.azdad.utils.DataTypes;

@Component
@Transactional
public class AffectationService extends GenericService<Integer,Affectation, AffectationRepos> {

	public Map<String, String> getDatas(String type) {
		List<Object[]> objects = new ArrayList<Object[]>();
		if (DataTypes.LM.getValue().equals(type))
			objects = repos.getLineManagers();
		else if (DataTypes.SLM.getValue().equals(type))
			objects = repos.getSuperLineManagers();
		else if (DataTypes.LOM.getValue().equals(type))
			objects = repos.getLogisticManagers();
		else if (DataTypes.HR.getValue().equals(type))
			objects = repos.getHrManagers();
		Map<String, String> map = new HashMap<String, String>();
		for (Object[] o : objects) {
			map.put((String) o[0], (String) o[1]);
		}
		return map;
	}

	@Cacheable("affectationService.findLightByLineManager")
	public List<User> findLightByLineManager(String lineManagerUsername, Boolean active) {
		return repos.findLightByLineManager(lineManagerUsername, active);
	}

}