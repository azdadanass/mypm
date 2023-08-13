	package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Sections;
import ma.azdad.model.UserAppraisal;
import ma.azdad.repos.SectionsRepos;

@Component
public class SectionsService extends GenericService<Integer, Sections,SectionsRepos>{
	@Override
	@Cacheable("sectionsService.findAll")
	public List<Sections> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("sectionsService.findOne")
	public Sections findOne(Integer id) {
		Sections sections = super.findOne(id);

		return sections;
	}
	
	
	@Cacheable("sectionsService.findSectionsDistinct")
	public List<String> findSectionsDistinct() {
		return repos.findDistinctSectionTitles();
	}

	@Cacheable("sectionsService.findSectionsByUserAppraisal")
	public List<Sections> findSectionsByUserAppraisal(UserAppraisal uap) {
		return repos.findByUserAppraisal(uap);
	}

	
	

}