package ma.azdad.service;

import java.util.List;

import org.springframework.stereotype.Component;

import ma.azdad.model.Restriction;
import ma.azdad.repos.RestrictionRepos;

@Component
public class RestrictionService extends GenericService<Integer,Restriction, RestrictionRepos> {

	@Override
	public List<Restriction> findAll() {
		return repos.findAll();
	}

	public List<Restriction> findLight(Boolean active) {
		if (active == null)
			return repos.findLight();
		else if (active)
			return repos.findLightActive();
		else
			return repos.findLightInactive();
	}

	public List<Restriction> findLightByUser(Boolean active, String userUsername) {
		if (active == null)
			return repos.findLightByUser(userUsername);
		else if (active)
			return repos.findLightActiveByUser(userUsername);
		else
			return repos.findLightInactiveByUser(userUsername);
	}

	@Override
	public Restriction findOne(Integer id) {
		Restriction restriction = super.findOne(id);
		initialize(restriction.getUser());
		initialize(restriction.getRequester());
		initialize(restriction.getFileList());
		initialize(restriction.getHistoryList());
		initialize(restriction.getCommentList());
		return restriction;
	}

	public Long countActive() {
		return repos.countActive();
	}

}
