package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Appraisals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.SupplementaryGoals;
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

	@Cacheable("userAppraisalService.findSectionByUserAppraisalAndNumber")
	public Sections findSectionByUserAppraisalAndNumber(UserAppraisal uap,Integer number) {
		Sections sections = repos.findSectionByUserAppraisalAndNumber(uap,number);

		return sections;
	}

	
	@Cacheable("userAppraisalService.findByAppraisal")
	public List<UserAppraisal> findByAppraisal(Appraisals appraisals) {

		List<UserAppraisal> list = repos.findByAppraisal(appraisals);
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
	
	@Cacheable("userAppraisalService.findByAppraisal")
	public List<SectionsData> findSectionDataByGoalId(Integer goalid) {

		return repos.findSectionDataByGoalId(goalid);
	}
	
	@Cacheable("userAppraisalService.findSupplementaryByGoaldId")
	public List<SupplementaryGoals> findSupplementaryByGoaldId(Integer goalid) {

		List<SupplementaryGoals> list = repos.findSupplementaryByGoaldId(goalid);
		for (SupplementaryGoals supplementaryGoals : list) {
			initialize(supplementaryGoals.getSections());
			initialize(supplementaryGoals.getSectionsData());

		}

		return list;
	}
	
	
	

	
}