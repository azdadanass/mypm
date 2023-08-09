package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Bu;
import ma.azdad.repos.BuRepos;

@Component
public class BuService extends GenericService<Integer,Bu, BuRepos> {

	@Override
	@Cacheable("buService.findAll")
	public List<Bu> findAll() {
		return repos.findAll();
	}

	@Cacheable("buService.findLight")
	public List<Bu> findLight() {
		return repos.findLight();
	}

	@Override
	public Bu findOne(Integer id) {
		Bu bu = super.findOne(id);
		initialize(bu.getDirector());
		initialize(bu.getOperationsDirector());
		initialize(bu.getHrDirector());
		initialize(bu.getTechnicalDirector());
		initialize(bu.getCompany());
		initialize(bu.getFileList());
		initialize(bu.getHistoryList());
		return bu;
	}

	@Cacheable("buService.findLightByCompany")
	public List<Bu> findLightByCompany(Integer companyId) {
		return repos.findLightByCompany(companyId);
	}

	public Long countByNameAndCompany(Bu bu) {
		return repos.countByNameAndCompany(bu.getName(), bu.getCompanyId(), bu.getId());
	}

}
