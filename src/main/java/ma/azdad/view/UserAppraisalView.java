package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.model.UserAppraisalComment;
import ma.azdad.model.UserAppraisalFile;
import ma.azdad.model.UserAppraisalHistory;
import ma.azdad.model.UserAppraisalStatus;
import ma.azdad.repos.BusinessGoalsRepos;
import ma.azdad.repos.UserAppraisalRepos;
import ma.azdad.service.BusinessGoalsService;
import ma.azdad.service.SectionsDataService;
import ma.azdad.service.SectionsService;
import ma.azdad.service.SupplementaryGoalsService;
import ma.azdad.service.UserAppraisalService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class UserAppraisalView extends GenericView<Integer, UserAppraisal, UserAppraisalRepos, UserAppraisalService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	FileUploadView fileUploadView;

	@Autowired
	SectionsView sectionsView;

	@Autowired
	SectionsService sectionsService;

	@Autowired
	BusinessGoalsService businessGoalsService;

	@Autowired
	SupplementaryGoalsService supplementaryGoalsService;

	@Autowired
	BusinessGoalsView businessGoalsView;

	@Autowired
	UserAppraisalService userAppraisalService;

	@Autowired
	BusinessGoalsRepos businessGoalsRepos;

	@Autowired
	UserAppraisalRepos userAppraisalRepos;

	@Autowired
	SectionsDataService sectionsDataService;

	private List<String> titleList;
	private List<Boolean> eligibleList;
	private List<Integer> weightList;

	private List<String> goalTitleList;
	

	private int goaltitlecount = 0;
	private List<BusinessGoals> businessGoalsList;
	private List<SupplementaryGoals> supplementaryGoalsList;
	private List<SupplementaryGoals> supplementaryGoalsListBg;
	private List<SectionsData> sectionsDatas;
	private List<UserAppraisal> userAppraisalList;
	private String selectedGoalTitle;
	private Integer isMid=1;
	private List<Sections> sectionList;


	public List<Sections> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<Sections> sectionList) {
		this.sectionList = sectionList;
	}

	public Integer getIsMid() {
		return isMid;
	}
	
	public void setIsMid(Integer isMid) {
		this.isMid = isMid;
	}
	
	public void onDebut() {
		isMid=1;
		
	}
	public void onMid() {
		isMid=2;
		
	}
	public void onFinal(){
		isMid=3;

	}

	

	public String getSelectedGoalTitle() {
		return selectedGoalTitle;
	}

	public void setSelectedGoalTitle(String selectedGoalTitle) {
		this.selectedGoalTitle = selectedGoalTitle;
	}

	public List<UserAppraisal> getUserAppraisalList() {
		return userAppraisalList;
	}

	public void setUserAppraisalList(List<UserAppraisal> userAppraisalList) {
		this.userAppraisalList = userAppraisalList;
	}

	public List<SectionsData> getSectionsDatas() {
		return sectionsDatas;
	}

	public List<UserAppraisal> findUserAppraisalByHR() {
		return userAppraisalService.findUserAppraisalByHR(true, true, sessionView.getUser());
	}

	public List<UserAppraisal> findUserAppraisalByLM() {
		return userAppraisalService.findUserAppraisalByLM(true, true, sessionView.getUser());
	}

	public List<UserAppraisal> findByAppraisal(User employe) {
		userAppraisalList = userAppraisalService.findUserAppraisalByUser(employe);
		return userAppraisalList;
	}

	public void setSectionsDatas(List<SectionsData> sectionsDatas) {
		this.sectionsDatas = sectionsDatas;
	}

	public List<String> getGoalTitleList() {
		return goalTitleList;
	}

	public void setGoalTitleList(List<String> goalTitleList) {
		this.goalTitleList = goalTitleList;
	}
    private PieChartModel pieChartModel;


	public PieChartModel getPieChartModel() {
		return pieChartModel;
	}

	public void setPieChartModel(PieChartModel pieChartModel) {
		this.pieChartModel = pieChartModel;
	}

	@Override
	@PostConstruct
	public void init() {
		super.init();
		//chart*****************************
pieChartModel = new PieChartModel();
        
        // Iterate through the data and add to the pie chart model
        for (Sections item : findSectionByUserAppraisal()) {
            pieChartModel.set(item.getSectionsTitle(), item.getWeight());
        }

        pieChartModel.setTitle("Appraisal Weighting Details");
        pieChartModel.setLegendPosition("e");
        pieChartModel.setShowDataLabels(true);
		
		
		
		// Sections Title
		titleList = new ArrayList<>();
		goalTitleList = new ArrayList<>();
		businessGoalsList = new ArrayList<>();
		supplementaryGoalsList = new ArrayList<>();
		supplementaryGoalsListBg = new ArrayList<>();
		titleList.add("BUSINESS Goals");
		titleList.add("JOB EXECUTION");
		titleList.add("CODE OF CONDUCT");
		titleList.add("LEADERSHIP & DEPENDABILTY");
		titleList.add("PERSONALITY ATTRIBUTES");
		titleList.add("MANAGEMENT RESPONSIBILITIES");
		eligibleList = new ArrayList<>();
		weightList = new ArrayList<>();

		// business Goal title
		goalTitleList.add("Financial Excellence");
		goalTitleList.add("Technical Excellence");
		goalTitleList.add("Business Development");
		goalTitleList.add("Governance ");
		goalTitleList.add("Customer satisfaction");

		for (int i = 0; i < titleList.size(); i++) {
			if (i == 0) {
				eligibleList.add(true);
			}
			eligibleList.add(false);
			weightList.add(0);
		}
		time();
	}

	
	private int step = 1;

	@Override
	public void refreshList() {

		if (pageIndex != null) {
			switch (pageIndex) {
			case 1:
				initLists(service.findUserAppraisalByUser(sessionView.getUser()));
				break;
			case 3:
				initLists(service.findUserAppraisalByHR(true, true, sessionView.getUser()));
				break;
			case 4:
				initLists(service.findUserAppraisalByLM(true, true, sessionView.getUser()));
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	protected Boolean canAccess() {
		return sessionView.getIsInternalAdmin();
	}

	public List<UserAppraisal> findUserAppraisalByUser() {

		return userAppraisalService.findUserAppraisalByUser(sessionView.getUser());
	}

	@Override
	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;

		filterBean(searchBean);
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsInternalAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.addHistory(new UserAppraisalHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(),
				getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	// sections
	public List<String> gettitleList() {
		return titleList;
	}

	public void setTitles(List<String> titleList) {
		this.titleList = titleList;
	}

	public List<Boolean> getEligibleList() {
		return eligibleList;
	}

	public void setEligibleList(List<Boolean> eligibleList) {
		this.eligibleList = eligibleList;
	}

	public List<Integer> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<Integer> weightList) {
		this.weightList = weightList;
	}

	public Boolean validateSections() {

		double wt = 0;
		for (int i = 0; i < titleList.size(); i++) {
			wt = wt + weightList.get(i);
		}
		if (wt != 100) {
			return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
		}
		return true;
	}

	public Boolean validateEligibleWeight() {
		for (int i = 0; i < titleList.size(); i++) {
			if ((eligibleList.get(i) == false) && (weightList.get(i) != 0)) {
				return FacesContextMessages.ErrorMessages("you should check eligible");
			}

		}
		return true;
	}

	public Boolean validateEligibleWeight1() {
		for (int i = 0; i < titleList.size(); i++) {
			if ((eligibleList.get(i) == true) && (weightList.get(i) == 0)) {
				return FacesContextMessages.ErrorMessages("you should enter the value");
			}

		}
		return true;
	}

	public Boolean canSaveSections() {
		return sessionView.getIsInternalAdmin();
	}

	public String saveSections() {
		if (!canSaveSections())
			return addParameters(listPage, "faces-redirect=true");

		if (!validateSections())
			return null;
		for (int i = 0; i < titleList.size(); i++) {
			Sections section = new Sections();

			section.setSectionsTitle(titleList.get(i));
			section.setWeight(weightList.get(i));
			section.setEligible(eligibleList.get(i));
			section.setUserappraisal(model);
			section.setSectionsNumber(i);
			sectionsService.save(section);

		}
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());

	}

	// Eligible True
	public List<Sections> findSectionsByEligible(UserAppraisal ua) {
		List<Sections> s = userAppraisalService.findSectionByEligible(ua);
		return s;
	}

	// Business Goals
	public Boolean validateBusinessMidYear() {
		Date dt = new Date();
		if (model.getAppraisal().getMidYearReviewEndDate().compareTo(dt) >= 0
				&& model.getAppraisal().getMidYearReviewStartDate().compareTo(dt) <= 0) {
			return false;
		}
		return true;
	}

	public Boolean validateBusinessFinalYear() {
		Date dt = new Date();
		if (model.getAppraisal().getEndYearSummaryEndDate().compareTo(dt) >= 0
				&& model.getAppraisal().getEndYearSummaryStartDate().compareTo(dt) <= 0) {
			return false;
		}
		return true;
	}

	public void validateRating(FacesContext context, UIComponent component, Object value) {
		int rating = (Integer) value;

		if (rating < 0 || rating > 100) {
			FacesMessage message = new FacesMessage("La valeur doit être entre 0 et 100.");
			throw new ValidatorException(message);
		}
	}

	public List<BusinessGoals> getBusinessGoalsList() {
		return businessGoalsList;
	}

	public void setBusinessGoalsList(List<BusinessGoals> businessGoalsList) {
		this.businessGoalsList = businessGoalsList;
	}


	public List<Sections> findSectionByUserAppraisal() {
		return sectionsService.findSectionsByUserAppraisal(model);
	}
	
	public List<BusinessGoals> findBusinessGoalSection0() {
		return userAppraisalService.findBusinessGoalsBySection0(model);
	}
	
	
	public List<SupplementaryGoals> findSuppGoalSection1() {
		return userAppraisalService.findSupGoalsBySection1(model);
	}
	
	public List<SupplementaryGoals> findSuppGoalSection2() {
		return userAppraisalService.findSupGoalsBySection2(model);
	}
	
	public List<SupplementaryGoals> findSuppGoalSection3() {
		return userAppraisalService.findSupGoalsBySection3(model);
	}
	
	public List<SupplementaryGoals> findSuppGoalSection4() {
		return userAppraisalService.findSupGoalsBySection4(model);
	}
	
	public List<SupplementaryGoals> findSuppGoalSection5() {
		return userAppraisalService.findSupGoalsBySection5(model);
	}
	
	
	public Boolean canAddBusiness() {
		return goaltitlecount<5;
	}

	public void addBusiness() {
		if (canAddBusiness()) {
			
			if(goaltitlecount>=1) {
				System.out.println("last item in businessGoalList : " + businessGoalsList.get(businessGoalsList.size()-1).getGoalTitle());
				goalTitleList.remove(businessGoalsList.get(businessGoalsList.size()-1).getGoalTitle());
			}
			
			businessGoalsList.add(new BusinessGoals(null, null, 0, findSectionId()));
			
			goaltitlecount++;
			// goalTitleList.remove(selectedGoalTitle);

			// RequestContext.getCurrentInstance().update("goalTitleCombo");

		}
	}
	
	public void removeBusinessGoal(BusinessGoals bg) {
		if(!goalTitleList.contains(bg.getGoalTitle()))
		goalTitleList.add(bg.getGoalTitle());
		
		businessGoalsList.remove(bg);	
		goaltitlecount--;
	}



	



	public Sections findSectionId() {
		return businessGoalsRepos.findSectionId(id);
	}

	public Boolean validateWeightBusinessGoals() {

		double weightTotal = 0;
		for (int i = 0; i < businessGoalsList.size(); i++) {
			weightTotal = weightTotal + businessGoalsList.get(i).getGoalWeight();
		}
		if (weightTotal != 100) {
			return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
		}
		return true;
	}

	public Boolean validateIsMidYear() {

		return true;
	}

	public Boolean canSaveBusinessGoals() {
		return sessionView.getIsMyPm();
	}

	public String saveBusinessGoals() {
		if (!canSaveBusinessGoals())
			return addParameters(listPage, "faces-redirect=true");

		if (!validateWeightBusinessGoals())
			return null;
		for (int i = 0; i < businessGoalsList.size(); i++) {

			BusinessGoals businessGoals = new BusinessGoals(businessGoalsList.get(i).getGoalDetails(),
					businessGoalsList.get(i).getGoalTitle(), businessGoalsList.get(i).getGoalWeight(),
					businessGoalsList.get(i).getMidYearReview(), businessGoalsList.get(i).getSummaryRaiting(),
					businessGoalsList.get(i).getSections()

			);

			businessGoalsService.save(businessGoals);

		}

		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());

	}

	// UserAppraisals status
	public Boolean canSubmited() {
		return UserAppraisalStatus.CREATED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void submited() {
		if (!canSubmited())
			return;
		model.setDateStatsSubmited(new Date());
		model.setUserStatsSubmited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from CREATED to SUBMITED"));
		service.save(model);
		model = service.findOne(model.getId());
	}
	
	public Boolean canEdited() {
		return UserAppraisalStatus.EDITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void edited() {
		if (!canEdited())
			return;

		model.setDateStatsEdited(new Date());
		model.setUserStatsEdited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.EDITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from CREATED to Edited"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canSubmiteduser() {
		return UserAppraisalStatus.EDITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void submiteduser() {
		if (!canSubmiteduser())
			return;

		model.setDateStatsSubmited(new Date());
		model.setUserStatsSubmited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from EDITED to SUBMITED"));

		service.save(model);
		model = service.findOne(model.getId());
	}
	
	

	public Boolean canApprovedLM() {
		return UserAppraisalStatus.SUBMITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void approvedLM() {
		if (!canApprovedLM())
			return;

		model.setDateStatsApprovedLM(new Date());
		model.setUserStatsApprovedLM(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from SUBMITED to APPROVED_LM"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canApproved() {
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void approved() {
		if (!canApproved())
			return;

		model.setDateStatsApproved(new Date());
		model.setUserStatsApproved(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.APPROVED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from APPROVED_LM to APPROVED"));

		service.save(model);
		model = service.findOne(model.getId());
	}
	
	public Boolean canMidSelfAssessment() {
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void midSelfAssessment() {
		if (!canMidSelfAssessment())
			return;

		model.setDateStatsSelfAssessmentMidYear(new Date());
		model.setUserStatsSelfAssessmentMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from APPROVED to MYR_SELF_ASSESSMENT"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canFinalSelfAssessment() {
		return UserAppraisalStatus.MYR_APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void finalSelfAssessment() {
		if (!canFinalSelfAssessment())
			return;

		model.setDateStatsSelfAssessmentFinalYear(new Date());
		model.setUserStatsSelfAssessmentFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from MYD_APPROVED to MYR_SELF_ASSESSMENT"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	
	public Boolean canSubmitedMidYear() {
		return UserAppraisalStatus.MYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	
	public void submitedMidYear() {
		if (!canSubmitedMidYear())
			return;

		model.setDateStatsSubmitedMidYear(new Date());
		model.setUserStatsSubmitedMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_MID_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from APPROVED to MID_YEAR_SUBMITED"));
		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canApprovedLMMidYear() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void approvedLMMidYear() {
		if (!canApprovedLMMidYear())
			return;

		model.setDateStatsApprovedLMMidYear(new Date());
		model.setUserStatsApprovedLMMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from Mid_YEAR_SUBMITED to MID_YEAR_APPROVED_LM"));

		service.save(model);
		model = service.findOne(model.getId());
	}


	public Boolean canSubmitedFinalYear() {
		return UserAppraisalStatus.FYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void submitedFinalYear() {
		if (!canSubmitedFinalYear())
			return;

		model.setDateStatsSubmitedFinalYear(new Date());
		model.setUserStatsSubmitedFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_FINAL_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from FYT_SELF_ASSESSMENT to FINAL_YEAR_SUBMITED"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canApprovedLMFinalYear() {
		return UserAppraisalStatus.SUBMITED_FINAL_YEAR.equals(model.getUserAppraisalStatus())
				&& sessionView.getIsMyPm();
	}

	public void approvedLMFinalYear() {
		if (!canApprovedLMFinalYear())
			return;

		model.setDateStatsApprovedLMFinalYear(new Date());
		model.setUserStatsApprovedLMFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from FINAL_YEAR_SUBMITED to FINAL_YEAR_APPROVED_LM"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean canClosed() {
		return UserAppraisalStatus.FYR_APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void closed() {
		if (!canClosed())
			return;

		model.setDateStatsClosed(new Date());
		model.setUserStatsClosed(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.CLOSED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				"change User AppraisalsStats from FINAL_YEAR_APPROVED to CLOSED"));

		service.save(model);
		model = service.findOne(model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			List<Sections> sec = sectionsService.findSectionsByUserAppraisal(model);
			if (sec.size() > 0) {
				for (Sections sect : sec) {

					sectionsService.delete(sect);
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

	public List<UserAppraisal> findAll() {
		return service.findAll();
	}

	public List<UserAppraisal> findByEmployOrAppraisee() {

		return userAppraisalService.findByEmployOrAppraisee(sessionView.getUser(), sessionView.getUser());
	}

	// files
	private UserAppraisalFile file;
	private String fileType;

	public Boolean canAddFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		UserAppraisalFile modelFile = new UserAppraisalFile(file, fileType, event.getFile().getFileName(),
				sessionView.getUser());
		model.addFile(modelFile);
		synchronized (UserAppraisalView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return sessionView.getIsInternalAdmin();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	public UserAppraisalFile getFile() {
		return file;
	}

	public void setFile(UserAppraisalFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// comments
	private UserAppraisalComment comment = new UserAppraisalComment();

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

	public UserAppraisalComment getComment() {
		return comment;
	}

	public void setComment(UserAppraisalComment comment) {
		this.comment = comment;
	}

	// Supplementary Goals
	public List<SupplementaryGoals> getSupplementaryGoalsList() {
		return supplementaryGoalsList;
	}

	public void setSupplementaryGoalsList(List<SupplementaryGoals> supplementaryGoalsList) {
		this.supplementaryGoalsList = supplementaryGoalsList;
	}

	public Boolean isElig(Integer idd) {
		for (Sections sections : userAppraisalRepos.findSectionByUserAppraisal(model)) {

			if (sections.getSectionsNumber() == idd && sections.getEligible() == true) {
				return true;
			}
		}
		return false;
	}

	public List<SupplementaryGoals> findSuppByGoaldId(Integer goalid) {

		List<SectionsData> secdata = new ArrayList<>();
		List<SupplementaryGoals> supplementaryGoalsList = new ArrayList<>();

		secdata = userAppraisalService.findSectionDataByGoalId(goalid);

		for (SectionsData se : secdata) {
			supplementaryGoalsList.add(new SupplementaryGoals(se));
			supplementaryGoalsListBg.add(new SupplementaryGoals(se));
		}

		return supplementaryGoalsList;
	}

	public Boolean canAddSupplementaryGoals() {
		return true;
	}

	public String saveSupplementaryGoals() {
		// if can etc validation
		for (int i = 0; i < supplementaryGoalsListBg.size(); i++) {
			SupplementaryGoals supplementaryGoals = new SupplementaryGoals(
					supplementaryGoalsListBg.get(i).getMidYearReview(),
					supplementaryGoalsListBg.get(i).getSummaryRaiting(), supplementaryGoalsListBg.get(i).getWeight(),
					supplementaryGoalsListBg.get(i).getSections(), supplementaryGoalsListBg.get(i).getSectionsData());
			supplementaryGoalsService.save(supplementaryGoals);
		}
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	// getters & setters
	public UserAppraisal getModel() {
		return model;
	}

	public void setModel(UserAppraisal model) {
		this.model = model;
	}

	@Transactional
	public String nextStep() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");

		switch (step) {
		case 1:

			if (!validateEligibleWeight())
				return null;
			if (!validateEligibleWeight1())
				return null;
			if (!validateSections())
				return null;
			saveSections();
			step++;
			break;

		case 2:
			if (!validateWeightBusinessGoals()) {
				return null;
			}
			saveBusinessGoals();
			step++;
			break;
		case 3:
			saveSupplementaryGoals();
			// step++;
			break;
		case 4:
			// saveSupplementaryGoals();
			step++;
			break;
		}
		return null;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public void previousStep() {
		if (step != 1)
			step--;
	}

}