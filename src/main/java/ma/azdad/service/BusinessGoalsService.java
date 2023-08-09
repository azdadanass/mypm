package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.BusinessGoals;
import ma.azdad.repos.BusinessGoalsRepos;

@Component
public class BusinessGoalsService extends GenericService<Integer, BusinessGoals,BusinessGoalsRepos>{
	@Override
	@Cacheable("businessGoalsService.findAll")
	public List<BusinessGoals> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("businessGoalsService.findOne")
	public BusinessGoals findOne(Integer id) {
		BusinessGoals businessGoals = super.findOne(id);

		return businessGoals;
	}

}
