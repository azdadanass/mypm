package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Appraisals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
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
			initialize(userAppraisal.getCommentList());
			initialize(userAppraisal.getFileList());
			initialize(userAppraisal.getHistoryList());
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
			initialize(userAppraisal.getCommentList());
			initialize(userAppraisal.getFileList());
			initialize(userAppraisal.getHistoryList());
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
		initialize(userAppraisal.getCommentList());
		initialize(userAppraisal.getFileList());
		initialize(userAppraisal.getHistoryList());

		return userAppraisal;
	}

	@Cacheable("userAppraisalService.findSectionByEligible")
	public List<Sections> findSectionByEligible(UserAppraisal userAppraisal) {

		return repos.findSectionByEligible(userAppraisal);
	}

	
	
	
	@Cacheable("userAppraisalService.findUserAppraisalByUser")
	public List<UserAppraisal> findUserAppraisalByUser(User user) {

		List<UserAppraisal> list = repos.findUserAppraisalByUser(user);
		for (UserAppraisal userAppraisal : list) {
			initialize(userAppraisal.getAppraisal());
			initialize(userAppraisal.getEmploy());
			initialize(userAppraisal.getAppraisee());
			initialize(userAppraisal.getCommentList());
			initialize(userAppraisal.getFileList());
			initialize(userAppraisal.getHistoryList());
			
		}

		return list;
	}
	
	@Cacheable("userAppraisalService.findByAppraisalAndManager")
	public List<UserAppraisal> findByAppraisalAndManager(Appraisals appraisal, User user) {

		List<UserAppraisal> list = repos.findByAppraisalAndManager(appraisal,user);
		for (UserAppraisal userAppraisal : list) {
			initialize(userAppraisal.getAppraisal());
			initialize(userAppraisal.getEmploy());
			initialize(userAppraisal.getAppraisee());
			initialize(userAppraisal.getCommentList());
			initialize(userAppraisal.getFileList());
			initialize(userAppraisal.getHistoryList());
			
		}

		return list;
	}
	
	@Cacheable("userAppraisalService.findSectionDataByGoalId")
	public List<SectionsData> findSectionDataByGoalId(Integer goalid) {

		return repos.findSectionDataByGoalId(goalid);
	}

	
	

	
}