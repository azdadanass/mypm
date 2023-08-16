package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Appraisals;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.repos.AppraisalsRepos;

@Component
public class AppraisalsService extends GenericService<Integer, Appraisals,AppraisalsRepos>{
	
	@Override
	@Cacheable("appraisalsService.findAll")
	public List<Appraisals> findAll() {
		
		List<Appraisals> list =  repos.findAll();

		
		for (Appraisals appraisals : list) {
			initialize(appraisals.getCommentList());
			initialize(appraisals.getFileList());
			initialize(appraisals.getHistoryList());

		}
		return repos.findAll();
	}

	@Override
	@Cacheable("appraisalsService.findOne")
	public Appraisals findOne(Integer id) {
		
		Appraisals appraisals = super.findOne(id);
		initialize(appraisals.getCommentList());
		initialize(appraisals.getFileList());
		initialize(appraisals.getHistoryList());

		return appraisals;
	}
	
	@Cacheable("appraisalsService.findByHr")
	public List<User>findByHr(Boolean a,Boolean b,User c) {

		return repos.findByHr(a, b, c);
	}

	@Cacheable("userAppraisalService.findByAppraisalAndManager")
	public List<UserAppraisal> findByAppraisalAndManager(User appraisee,Appraisals appraisals) {

		List<UserAppraisal> list = repos.findByAppraisalAndManager(appraisee,appraisals);
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
