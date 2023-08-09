package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Lob;
import ma.azdad.repos.LobRepos;

@Component
public class LobService extends GenericService<Integer,Lob, LobRepos> {

	@Override
	@Cacheable("lobService.findAll")
	public List<Lob> findAll() {
		return repos.findAll();
	}

	@Cacheable("lobService.findLight")
	public List<Lob> findLight() {
		return repos.findLight();
	}

	@Override
	public Lob findOne(Integer id) {
		Lob lob = super.findOne(id);
		initialize(lob.getManager());
		initialize(lob.getBu().getCompany());
		initialize(lob.getFileList());
		initialize(lob.getHistoryList());
		return lob;
	}

	@Cacheable("lobService.findLightByBu")
	public List<Lob> findLightByBu(Integer buId) {
		return repos.findLightByBu(buId);
	}

	public Long countByNameAndBu(Lob lob) {
		return repos.countByNameAndBu(lob.getName(), lob.getBuId(), lob.getId());
	}

	@Cacheable("lobService.findNameByProject")
	public String findNameByProject(Integer projectId) {
		return repos.findNameByProject(projectId);
	}
	
	@Cacheable("lobService.findLightByManager")
	public List<Lob> findLightByManager(String managerUsername) {
		return repos.findLightByManager(managerUsername);
	}
}
