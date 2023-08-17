package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.SupplementaryGoals;
import ma.azdad.repos.SupplementaryGoalsRepos;

@Component
public class SupplementaryGoalsService extends GenericService<Integer, SupplementaryGoals,SupplementaryGoalsRepos>{
	@Override
	@Cacheable("supplementaryGoalsService.findAll")
	public List<SupplementaryGoals> findAll() {
			
		List<SupplementaryGoals> list =  repos.findAll();
		for (SupplementaryGoals supplementaryGoals : list) {
	
			initialize(supplementaryGoals.getSectionsData());
			initialize(supplementaryGoals.getSections());
		}
		
		return list;
	}

	@Override
	@Cacheable("supplementaryGoalsService.findOne")
	public SupplementaryGoals findOne(Integer id) {
		SupplementaryGoals supplementaryGoals = super.findOne(id);
		
			initialize(supplementaryGoals.getSectionsData());
			initialize(supplementaryGoals.getSections());
	
		
		return supplementaryGoals;
	}
	
	

}
