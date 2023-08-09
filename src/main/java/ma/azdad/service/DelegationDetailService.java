package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.DelegationDetail;
import ma.azdad.repos.DelegationDetailRepos;

@Component
public class DelegationDetailService extends GenericService<Integer,DelegationDetail, DelegationDetailRepos> {

	@Override
	@Cacheable("delegationDetailService.findAll")
	public List<DelegationDetail> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("delegationDetailService.findOne")
	public DelegationDetail findOne(Integer id) {
		DelegationDetail delegationDetail = super.findOne(id);
		return delegationDetail;
	}

	@Cacheable("delegationDetailService.find")
	public List<DelegationDetail> find(String username, Boolean delegate, Boolean active) {
		if (delegate)
			return active ? repos.findByDelegateAndActive(username) : repos.findByDelegateAndInactive(username);
		else
			return active ? repos.findByDelegatorAndActive(username) : repos.findByDelegatorAndInactive(username);
	}

}
