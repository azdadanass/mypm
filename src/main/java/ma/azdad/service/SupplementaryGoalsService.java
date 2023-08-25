package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.UserAppraisal;
import ma.azdad.repos.SupplementaryGoalsRepos;

@Component
public class SupplementaryGoalsService extends GenericService<Integer, SupplementaryGoals, SupplementaryGoalsRepos> {
	@Override
	@Cacheable("supplementaryGoalsService.findAll")
	public List<SupplementaryGoals> findAll() {

		List<SupplementaryGoals> list = repos.findAll();
		for (SupplementaryGoals supplementaryGoals : list) {

			initialize(supplementaryGoals.getSectionsData());
			initialize(supplementaryGoals.getSections());
		}

		return list;
	}

	@Cacheable("supplementaryGoalsService.findByUserAppraisal")
	public List<SupplementaryGoals> findByUserAppraisal(UserAppraisal uapp, int i) {

		List<SupplementaryGoals> list = repos.findByUserAppraisal(uapp, i);
		for (SupplementaryGoals supplementaryGoals : list) {

			initialize(supplementaryGoals.getSections());
			initialize(supplementaryGoals.getSectionsData());

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
