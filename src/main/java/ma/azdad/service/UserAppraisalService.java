package ma.azdad.service;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Appraisals;
import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.ToNotify;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.model.UserAppraisalComment;
import ma.azdad.model.UserAppraisalStatus;
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
	
	@Cacheable("userappraisalService.findHireDate")
	public Date findHireDate(String u) {
		return repos.findHireDate(u);
	}
	
	
	@Cacheable("userappraisalService.findToNotify")
	public ToNotify findToNotify(String u,UserAppraisal uap) {
		return repos.findToNotify(u,uap);
	}
	
	
	@Cacheable("userappraisalService.findToNotifyByUserAppraisal")
	public List<ToNotify> findToNotifyByUserAppraisal(UserAppraisal uap) {
		
		List<ToNotify> list = repos.findToNotifyByUserAppraisal(uap);
		for (ToNotify userAppraisal : list) {
			initialize(userAppraisal.getInternalResource());
			//initialize(userAppraisal.getUserAppraisal());
			
		}
		return list;
	}
	
	@Cacheable("userappraisalService.findToNotifyByUserAppraisalFinal")
	public List<ToNotify> findToNotifyByUserAppraisalFinal(UserAppraisal uap) {
		
		List<ToNotify> list = repos.findToNotifyByUserAppraisalFinal(uap);
		for (ToNotify userAppraisal : list) {
			initialize(userAppraisal.getInternalResource());
			//initialize(userAppraisal.getUserAppraisal());
			
		}
		return list;
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
		initialize(userAppraisal.getToNotifyList());


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
	
	
	@Cacheable("userAppraisalService.findCommentByTitle")
	public UserAppraisalComment findCommentByTitle(UserAppraisal userAppraisal) {		
		return repos.findCommentByTitle(userAppraisal);
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
	
	@Cacheable("userAppraisalService.findSuppBySection")
	public List<SupplementaryGoals> findSuppBySection(int i,UserAppraisal employe) {

		List<SupplementaryGoals> list = repos.findSuppBySection(i,employe);
		for (SupplementaryGoals userAppraisal : list) {
		
			initialize(userAppraisal.getCommentList());
			initialize(userAppraisal.getFileList());
			initialize(userAppraisal.getHistoryList());
			initialize(userAppraisal.getSectionsData());
			initialize(userAppraisal.getSections());
			
			
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
	
	@Cacheable("userAppraisalService.countToSubmitted")
	public Long countToSubmitted(String username ,UserAppraisalStatus u) {
		return repos.countToSubmitted(username,u);
	}
	
	@Cacheable("userAppraisalService.countToApprovedMid")
	public Long countToApprovedMid(User u) {
		return repos.countToApprovedMid(u.getUsername(), UserAppraisalStatus.SUBMITED_MID_YEAR);
	}
	@Cacheable("userAppraisalService.countToApprovedFinal")
	public Long countToApprovedFinal(User u) {
		return repos.countToApprovedFinal(u.getUsername(), UserAppraisalStatus.SUBMITED_FINAL_YEAR);
	}
	@Cacheable("userAppraisalService.countToApproved")
	public Long countToApproved(User u) {
		return repos.countToApproved(u.getUsername(), UserAppraisalStatus.SUBMITED, UserAppraisalStatus.APPROVED_LM);
	}
	
	
	
	@Cacheable("userAppraisalService.findUserappraisalbyStats")
	public List<UserAppraisal> findUserappraisalbyStats(String username,UserAppraisalStatus stats1) {
		
		List<UserAppraisal> list = repos.findUserappraisalbyStats(username, stats1);
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
	
	@Cacheable("userAppraisalService.findUserappraisalbykeyworkerMid")
	public List<UserAppraisal> findUserappraisalbykeyworkerMid(String username,UserAppraisalStatus stats1) {
		
		List<UserAppraisal> list = repos.findUserappraisalbykeyworkerMid(username, stats1);
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
	@Cacheable("userAppraisalService.findUserappraisalbykeyworkerFinal")
	public List<UserAppraisal> findUserappraisalbykeyworkerFinal(String username,UserAppraisalStatus stats1) {
		
		List<UserAppraisal> list = repos.findUserappraisalbykeyworkerFinal(username, stats1);
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
	
	@Cacheable("userAppraisalService.countTocommentMid")
	public Long countTocommentMid(String username,UserAppraisalStatus stats) {
		
		return repos.countTocommentMid(username,stats);
	
	}
	
	
	@Cacheable("userAppraisalService.countTocommentFinal")
	public Long countTocommentFinal(String username,UserAppraisalStatus stats) {
		
		return repos.countTocommentFinal(username,stats);
	
	}
	
	
	
	@Cacheable("userAppraisalService.findUserappraisalbyStatsApproved")
	public List<UserAppraisal> findUserappraisalbyStatsApproved(String username,UserAppraisalStatus stats1,UserAppraisalStatus stats2) {
		
		List<UserAppraisal> list = repos.findUserappraisalbyStatsApproved(username, stats1,stats2);
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
	@Cacheable("userAppraisalService.findUserappraisalRolebyStats")
	public List<UserAppraisal> findUserappraisalRolebyStats(String username,UserAppraisalStatus stats1,UserAppraisalStatus stats2) {
		
		List<UserAppraisal> list = repos.findUserappraisalRolebyStats(username, stats1, stats2);
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
	
	
	
	
	@Cacheable("userAppraisalService.countSections")
	public Integer countSections(UserAppraisal u) {
		return repos.countSections(u);
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