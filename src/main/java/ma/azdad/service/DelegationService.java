package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Delegation;
import ma.azdad.model.DelegationDetail;
import ma.azdad.model.DelegationDetailType;
import ma.azdad.model.DelegationType;
import ma.azdad.repos.DelegationRepos;

@Component
public class DelegationService extends GenericService<Integer, Delegation, DelegationRepos> {

	@Override
	@Cacheable("delegationService.findAll")
	public List<Delegation> findAll() {
		return repos.findAll();
	}

	@Override
	public Delegation findOne(Integer id) {
		Delegation delegation = super.findOne(id);
		initialize(delegation.getDelegate());
		initialize(delegation.getDelegator());
		delegation.getDetailList().forEach(i -> {
			initialize(i.getProject());
			initialize(i.getLob());
			initialize(i.getUser());
		});

		return delegation;
	}

	@Cacheable("delegationService.findDelegatedProjects")
	public List<Integer> findDelegatedProjectIdList(String username) {
		return repos.findDelegatedProjectIdList(username, DelegationDetailType.PM);
	}

	@Cacheable("delegationService.find")
	public List<Delegation> find(String username, Boolean delegate, Boolean active) {
		if (delegate)
			return active ? repos.findByDelegateAndActive(username) : repos.findByDelegateAndInactive(username);
		else
			return active ? repos.findByDelegatorAndActive(username) : repos.findByDelegatorAndInactive(username);
	}

	public void updateStatus() {
		repos.updateStatus();
	}

	@Override
	public void evictCache() {
		super.evictCache();
		evictCache("delegationDetailService");
	}
	
	public void splitDelegationPerTypeScript() {
		repos.findAll().forEach(delegation->{
			if(delegation.getProjectDetailList().size()>0 && delegation.getUserDetailList().size()>0) {
				delegation.setType(DelegationType.PROJECT);
				
				Delegation newDelegation = new Delegation();
				newDelegation.setType(DelegationType.USER);
				newDelegation.setCreateDate(delegation.getCreateDate());
				newDelegation.setDelegate(delegation.getDelegate());
				newDelegation.setDelegator(delegation.getDelegator());
				newDelegation.setStartDate(delegation.getStartDate());
				newDelegation.setEndDate(delegation.getEndDate());
				newDelegation.setDescription(delegation.getDescription());
				newDelegation.setStatus(delegation.getStatus());
				
				delegation.getUserDetailList().forEach(d->newDelegation.addDetail(new DelegationDetail(d.getUser())));
				delegation.getDetailList().removeAll(delegation.getUserDetailList());
				
				repos.save(newDelegation);
				repos.save(delegation);
				
				
			}else if(delegation.getProjectDetailList().size()>0) {
				delegation.setType(DelegationType.PROJECT);
				repos.save(delegation);
			}else if(delegation.getUserDetailList().size()>0) {
				delegation.setType(DelegationType.USER);
				repos.save(delegation);
			}
		});
	}

}
