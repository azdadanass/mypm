package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Costcenter;
import ma.azdad.repos.CostcenterRepos;

@Component
public class CostcenterService extends GenericService<Integer,Costcenter, CostcenterRepos> {

	@Override
	@Cacheable("costcenterService.findAll")
	public List<Costcenter> findAll() {
		return repos.findAll();
	}

	@Cacheable("costcenterService.findLight")
	public List<Costcenter> findLight() {
		return repos.findLight();
	}

	@Override
	public Costcenter findOne(Integer id) {
		Costcenter costcenter = super.findOne(id);
		initialize(costcenter.getLob());
		initialize(costcenter.getFileList());
		initialize(costcenter.getHistoryList());

		return costcenter;
	}

	@Cacheable("costcenterService.findLightByLob")
	public List<Costcenter> findLightByLob(Integer lobId) {
		return repos.findLightByLob(lobId);
	}

	public Long countByYearAndLob(Costcenter costcenter) {
		return repos.countByYearAndLob(costcenter.getYear(), costcenter.getLobId(), costcenter.getId());
	}

}
