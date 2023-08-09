package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.repos.UserAppraisalRepos;

@Component
public class UserAppraisalService extends GenericService<Integer, UserAppraisal,UserAppraisalRepos>{
	@Override
	@Cacheable("userAppraisalService.findAll")
	public List<UserAppraisal> findAll() {
		
		List<UserAppraisal> list =  repos.findAll();
		for (UserAppraisal userAppraisal : list) {
			initialize(userAppraisal.getAppraisal());
			initialize(userAppraisal.getEmploy());
		}
		
		return repos.findAll();
	}
	
	@Cacheable("userAppraisalService.findByEmployOrAppraisee")
	public List<UserAppraisal> findByEmployOrAppraisee(User appraisee, User employ) {
		
		List<UserAppraisal> list = repos.findByEmployOrAppraisee(appraisee,employ);
		for (UserAppraisal userAppraisal : list) {
			initialize(userAppraisal.getAppraisal());
			initialize(userAppraisal.getEmploy());
			initialize(userAppraisal.getAppraisee());

		}
		return  list;
	}

	
	@Override
	@Cacheable("userAppraisalService.findOne")
	public UserAppraisal findOne(Integer id) {
		UserAppraisal userAppraisal = super.findOne(id);
		initialize(userAppraisal.getAppraisal());
		initialize(userAppraisal.getEmploy());
		initialize(userAppraisal.getAppraisee());

		return userAppraisal;
	}

}