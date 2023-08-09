package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.SectionsData;
import ma.azdad.repos.SectionsDataRepos;

@Component
public class SectionsDataService extends GenericService<Integer, SectionsData, SectionsDataRepos>{

	@Override
	@Cacheable("sectionsDataService.findAll")
	public List<SectionsData> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("sectionsDataService.findOne")
	public SectionsData findOne(Integer id) {
		SectionsData sectionsData = super.findOne(id);

		return sectionsData;
	}

}
