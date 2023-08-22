package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

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
import ma.azdad.repos.AffectationRepos;
import ma.azdad.repos.AppraisalsRepos;
import ma.azdad.repos.SupplementaryGoalsRepos;
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

	// @Autowired
	// User user;

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
	private List<Integer> yearRange;
	private List<User> usersBackup; // Ajoutez cette liste pour stocker le backup

	@Override
	@PostConstruct
	public void init() {
		super.init();

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

		return appraisalsService.findByAppraisalAndManager(sessionView.getUser(), model);
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
				getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));
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
				userAppraisal.setUserStatsApproved(sessionView.getUser());
				userAppraisal.setUserStatsSelfAssessmentMidYear(usr);
				userAppraisal.setUserStatsApprovedLMMidYear(usr);
				userAppraisal.setUserStatsSubmitedMidYear(usr);
				userAppraisal.setUserStatsSelfAssessmentFinalYear(usr);
				userAppraisal.setUserStatsSubmitedFinalYear(usr);
				userAppraisal.setUserStatsApprovedLMFinalYear(usr.getAffectation().getLineManager());
				userAppraisal.setUserStatsClosed(usr);
				
				userAppraisalService.save(userAppraisal);

			}

		} else {
			List<UserAppraisal> liUser = userAppraisalService.findUserAppraisalByAppraisal(model);

			for (UserAppraisal usr : liUser) {

				// List<UserAppraisal> uss =
				// userAppraisalService.findUserAppraisalByUser(usr.getEmploy());
				usr.setAppraisal(model);
				userAppraisalService.save(usr);

			}

		}

		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
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
		// Stockez le backup de la liste users avant de supprimer l'utilisateur
		if (users != null && user != null) {
			usersBackup = new ArrayList<>(users);
			users.remove(user);
			undoMode = true; // Activez le mode annulation de suppression

		}
	}

	public void undoDelete() {
		// Restaurez la liste users à partir du backup pour annuler la suppression
		if (usersBackup != null) {
			users = new ArrayList<>(usersBackup);
			usersBackup = null; // Réinitialisez le backup après restauration
			undoMode = false; // Désactivez le mode annulation de suppression

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
	public Boolean canMidYearReview() {
		return AppraisalsStatus.OPEN.equals(model.getAppraisalsStatus());
	}

	@Scheduled(fixedRate = 600000)
	public void midYearReview() {
		if (!canMidYearReview())
			return;
		if (!validateMidYear()) {
			return;
		}
		model.setDateStatsMid(new Date());
		model.setUserStatsMid(sessionView.getUser());
		model.setAppraisalsStatus(AppraisalsStatus.MID_YEAR_REVIEW);
		model.addHistory(new AppraisalsHistory(model.getAppraisalsStatus().getValue(), sessionView.getUser(),
				"change AppraisalsStats from OPEN to MID_YEAR_REVIEW"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	// FinalYearReview
	public Boolean canFinalYearReview() {
		return AppraisalsStatus.MID_YEAR_REVIEW.equals(model.getAppraisalsStatus());
	}

	@Scheduled(fixedRate = 600000)
	public void finalYearReview() {
		if (!canFinalYearReview())
			return;
		if (!validateFinalYear()) {
			return;
		}
		model.setDateStatsFinal(new Date());
		model.setUserStatsFinal(sessionView.getUser());
		model.setAppraisalsStatus(AppraisalsStatus.FINAL_REVIEW);
		model.addHistory(new AppraisalsHistory(model.getAppraisalsStatus().getValue(), sessionView.getUser(),
				"change AppraisalsStats from MID_YEAR_REVIEW to FINAL_YEAR_REVIEW"));

		service.save(model);
		model = service.findOne(model.getId());
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

}