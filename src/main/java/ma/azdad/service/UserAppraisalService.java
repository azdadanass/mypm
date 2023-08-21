package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Appraisals;
import ma.azdad.model.BusinessGoals;
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

	@Cacheable("userAppraisalService.findSectionByEligible")
	public List<Sections> findSectionByEligible(UserAppraisal userAppraisal) {

		return repos.findSectionByEligible(userAppraisal);
	}

	@Cacheable("userAppraisalService.findBusinessGoalsBySection0")
	public List<BusinessGoals> findBusinessGoalsBySection0(UserAppraisal userAppraisal) {

		return repos.findBusinessGoalsBySection0(userAppraisal);
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection1")
	public List<SupplementaryGoals> findSupGoalsBySection1(UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection1(userAppraisal);
		for (SupplementaryGoals suppgoals : list) {
			initialize(suppgoals.getSections());
			initialize(suppgoals.getSectionsData());		
		}
		
		return list;
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection2")
	public List<SupplementaryGoals> findSupGoalsBySection2(UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection2(userAppraisal);
		for (SupplementaryGoals suppgoals : list) {
			initialize(suppgoals.getSections());
			initialize(suppgoals.getSectionsData());		
		}
		
		return list;
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection")
	public List<SupplementaryGoals> findSupGoalsBySection(int number,UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection(number,userAppraisal);
		
		for (SupplementaryGoals supplementaryGoals : list) {
			
			initialize(supplementaryGoals.getSections());
			initialize(supplementaryGoals.getSectionsData());
		}
		
		return repos.findSuppBySection(number,userAppraisal);
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection3")
	public List<SupplementaryGoals> findSupGoalsBySection3(UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection3(userAppraisal);
		for (SupplementaryGoals suppgoals : list) {
			initialize(suppgoals.getSections());
			initialize(suppgoals.getSectionsData());		
		}
		
		return list;
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection4")
	public List<SupplementaryGoals> findSupGoalsBySection4(UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection4(userAppraisal);
		for (SupplementaryGoals suppgoals : list) {
			initialize(suppgoals.getSections());
			initialize(suppgoals.getSectionsData());		
		}
		
		return list;
	}
	
	@Cacheable("userAppraisalService.findSupGoalsBySection5")
	public List<SupplementaryGoals> findSupGoalsBySection5(UserAppraisal userAppraisal) {

		List<SupplementaryGoals> list = repos.findSuppBySection5(userAppraisal);
		for (SupplementaryGoals suppgoals : list) {
			initialize(suppgoals.getSections());
			initialize(suppgoals.getSectionsData());		
		}
		
		return list;
	}
	
	
	
	
	
	@Cacheable("userAppraisalService.findUserAppraisalByUser")
	public List<UserAppraisal> findUserAppraisalByUser(User employe) {

		List<UserAppraisal> list = repos.findUserAppraisalByUser(employe);
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

	@Cacheable("userAppraisalService.findUserAppraisalByHR")
	public List<UserAppraisal> findUserAppraisalByHR(Boolean t1,Boolean t2,User employe) {

		List<UserAppraisal> list = repos.findUserAppraisalByHR(t1,t2,employe);
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
	@Cacheable("userAppraisalService.findUserAppraisalByLM")
	public List<UserAppraisal> findUserAppraisalByLM(Boolean t1,Boolean t2,User employe) {

		List<UserAppraisal> list = repos.findUserAppraisalByLM(t1,t2,employe);
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
	
	@Cacheable("userAppraisalService.findSectionByUserAppraisal")
	public List<UserAppraisal> findUserAppraisalByAppraisal(Appraisals appraisal) {

		List<UserAppraisal> list = repos.findUserAppraisalByAppraisal(appraisal);
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

	
}