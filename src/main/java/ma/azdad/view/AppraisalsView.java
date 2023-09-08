package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Appraisals;
import ma.azdad.model.AppraisalsComment;
import ma.azdad.model.AppraisalsFile;
import ma.azdad.model.AppraisalsHistory;
import ma.azdad.model.AppraisalsStatus;
import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.model.UserAppraisalHistory;
import ma.azdad.repos.AffectationRepos;
import ma.azdad.repos.AppraisalsRepos;
import ma.azdad.repos.UserAppraisalRepos;
import ma.azdad.service.AppraisalsService;
import ma.azdad.service.BusinessGoalsService;
import ma.azdad.service.SectionsService;
import ma.azdad.service.SupplementaryGoalsService;
import ma.azdad.service.UserAppraisalService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class AppraisalsView extends GenericView<Integer, Appraisals, AppraisalsRepos, AppraisalsService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

	@Autowired
	AffectationRepos af;

	@Autowired
	UserView userview;

	@Autowired
	UserAppraisalService userAppraisalService;

	@Autowired
	SupplementaryGoalsService supplementaryGoalsService;

	@Autowired
	UserAppraisalRepos userAppraisalRepos;

	@Autowired
	AppraisalsService appraisalsService;

	@Autowired
	SectionsService sectionsService;

	@Autowired
	BusinessGoalsService businessGoalsService;

	private boolean undoMode; // Ajoutez cette propriété pour gérer le mode annulation de suppression

	private int step = 1;
	private List<User> users;
	private List<User> userNoAppraisalList = new ArrayList<>();
	private List<Integer> yearRange;
	private List<User> usersBackup; // Ajoutez cette liste pour stocker le backup

	@Override
	@PostConstruct
	public void init() {
		super.init();
		evictCache();

		yearRange = new ArrayList<>();
		for (int i = 2020; i <= 2050; i++) {
			yearRange.add(i);
		}
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public List<User> getUserNoAppraisalList() {
		return userNoAppraisalList;
	}

	public void setUserNoAppraisalList(List<User> userNoAppraisalList) {
		this.userNoAppraisalList = userNoAppraisalList;
	}

	public List<Integer> getYearRange() {
		return yearRange;
	}

	public void setYearRange(List<Integer> yearRange) {
		this.yearRange = yearRange;
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsMyPm() || sessionView.getIsMyPmHr() || sessionView.getIsMyPmLineManager();
	}

	@Override
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;

		filterBean(searchBean);
	}

	public List<UserAppraisal> findByAppraisalAndManager() {

		return service.findByAppraisalAndManager(sessionView.getUser(), model);
	}

	public void saveUserAppraisalForNoUserAppraisal(User userNoAppraisal) {

		// if (userNoAppraisal!=null) {
		System.out.println(userNoAppraisal);
		UserAppraisal userAppraisal = new UserAppraisal();
		userAppraisal.setAppraisee(sessionView.getUser());
		userAppraisal.setAppraisal(model);
		userAppraisal.setEmploy(userNoAppraisal);
		userAppraisal.setDateStatsCreated(new Date());
		System.out.println(userAppraisal);
		userAppraisalService.save(userAppraisal);

		evictCache();
		// evaluatedEmployees.add(usr);
		// }
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsMyPmHr();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.addHistory(new AppraisalsHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(),
				sessionView.getUser() + " has Created Master Appraisal"));
		model.setUserStatsOpen(sessionView.getUser());
		model.setDateStatsOpen(new Date());

		model = service.save(model);

		if (userAppraisalService.findUserAppraisalByAppraisal(model).size() == 0) {
			for (User usr : users) {

				UserAppraisal userAppraisal = new UserAppraisal();
				userAppraisal.setAppraisee(sessionView.getUser());
				userAppraisal.setAppraisal(model);
				userAppraisal.setEmploy(usr);
				userAppraisal.setDateStatsCreated(new Date());

				userAppraisal.setUserStatsCreated(sessionView.getUser());
				userAppraisal.setUserStatsEdited(usr);
				userAppraisal.setUserStatsSubmited(usr);
				userAppraisal.setUserStatsApprovedLM(usr.getAffectation().getLineManager());
				userAppraisal.setUserStatsApproved(usr.getAffectation().getHrManager());

				userAppraisal.setUserStatsSelfAssessmentMidYear(usr.getAffectation().getHrManager());
				userAppraisal.setUserStatsMidEdited(usr);
				userAppraisal.setUserStatsSubmitedMidYear(usr);
				userAppraisal.setUserStatsApprovedLMMidYear(usr.getAffectation().getLineManager());

				userAppraisal.setUserStatsSelfAssessmentFinalYear(usr.getAffectation().getHrManager());
				userAppraisal.setUserStatsFinalEdited(usr);
				userAppraisal.setUserStatsSubmitedFinalYear(usr);
				userAppraisal.setUserStatsApprovedLMFinalYear(usr.getAffectation().getLineManager());
				userAppraisal.setUserStatsClosed(usr);

				userAppraisal.addHistory(new UserAppraisalHistory(getIsAddPage() ? "Created" : "Edited",
						usr.getAffectation().getHrManager(), usr.getAffectation().getLineManager() + " has Created "
								+ userAppraisal.getEmploy().getFullName() + " Appraisal"));
				userAppraisalService.save(userAppraisal);

			}

		} else {

			List<UserAppraisal> liUser = userAppraisalService.findUserAppraisalByAppraisal(model);

			Set<User> evaluatedEmployees = new HashSet<>();

			for (UserAppraisal usr : liUser) {
				evaluatedEmployees.add(usr.getEmploy());
			}

			for (User usr : users) {
				if (!evaluatedEmployees.contains(usr)) {
					boolean shouldCreateAppraisal = true;

					for (UserAppraisal existingAppraisal : liUser) {
						if (existingAppraisal.getEmploy().equals(usr)) {
							shouldCreateAppraisal = false;
							break;
						}
					}

					if (shouldCreateAppraisal) {
						UserAppraisal userAppraisal = new UserAppraisal();
						userAppraisal.setAppraisee(sessionView.getUser());
						userAppraisal.setAppraisal(model);
						userAppraisal.setEmploy(usr);
						userAppraisal.setDateStatsCreated(new Date());

						userAppraisalService.save(userAppraisal);

						evaluatedEmployees.add(usr);
					}
				}
			}
		}

		String listPage = "viewAppraisals.xhtml";
		String parameters = "faces-redirect=true&pageIndex=9&id=" + model.getId();

		return addParameters(listPage, parameters);

	}

	public Boolean validate() {
		return true;
	}

	public Boolean validateMidYear() {
		Date dt = new Date();
		if (!(model.getMidYearReviewEndDate().compareTo(dt) >= 0
				&& model.getMidYearReviewStartDate().compareTo(dt) <= 0)) {
			return FacesContextMessages.ErrorMessages("date not valide in appraisals Mid year date");
		}
		return true;
	}

	public Boolean validateFinalYear() {
		Date dt = new Date();
		if (!(model.getEndYearSummaryEndDate().compareTo(dt) >= 0
				&& model.getEndYearSummaryStartDate().compareTo(dt) <= 0)) {
			return FacesContextMessages.ErrorMessages("date not valide in appraisals Final year date");
		}
		return true;
	}

	public Boolean validate1() {
		if (model.getEndDate() != null && model.getStartDate().compareTo(model.getEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Date should be lower than end date");
		return true;
	}

	public Boolean validate2() {

		if (model.getPlanningEndDate() != null
				&& model.getPlanningStartDate().compareTo(model.getPlanningEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Planning Start Date should be lower than Planning End date");
		if (model.getMidYearReviewEndDate() != null
				&& model.getMidYearReviewStartDate().compareTo(model.getMidYearReviewEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Mid Year Start Date should be lower than Mid Year End Date");
		if (model.getEndYearSummaryEndDate() != null
				&& model.getEndYearSummaryStartDate().compareTo(model.getEndYearSummaryEndDate()) > 0)
			return FacesContextMessages.ErrorMessages("Start Summary Date should be lower than end Summary date");
		if (model.getPlanningEndDate().compareTo(model.getMidYearReviewStartDate()) > 0)
			return FacesContextMessages.ErrorMessages("Mid Year Start Date should be after Planning Date");
		if (model.getMidYearReviewEndDate().compareTo(model.getEndYearSummaryStartDate()) > 0)
			return FacesContextMessages.ErrorMessages("Final Year Start Date should be after Mid Year Date");

		return true;
	}

	// files
	private AppraisalsFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		AppraisalsFile modelFile = new AppraisalsFile(file, fileType, event.getFile().getFileName(),
				sessionView.getUser());
		model.addFile(modelFile);
		synchronized (AppraisalsView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return sessionView.getIsMyPm() || sessionView.getIsMyPmLineManager();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	public AppraisalsFile getFile() {
		return file;
	}

	public void setFile(AppraisalsFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			List<UserAppraisal> userapp = userAppraisalService.findUserAppraisalByAppraisal(model);
			if (userapp.size() > 0) {
				for (UserAppraisal usap : userapp) {
					List<Sections> sec = sectionsService.findSectionsByUserAppraisal(usap);
					if (sec.size() > 0) {
						for (Sections sect : sec) {

							List<BusinessGoals> bgoal = businessGoalsService.findBySections(sect);
							List<SupplementaryGoals> spgoal = userAppraisalRepos.findSuppByUser(usap);
							if (bgoal.size() > 0) {
								for (BusinessGoals bg : bgoal) {

									businessGoalsService.delete(bg);
								}
							}
							if (spgoal.size() > 0) {
								for (SupplementaryGoals sp : spgoal) {

									supplementaryGoalsService.delete(sp);
								}
							}
							sectionsService.delete(sect);
						}
					}
					userAppraisalService.delete(usap);
				}
			}
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	public List<Appraisals> findAll() {
		return service.findAll();
	}

	public List<User> findByHr(Boolean a, Boolean b, User c) {

		return appraisalsService.findByHr(true, true, sessionView.getUser());
	}

	// getters & setters
	public Appraisals getModel() {
		return model;
	}

	public void setModel(Appraisals model) {
		this.model = model;
	}

	// comments
	private AppraisalsComment comment = new AppraisalsComment();

	public Boolean canAddComment() {
		return sessionView.getIsInternalAdmin();
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	public AppraisalsComment getComment() {
		return comment;
	}

	public void setComment(AppraisalsComment comment) {
		this.comment = comment;
	}

	public void previousStep() {
		if (step != 1)
			step--;
	}

	public List<User> getUsersByManager() {

		users = appraisalsService.findByHr(true, false, sessionView.getUser());
		return users;
	}

	public void deleteUser(User user) {
		if (users != null && user != null) {
			usersBackup = new ArrayList<>(users);
			users.remove(user);
			undoMode = true;

		}
	}

	public void undoDelete() {

		if (usersBackup != null) {
			users = new ArrayList<>(usersBackup);
			usersBackup = null;
			undoMode = false;

		}
	}

	public boolean getIsUndoMode() {
		return undoMode;
	}

	@Transactional
	public String nextStep() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");

		switch (step) {
		case 1:
			if (!validate1()) {
				return null;
			}
			step++;

			break;

		case 2:
			if (!validate2()) {
				return null;
			}

			step++;
			break;
		case 3:
			return save();
		}
		return null;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	// MidYearReview
	public Boolean canMidYearReview(Appraisals ap) {
		return AppraisalsStatus.OPEN.equals(ap.getAppraisalsStatus());
	}

	public void midYearReview(Appraisals ap) {

		if (!canMidYearReview(ap))
			return;
		if (!validateMidYear(ap)) {
			return;
		}
		ap.setDateStatsMid(new Date());
		ap.setUserStatsMid(sessionView.getUser());
		ap.setAppraisalsStatus(AppraisalsStatus.MID_YEAR_REVIEW);
		ap.addHistory(new AppraisalsHistory(ap.getAppraisalsStatus().getValue(), sessionView.getUser(),
				"change AppraisalsStats from OPEN to MID_YEAR_REVIEW"));

		service.save(ap);
		// model = service.findOne(model.getId());

	}

	@Scheduled(fixedRate = 30000)
	public void autoMidFinalYear() {
		for (Appraisals ap : findAll()) {

			midYearReview(ap);
			finalYearReview(ap);

		}
	}

	// FinalYearReview
	public Boolean canFinalYearReview(Appraisals ap) {
		return AppraisalsStatus.MID_YEAR_REVIEW.equals(ap.getAppraisalsStatus());
	}

	public void finalYearReview(Appraisals ap) {
		if (!canFinalYearReview(ap))
			return;
		if (!validateFinalYear(ap)) {
			return;
		}
		ap.setDateStatsFinal(new Date());
		ap.setUserStatsFinal(sessionView.getUser());
		ap.setAppraisalsStatus(AppraisalsStatus.FINAL_REVIEW);
		ap.addHistory(new AppraisalsHistory(ap.getAppraisalsStatus().getValue(), sessionView.getUser(),
				"change AppraisalsStats from MID_YEAR_REVIEW to FINAL_YEAR_REVIEW"));

		service.save(ap);
		// model = service.findOne(model.getId());
	}

	public Boolean validateMidYear(Appraisals ap) {
		Date dt = new Date();
		if (!(ap.getMidYearReviewEndDate().compareTo(dt) >= 0 && ap.getMidYearReviewStartDate().compareTo(dt) <= 0)) {
			return false;
		}
		return true;
	}

	public Boolean validateFinalYear(Appraisals ap) {
		Date dt = new Date();
		if (!(ap.getEndYearSummaryEndDate().compareTo(dt) >= 0 && ap.getEndYearSummaryStartDate().compareTo(dt) <= 0)) {
			return false;
		}
		return true;
	}

	// Closed
	public Boolean canClosed() {
		return AppraisalsStatus.FINAL_REVIEW.equals(model.getAppraisalsStatus()) && sessionView.getIsMyPmHr();
	}

	public void closed() {
		if (!canClosed())
			return;

		model.setDateStatsClosed(new Date());
		model.setUserStatsClosed(sessionView.getUser());
		model.setAppraisalsStatus(AppraisalsStatus.CLOSED);
		model.addHistory(new AppraisalsHistory(model.getAppraisalsStatus().getValue(), sessionView.getUser(),
				"change AppraisalsStats from FINAL_YEAR_REVIEW to CLOSED"));
		service.save(model);
		model = service.findOne(model.getId());
	}

	public List<User> findUserNoAppraisal() {
		userNoAppraisalList = new ArrayList<>();
		List<User> userNoAppraisalList = service.findUserNoAppraisal(true, sessionView.getUser(), model,
				model.getEndDate());

		return userNoAppraisalList;
	}

	public void deleteUserAppraisal(UserAppraisal u) {

		try {
			userAppraisalService.delete(u);
			evictCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public Boolean listenerEdit() {
		for (UserAppraisal ua : userAppraisalService.findUserAppraisalByAppraisal(model)) {
			if(ua.getUserAppraisalStatus().getValue()!="Created") {
				return false;
			}
			
		}
		return true;
}
	

}