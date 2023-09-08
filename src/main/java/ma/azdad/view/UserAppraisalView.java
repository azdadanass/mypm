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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.AppraisalsStatus;
import ma.azdad.model.BusinessGoals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.SupplementaryGoals;
import ma.azdad.model.ToNotify;
import ma.azdad.model.User;
import ma.azdad.model.UserAppraisal;
import ma.azdad.model.UserAppraisalComment;
import ma.azdad.model.UserAppraisalFile;
import ma.azdad.model.UserAppraisalHistory;
import ma.azdad.model.UserAppraisalStatus;
import ma.azdad.repos.BusinessGoalsRepos;
import ma.azdad.repos.SupplementaryGoalsRepos;
import ma.azdad.repos.UserAppraisalRepos;
import ma.azdad.service.BusinessGoalsService;
import ma.azdad.service.SectionsDataService;
import ma.azdad.service.SectionsService;
import ma.azdad.service.SupplementaryGoalsService;
import ma.azdad.service.UserAppraisalService;
import ma.azdad.service.UserService;
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
	UserService userService;

	@Autowired
	SupplementaryGoalsService supplementaryGoalsService;

	@Autowired
	UserAppraisalService userAppraisalService;

	@Autowired
	BusinessGoalsRepos businessGoalsRepos;

	@Autowired
	UserAppraisalRepos userAppraisalRepos;

	@Autowired
	SectionsDataService sectionsDataService;

	@Autowired
	SupplementaryGoalsRepos supplementaryGoalsRepos;

	private List<String> titleList;
	private List<Boolean> eligibleList;
	private List<Integer> weightList;
	private String toNotifyUserUsername;

	private List<SupplementaryGoals> suppl1 = new ArrayList<>();
	private List<SupplementaryGoals> suppl2 = new ArrayList<>();
	private List<SupplementaryGoals> suppl3 = new ArrayList<>();
	private List<SupplementaryGoals> suppl4 = new ArrayList<>();
	private List<SupplementaryGoals> suppl5 = new ArrayList<>();

	private List<SupplementaryGoals> editsuppl1 = new ArrayList<>();
	private List<SupplementaryGoals> editsuppl2 = new ArrayList<>();
	private List<SupplementaryGoals> editsuppl3 = new ArrayList<>();
	private List<SupplementaryGoals> editsuppl4 = new ArrayList<>();
	private List<SupplementaryGoals> editsuppl5 = new ArrayList<>();

	private ToNotify tnt = new ToNotify();

	private List<String> goalTitleList;

	private int goaltitlecount = 0;
	private List<BusinessGoals> businessGoalsList;
	private List<SupplementaryGoals> supplementaryGoalsList;
	private List<SupplementaryGoals> supplementaryGoalsListBg;
	private List<SectionsData> sectionsDatas;
	private List<UserAppraisal> userAppraisalList;
	private String selectedGoalTitle;
	private Integer isMid = 1;
	private List<Sections> sectionList;
	private List<Sections> sectionEditList;
	private List<BusinessGoals> businessGoalsListEdit;
	private List<SupplementaryGoals> supplementaryGoalsListEdit;
	private List<ToNotify> toNotifyList;

	public List<ToNotify> getToNotifyList() {
		return toNotifyList;
	}

	public void setToNotifyList(List<ToNotify> toNotifyList) {
		this.toNotifyList = toNotifyList;
	}

	@Override
	@PostConstruct
	public void init() {
		super.init();
		// chart*****************************
		toNotifyList = new ArrayList<>();
		businessGoalsListEdit = new ArrayList<>();
		editBusinessGoals();
		findToNotifyByUserAppraisal();
		sectionEditList = new ArrayList<>();
		editSection();
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

	public List<SupplementaryGoals> getSupplementaryGoalsListEdit() {
		return supplementaryGoalsListEdit;
	}

	public void setSupplementaryGoalsListEdit(List<SupplementaryGoals> supplementaryGoalsListEdit) {
		this.supplementaryGoalsListEdit = supplementaryGoalsListEdit;
	}

	public List<BusinessGoals> getBusinessGoalsListEdit() {
		return businessGoalsListEdit;
	}

	public void setBusinessGoalsListEdit(List<BusinessGoals> businessGoalsListEdit) {
		this.businessGoalsListEdit = businessGoalsListEdit;
	}

	public List<Sections> getSectionEditList() {
		return sectionEditList;
	}

	public void setSectionEditList(List<Sections> sectionEditList) {
		this.sectionEditList = sectionEditList;
	}

	public List<SupplementaryGoals> getSuppl1() {
		return suppl1;
	}

	public void setSuppl1(List<SupplementaryGoals> suppl1) {
		this.suppl1 = suppl1;
	}

	public List<SupplementaryGoals> getSuppl2() {
		return suppl2;
	}

	public void setSuppl2(List<SupplementaryGoals> suppl2) {
		this.suppl2 = suppl2;
	}

	public List<SupplementaryGoals> getSuppl3() {
		return suppl3;
	}

	public void setSuppl3(List<SupplementaryGoals> suppl3) {
		this.suppl3 = suppl3;
	}

	public List<SupplementaryGoals> getSuppl4() {
		return suppl4;
	}

	public void setSuppl4(List<SupplementaryGoals> suppl4) {
		this.suppl4 = suppl4;
	}

	public List<SupplementaryGoals> getSuppl5() {
		return suppl5;
	}

	public void setSuppl5(List<SupplementaryGoals> suppl5) {
		this.suppl5 = suppl5;
	}

	public List<SupplementaryGoals> getEditsuppl1() {
		return editsuppl1;
	}

	public void setEditsuppl1(List<SupplementaryGoals> editsuppl1) {
		this.editsuppl1 = editsuppl1;
	}

	public List<SupplementaryGoals> getEditsuppl2() {
		return editsuppl2;
	}

	public void setEditsuppl2(List<SupplementaryGoals> editsuppl2) {
		this.editsuppl2 = editsuppl2;
	}

	public List<SupplementaryGoals> getEditsuppl3() {
		return editsuppl3;
	}

	public void setEditsuppl3(List<SupplementaryGoals> editsuppl3) {
		this.editsuppl3 = editsuppl3;
	}

	public List<SupplementaryGoals> getEditsuppl4() {
		return editsuppl4;
	}

	public void setEditsuppl4(List<SupplementaryGoals> editsuppl4) {
		this.editsuppl4 = editsuppl4;
	}

	public List<SupplementaryGoals> getEditsuppl5() {
		return editsuppl5;
	}

	public void setEditsuppl5(List<SupplementaryGoals> editsuppl5) {
		this.editsuppl5 = editsuppl5;
	}

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

	// 3 stats Mid and Final And debut Status

	public void onDebut() {
		isMid = 1;
	}

	public void onMid() {
		isMid = 2;
	}

	public void onFinal() {
		isMid = 3;
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

		Integer task = getIntegerParameter("task");
		if (task != null) {
			switch (task) {
			case 1:
				initLists(service.findUserappraisalbyStats(sessionView.getUsername(), UserAppraisalStatus.EDITED));
				break;
			case 2:
				initLists(service.findUserappraisalRolebyStats(sessionView.getUsername(), UserAppraisalStatus.SUBMITED,
						UserAppraisalStatus.APPROVED_LM));
				break;
			case 3:
				initLists(service.findUserappraisalbyStats(sessionView.getUsername(), UserAppraisalStatus.MYR_EDITED));
				break;
			case 4:
				initLists(service.findUserappraisalbyStatsApproved(sessionView.getUsername(),
						UserAppraisalStatus.SUBMITED_MID_YEAR, UserAppraisalStatus.SUBMITED_FINAL_YEAR));
				break;
			case 5:
				initLists(service.findUserappraisalbyStats(sessionView.getUsername(), UserAppraisalStatus.FYR_EDITED));
				break;
			case 6:
				initLists(service.findUserappraisalbykeyworkerMid(sessionView.getUsername(),
						UserAppraisalStatus.SUBMITED_MID_YEAR));
				break;
			case 7:
				initLists(service.findUserappraisalbykeyworkerFinal(sessionView.getUsername(),
						UserAppraisalStatus.SUBMITED_FINAL_YEAR));
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

	public void editSupplementaryGoals() {
		editsuppl1 = new ArrayList<>();
		editsuppl2 = new ArrayList<>();
		editsuppl3 = new ArrayList<>();
		editsuppl4 = new ArrayList<>();
		editsuppl5 = new ArrayList<>();
		for (int i = 1; i < 6; i++) {

			for (SupplementaryGoals supplementary : supplementaryGoalsService.findByUserAppraisal(model, i)) {
				switch (i) {
				case 1:
					editsuppl1.add(supplementary);

					break;
				case 2:
					editsuppl2.add(supplementary);

					break;
				case 3:
					editsuppl3.add(supplementary);

					break;
				case 4:
					editsuppl4.add(supplementary);

					break;
				case 5:
					editsuppl5.add(supplementary);

					break;
				default:
					break;
				}
			}
		}

	}

	public void editBusinessGoals() {
		for (BusinessGoals business : businessGoalsRepos.findBySectionsUserAppraisal(model)) {
			businessGoalsListEdit.add(business);
		}
	}

	public void editSection() {
		for (Sections edit : sectionsService.findSectionsByUserAppraisal(model)) {
			sectionEditList.add(edit);
		}
	}

	public Boolean validateSections() {
		if (getIntegerParameter("isEdit") == 0) {
			double wt = 0;
			for (int i = 0; i < titleList.size(); i++) {
				wt = wt + weightList.get(i);
			}
			if (wt != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			double wt = 0;
			for (int i = 0; i < sectionEditList.size(); i++) {
				wt = wt + sectionEditList.get(i).getWeight();
			}
			if (wt != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
		}
		return true;

	}

	public Boolean validateEligibleWeight() {
		if (getIntegerParameter("isEdit") == 0) {
			for (int i = 0; i < titleList.size(); i++) {
				if ((eligibleList.get(i) == false) && (weightList.get(i) != 0)) {
					return FacesContextMessages.ErrorMessages("you should check eligible");
				}

			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			for (int i = 0; i < sectionEditList.size(); i++) {
				if ((sectionEditList.get(i).getEligible() == false) && (sectionEditList.get(i).getWeight() != 0)) {
					return FacesContextMessages.ErrorMessages("you should check eligible");
				}

			}
		}

		return true;
	}

	public Boolean validateEligibleWeight1() {
		if (getIntegerParameter("isEdit") == 0) {
			for (int i = 0; i < titleList.size(); i++) {
				if ((eligibleList.get(i) == true) && (weightList.get(i) == 0)) {
					return FacesContextMessages.ErrorMessages("you should enter the value");
				}

			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			for (int i = 0; i < sectionEditList.size(); i++) {
				if ((sectionEditList.get(i).getEligible() == true) && (sectionEditList.get(i).getWeight() == 0)) {
					return FacesContextMessages.ErrorMessages("you should enter the value");
				}

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
		if (getIntegerParameter("isEdit") == 0) {
			for (int i = 0; i < titleList.size(); i++) {
				Sections section = new Sections();

				section.setSectionsTitle(titleList.get(i));
				section.setWeight(weightList.get(i));
				section.setEligible(eligibleList.get(i));
				section.setUserappraisal(model);
				section.setSectionsNumber(i);
				sectionsService.save(section);

			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			for (int i = 0; i < sectionEditList.size(); i++) {
				Sections section = sectionEditList.get(i);

				section.setSectionsTitle(sectionEditList.get(i).getSectionsTitle());
				section.setWeight(sectionEditList.get(i).getWeight());
				section.setEligible(sectionEditList.get(i).getEligible());
				section.setUserappraisal(model);
				section.setSectionsNumber(i);
				sectionsService.save(section);

			}
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

	public List<Sections> findSectionByUserAppraisal(int idedit) {
		return sectionsService.findSectionsByUserAppraisal(userAppraisalService.findOne(idedit));
	}

	public Sections findSection(int i) {
		return sectionsService.findSectionsByUserAppraisal(model).get(i);
	}

	public List<BusinessGoals> findBusinessGoalSection0() {
		return userAppraisalService.findBusinessGoalsBySection0(model);
	}

	public void fillSupp1() {

		editSupplementaryGoals();
		List<SectionsData> secdata = userAppraisalService.findSectionDataByGoalId(1);
		suppl1 = new ArrayList<>();
		secdata = userAppraisalService.findSectionDataByGoalId(1);
		if (isElig(1)) {
			int k = 0;
			for (SectionsData se : secdata) {
				if (getIntegerParameter("isEdit") == 1) {
					if (editsuppl1.size() == 0) {
						SupplementaryGoals newGoal = new SupplementaryGoals(
								findSectionByNumberAndUserAppraisal(1, model), se);
						supplementaryGoalsService.save(newGoal);
						suppl1.add(newGoal);
					} else {
						suppl1.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(1, model), se,
								editsuppl1.get(k).getWeight()));
						k++;
					}
				} else if (getIntegerParameter("isEdit") == 0) {
					suppl1.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(1, model), se));
				}
			}
		}
	}

	public void fillSupp2() {
		editSupplementaryGoals();
		List<SectionsData> secdata = new ArrayList<>();
		suppl2 = new ArrayList<>();
		secdata = userAppraisalService.findSectionDataByGoalId(2);
		if (isElig(2)) {

			int k = 0;
			for (SectionsData se : secdata) {
				if (getIntegerParameter("isEdit") == 1) {

					if (editsuppl2.size() == 0) {

						SupplementaryGoals newGoal = new SupplementaryGoals(
								findSectionByNumberAndUserAppraisal(2, model), se);
						supplementaryGoalsService.save(newGoal);
						suppl2.add(newGoal);
					} else {

						suppl2.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(2, model), se,
								editsuppl2.get(k).getWeight()));
						k++;
					}
				} else if (getIntegerParameter("isEdit") == 0) {
					suppl2.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(2, model), se));

				}
			}
		}
	}

	public void fillSupp3() {

		editSupplementaryGoals();
		List<SectionsData> secdata = new ArrayList<>();
		suppl3 = new ArrayList<>();
		secdata = userAppraisalService.findSectionDataByGoalId(3);
		if (isElig(3)) {

			int k = 0;
			for (SectionsData se : secdata) {
				if (getIntegerParameter("isEdit") == 1) {

					if (editsuppl3.size() == 0) {

						SupplementaryGoals newGoal = new SupplementaryGoals(
								findSectionByNumberAndUserAppraisal(3, model), se);
						supplementaryGoalsService.save(newGoal);
						suppl3.add(newGoal);
					} else {

						suppl3.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(3, model), se,
								editsuppl3.get(k).getWeight()));
						k++;
					}

				} else if (getIntegerParameter("isEdit") == 0) {
					suppl3.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(3, model), se));
				}
			}
		}
	}

	public void fillSupp4() {

		editSupplementaryGoals();
		List<SectionsData> secdata = new ArrayList<>();
		suppl4 = new ArrayList<>();
		secdata = userAppraisalService.findSectionDataByGoalId(4);
		if (isElig(4)) {

			int k = 0;
			for (SectionsData se : secdata) {
				if (getIntegerParameter("isEdit") == 1) {

					if (editsuppl4.size() == 0) {

						SupplementaryGoals newGoal = new SupplementaryGoals(
								findSectionByNumberAndUserAppraisal(4, model), se);
						supplementaryGoalsService.save(newGoal);
						suppl4.add(newGoal);
					} else {

						suppl4.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(4, model), se,
								editsuppl4.get(k).getWeight()));
						k++;
					}

				} else if (getIntegerParameter("isEdit") == 0) {
					suppl4.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(4, model), se));

				}
			}
		}
	}

	public void fillSupp5() {

		editSupplementaryGoals();
		List<SectionsData> secdata = new ArrayList<>();
		suppl5 = new ArrayList<>();
		secdata = userAppraisalService.findSectionDataByGoalId(5);
		if (isElig(5)) {

			int k = 0;
			for (SectionsData se : secdata) {
				if (getIntegerParameter("isEdit") == 1) {

					if (editsuppl5.size() == 0) {

						SupplementaryGoals newGoal = new SupplementaryGoals(
								findSectionByNumberAndUserAppraisal(5, model), se);
						supplementaryGoalsService.save(newGoal);
						suppl5.add(newGoal);
					} else {

						suppl5.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(5, model), se,
								editsuppl5.get(k).getWeight()));
						k++;
					}

				} else if (getIntegerParameter("isEdit") == 0) {
					suppl5.add(new SupplementaryGoals(findSectionByNumberAndUserAppraisal(5, model), se));

				}
			}
		}
	}

	public Integer countSections(UserAppraisal u) {
		return userAppraisalService.countSections(u);
	}

	public Boolean canAddBusiness() {
		if (getIntegerParameter("isEdit") == 1) {
			goaltitlecount = businessGoalsListEdit.size();

		}
		return goaltitlecount < 5;
	}

	public void addBusiness() {
		if (canAddBusiness()) {

			if (getIntegerParameter("isEdit") == 0) {
				if (goaltitlecount >= 1) {

					goalTitleList.remove(businessGoalsList.get(businessGoalsList.size() - 1).getGoalTitle());
				}
				businessGoalsList.add(new BusinessGoals(null, null, 0, findSectionId()));
				goaltitlecount++;

			}
			if (getIntegerParameter("isEdit") == 1) {
				if (goaltitlecount >= 1) {

					goalTitleList.remove(businessGoalsListEdit.get(businessGoalsListEdit.size() - 1).getGoalTitle());
				}
				businessGoalsListEdit.add(new BusinessGoals(null, null, 0, findSectionId()));
				goaltitlecount++;
			}
		}
	}

	public void removeBusinessGoal(BusinessGoals bg) throws DataIntegrityViolationException, Exception {

		if (getIntegerParameter("isEdit") == 1) {

			if (!goalTitleList.contains(bg.getGoalTitle()))
				goalTitleList.add(bg.getGoalTitle());

			businessGoalsService.delete(bg);
			businessGoalsListEdit.remove(bg);
			goaltitlecount--;
		}
		if (getIntegerParameter("isEdit") == 0) {
			if (!goalTitleList.contains(bg.getGoalTitle()))
				goalTitleList.add(bg.getGoalTitle());

			businessGoalsService.delete(bg);
			businessGoalsListEdit.remove(bg);
			goaltitlecount--;
		}

	}

	public Sections findSectionId() {
		return businessGoalsRepos.findSectionId(id);
	}

	public Boolean validateWeightBusinessGoals() {
		if (getIntegerParameter("isEdit") == 0) {
			double weightTotal = 0;
			for (int i = 0; i < businessGoalsListEdit.size(); i++) {
				weightTotal = weightTotal + businessGoalsListEdit.get(i).getGoalWeight();
			}
			if (weightTotal != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			double weightTotal = 0;
			for (int i = 0; i < businessGoalsListEdit.size(); i++) {
				weightTotal = weightTotal + businessGoalsListEdit.get(i).getGoalWeight();
			}
			if (weightTotal != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
		}
		return true;
	}

	public Boolean validateWeightSuppGoals() {

		if (getIntegerParameter("isEdit") == 0) {
			int weightTotal1 = 0;
			int weightTotal2 = 0;
			int weightTotal3 = 0;
			int weightTotal4 = 0;
			int weightTotal5 = 0;

			for (int i = 0; i < suppl1.size(); i++) {
				weightTotal1 = weightTotal1 + suppl1.get(i).getWeight();
			}
			for (int i = 0; i < suppl2.size(); i++) {
				weightTotal2 = weightTotal2 + suppl2.get(i).getWeight();
			}
			for (int i = 0; i < suppl3.size(); i++) {
				weightTotal3 = weightTotal3 + suppl3.get(i).getWeight();
			}
			for (int i = 0; i < suppl4.size(); i++) {
				weightTotal4 = weightTotal4 + suppl4.get(i).getWeight();
			}
			for (int i = 0; i < suppl5.size(); i++) {
				weightTotal5 = weightTotal5 + suppl5.get(i).getWeight();
			}

			if (suppl1.size() > 0 && weightTotal1 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl2.size() > 0 && weightTotal2 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl3.size() > 0 && weightTotal3 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl4.size() > 0 && weightTotal4 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl5.size() > 0 && weightTotal5 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}

		}
		if (getIntegerParameter("isEdit") == 1) {

			int weightTotal1 = 0;
			int weightTotal2 = 0;
			int weightTotal3 = 0;
			int weightTotal4 = 0;
			int weightTotal5 = 0;

			for (int i = 0; i < suppl1.size(); i++) {

				weightTotal1 = weightTotal1 + suppl1.get(i).getWeight();
			}
			for (int i = 0; i < suppl2.size(); i++) {
				weightTotal2 = weightTotal2 + suppl2.get(i).getWeight();
			}
			for (int i = 0; i < suppl3.size(); i++) {
				weightTotal3 = weightTotal3 + suppl3.get(i).getWeight();
			}
			for (int i = 0; i < suppl4.size(); i++) {
				weightTotal4 = weightTotal4 + suppl4.get(i).getWeight();
			}
			for (int i = 0; i < suppl5.size(); i++) {
				weightTotal5 = weightTotal5 + suppl5.get(i).getWeight();
			}

			if (suppl1.size() > 0 && weightTotal1 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl2.size() > 0 && weightTotal2 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl3.size() > 0 && weightTotal3 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl4.size() > 0 && weightTotal4 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (suppl5.size() > 0 && weightTotal5 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
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
		if (getIntegerParameter("isEdit") == 0) {
			for (int i = 0; i < businessGoalsList.size(); i++) {

				BusinessGoals businessGoals = new BusinessGoals(businessGoalsList.get(i).getGoalDetails(),
						businessGoalsList.get(i).getGoalTitle(), businessGoalsList.get(i).getGoalWeight(),
						businessGoalsList.get(i).getMidYearReview(), businessGoalsList.get(i).getSummaryRaiting(),
						businessGoalsList.get(i).getSections());
				businessGoalsService.save(businessGoals);
			}

		}
		if (getIntegerParameter("isEdit") == 1) {
			for (int i = 0; i < businessGoalsListEdit.size(); i++) {

				BusinessGoals businessGoals = businessGoalsListEdit.get(i);
				if (businessGoals.getId() == null) {
					BusinessGoals bg = new BusinessGoals(businessGoals.getGoalDetails(), businessGoals.getGoalTitle(),
							businessGoals.getGoalWeight(), businessGoals.getMidYearReview(),
							businessGoals.getSummaryRaiting(), businessGoals.getSections()

					);
					businessGoalsService.save(bg);
				} else {
					businessGoalsService.save(businessGoals);
				}
			}
		}
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	// UserAppraisals status
	public Boolean canSubmited() {
		return UserAppraisalStatus.EDITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getEmploy().equals(sessionView.getUser());
	}

	public void submited() {
		if (!canSubmited())
			return;
		model.setDateStatsSubmited(new Date());
		model.setUserStatsSubmited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has submitted " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canEdited() {
		return UserAppraisalStatus.CREATED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void edited() {
		if (!canEdited())
			return;

		model.setDateStatsEdited(new Date());
		model.setUserStatsEdited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.EDITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Edited " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApprovedLM() {
		return UserAppraisalStatus.SUBMITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager() && (model.getUserStatsApprovedLM().equals(sessionView.getUser()));
	}

	public void approvedLM() {
		if (!canApprovedLM())
			return;

		model.setDateStatsApprovedLM(new Date());
		model.setUserStatsApprovedLM(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejectedLM() {
		return UserAppraisalStatus.SUBMITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager() && (model.getUserStatsApprovedLM().equals(sessionView.getUser()));
	}

	public void rejectedLM() {
		if (!canRejectedLM())
			return;

		model.setDateStatsSubmited(null);
		model.setDateStatsApprovedLM(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.EDITED);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApproved() {
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmHr() && (model.getUserStatsApproved().equals(sessionView.getUser()));
	}

	public void approved() {
		if (!canApproved())
			return;

		model.setDateStatsApproved(new Date());
		model.setUserStatsApproved(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.APPROVED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejected() {
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmHr() && (model.getUserStatsApproved().equals(sessionView.getUser()));
	}

	public void rejected() {
		if (!canRejected())
			return;

		model.setDateStatsSubmited(null);
		model.setDateStatsApprovedLM(null);
		model.setDateStatsApproved(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.EDITED);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canMYREdited() {
		return UserAppraisalStatus.MYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus())
				&& sessionView.getIsMyPm();
	}

	public void MYRedited() {
		if (!canMYREdited())
			return;

		model.setDateStatsMidEdited(new Date());
		model.setUserStatsMidEdited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_EDITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Edited " + model.getEmploy().getFullName() + " MID YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canSubmitedMidYear() {
		return UserAppraisalStatus.MYR_EDITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm();
	}

	public void submitedMidYear() {
		if (!canSubmitedMidYear())
			return;

		model.setDateStatsSubmitedMidYear(new Date());
		model.setUserStatsSubmitedMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_MID_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Submitted " + model.getEmploy().getFullName() + "MID YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApprovedLMMidYear() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW)
				&& (model.getUserStatsApprovedLMMidYear().equals(sessionView.getUser()));
	}

	public void approvedLMMidYear() {
		if (!canApprovedLMMidYear())
			return;

		model.setDateStatsApprovedLMMidYear(new Date());
		model.setUserStatsApprovedLMMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " MID YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejectedLMMid() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW)
				&& (model.getUserStatsApprovedLMMidYear().equals(sessionView.getUser()));
	}

	public void rejectedLMMid() {
		if (!canRejectedLMMid())
			return;

		model.setDateStatsSubmitedMidYear(null);
		model.setDateStatsApprovedLMMidYear(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_EDITED);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.MYR_REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + "MID YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	// ################################# Auto Mid/Final YEAr
	// ###############################################

	@Scheduled(fixedRate = 30000)
	public void autoMidFinalAssesment() {
		for (UserAppraisal ap : findAll()) {

			midSelfAssessment(ap);
			finalSelfAssessment(ap);

		}
	}

	// ################################# Final YEAr
	// ###############################################

	public Boolean canFinalSelfAssessment(UserAppraisal us) {
		return UserAppraisalStatus.MYR_APPROVED_LM.equals(us.getUserAppraisalStatus())
				&& us.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	// @Scheduled(fixedRate = 30000)
	public void finalSelfAssessment(UserAppraisal us) {
		if (!canFinalSelfAssessment(us))
			return;

		us.setDateStatsSelfAssessmentFinalYear(new Date());
		us.setUserAppraisalStatus(UserAppraisalStatus.FYR_SELF_ASSESSMENT);
		us.addHistory(new UserAppraisalHistory(us.getUserAppraisalStatus().getValue(), us.getEmploy(),
				us.getEmploy() + " has change " + us.getEmploy() + " to FINAL YEAR Appraisal"));
		service.save(us);
		// model = service.findOne(model.getId());
		evictCache();
	}

	// ############### MID YEAr ########################

	public Boolean canMidSelfAssessment(UserAppraisal us) {
		return (UserAppraisalStatus.APPROVED.equals(us.getUserAppraisalStatus()))
				&& us.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW);
	}

	public synchronized void midSelfAssessment(UserAppraisal us) {
		if (!canMidSelfAssessment(us))
			return;

		us.setDateStatsSelfAssessmentMidYear(new Date());
		us.setUserAppraisalStatus(UserAppraisalStatus.MYR_SELF_ASSESSMENT);
		us.addHistory(new UserAppraisalHistory(us.getUserAppraisalStatus().getValue(), us.getEmploy(),
				us.getEmploy() + " has Change " + us.getEmploy().getFullName() + " to MID YEAR Appraisal"));
		service.save(us);

		evictCache();
	}

	public Boolean canFYREdited() {
		return UserAppraisalStatus.FYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus())
				&& sessionView.getIsMyPm();
	}

	public void FYRedited() {
		if (!canFYREdited())
			return;

		model.setDateStatsFinalEdited(new Date());
		model.setUserStatsFinalEdited(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_EDITED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Edited " + model.getEmploy().getFullName() + " FINAL YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canSubmitedFinalYear() {
		return UserAppraisalStatus.FYR_EDITED.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void submitedFinalYear() {
		if (!canSubmitedFinalYear())
			return;

		model.setDateStatsSubmitedFinalYear(new Date());
		model.setUserStatsSubmitedFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_FINAL_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Submitted " + model.getEmploy().getFullName() + " FINAL YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApprovedLMFinalYear() {
		return UserAppraisalStatus.SUBMITED_FINAL_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW)
				&& (model.getUserStatsApprovedLMFinalYear().equals(sessionView.getUser()));
	}

	public void approvedLMFinalYear() {
		if (!canApprovedLMFinalYear())
			return;

		model.setDateStatsApprovedLMFinalYear(new Date());
		model.setUserStatsApprovedLMFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " FINAL YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejectedLMFinal() {
		return UserAppraisalStatus.SUBMITED_FINAL_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmLineManager()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW)
				&& (model.getUserStatsApprovedLMFinalYear().equals(sessionView.getUser()));
	}

	public void rejectedLMFinal() {
		if (!canRejectedLMFinal())
			return;

		model.setDateStatsSubmitedFinalYear(null);
		model.setDateStatsApprovedLMFinalYear(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_EDITED);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.FYR_REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + "FINAL YEAR Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canClosed() {
		return UserAppraisalStatus.FYR_APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void closed() {
		if (!canClosed())
			return;

		model.setDateStatsClosed(new Date());
		model.setUserStatsClosed(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.CLOSED);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Closed " + model.getEmploy().getFullName() + " Appraisal"));
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

					List<BusinessGoals> bgoal = businessGoalsService.findBySections(sect);
					List<SupplementaryGoals> spgoal = userAppraisalRepos.findSuppByUser(model);
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

		String listPage = "userAppraisalList.xhtml";
		String parameters = "faces-redirect=true&pageIndex=1";
		return addParameters(listPage, parameters);
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
	private UserAppraisalComment userAppraisalComment = new UserAppraisalComment();

	public UserAppraisalComment getUserAppraisalComment() {
		return userAppraisalComment;
	}

	public void setUserAppraisalComment(UserAppraisalComment userAppraisalComment) {
		this.userAppraisalComment = userAppraisalComment;
	}

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
		}

		return supplementaryGoalsList;
	}

	// SUUPLEMENTARY GOALS
	public List<SupplementaryGoals> findSuppGoalSection(int number) {
		return userAppraisalService.findSupGoalsBySection(number, model);
	}

	public Sections findSectionByNumberAndUserAppraisal(int number, UserAppraisal us) {
		return businessGoalsRepos.findSectionByNumberAndUserAppraisal(number, us);
	}

	public Date findHireDate(String u) {
		return service.findHireDate(u);
	}

	public void initSuppGoals() {

		supplementaryGoalsListBg = new ArrayList<>();
		if (getIntegerParameter("isEdit") == 1) {

			for (Sections section : sectionEditList) {
				if (section.getEligible() == false) {
					for (SupplementaryGoals supp : userAppraisalService
							.findSupGoalsBySection(section.getSectionsNumber(), model)) {

						try {
							supplementaryGoalsService.delete(supp);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void initBg() {
		if (businessGoalsList.size() > 0) {
			for (BusinessGoals bg : businessGoalsList) {

				bg.setSections(findSectionId());
			}
		}

		if (businessGoalsListEdit.size() > 0) {
			for (BusinessGoals bg : businessGoalsListEdit) {
				bg.setSections(findSectionId());

			}
			for (int i = 0; i < businessGoalsListEdit.size(); i++) {
				goalTitleList.remove(businessGoalsListEdit.get(i).getGoalTitle());
			}
		}
	}

	public void removeBgFromDB() {

		List<BusinessGoals> list = businessGoalsRepos.findBySectionsUserAppraisal(model);

		for (BusinessGoals businessGoals : list) {

			try {
				businessGoalsService.delete(businessGoals);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeSections() {

		List<Sections> sections = sectionsService.findSectionsByUserAppraisal(model);

		for (Sections section : sections) {

			try {
				sectionsService.delete(section);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void removeSuppGoal() {

		List<SupplementaryGoals> list = userAppraisalRepos.findSuppByUser(model);

		for (SupplementaryGoals supp : list) {

			try {
				supplementaryGoalsService.delete(supp);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public Boolean canAddSupplementaryGoals() {
		return true;
	}

	public String saveSupplementaryGoals() {

		if (getIntegerParameter("isEdit") == 1) {
			int l = 0;
			List<SupplementaryGoals> spl1 = supplementaryGoalsService.findByUserAppraisal(model, 1);

			if (spl1.size() > 0) {
				for (SupplementaryGoals supplementaryGoals : spl1) {
					supplementaryGoals.setWeight(suppl1.get(l).getWeight());
					l++;
					supplementaryGoalsService.save(supplementaryGoals);
				}

			} else {
				for (SupplementaryGoals s : suppl1) {
					supplementaryGoalsService.save(s);
				}
			}

			l = 0;
			List<SupplementaryGoals> spl2 = supplementaryGoalsService.findByUserAppraisal(model, 2);

			if (isElig(2)) {
				if (spl2.size() > 0) {
					for (SupplementaryGoals supplementaryGoals : spl2) {
						supplementaryGoals.setWeight(suppl2.get(l).getWeight());
						l++;
						supplementaryGoalsService.save(supplementaryGoals);
					}

				} else {
					for (SupplementaryGoals s : suppl2) {
						supplementaryGoalsService.save(s);
					}
				}
			}

			l = 0;
			List<SupplementaryGoals> spl3 = supplementaryGoalsService.findByUserAppraisal(model, 3);

			if (spl3.size() > 0) {
				for (SupplementaryGoals supplementaryGoals : spl3) {
					supplementaryGoals.setWeight(suppl3.get(l).getWeight());
					l++;
					supplementaryGoalsService.save(supplementaryGoals);
				}

			} else {
				for (SupplementaryGoals s : suppl3) {
					supplementaryGoalsService.save(s);
				}
			}

			l = 0;
			List<SupplementaryGoals> spl4 = supplementaryGoalsService.findByUserAppraisal(model, 4);

			if (spl4.size() > 0) {
				for (SupplementaryGoals supplementaryGoals : spl4) {
					supplementaryGoals.setWeight(suppl4.get(l).getWeight());
					l++;
					supplementaryGoalsService.save(supplementaryGoals);
				}

			} else {
				for (SupplementaryGoals s : suppl4) {
					supplementaryGoalsService.save(s);
				}
			}

			l = 0;
			List<SupplementaryGoals> spl5 = supplementaryGoalsService.findByUserAppraisal(model, 5);

			if (spl5.size() > 0) {
				for (SupplementaryGoals supplementaryGoals : spl5) {
					supplementaryGoals.setWeight(suppl5.get(l).getWeight());
					l++;
					supplementaryGoalsService.save(supplementaryGoals);
				}

			} else {
				for (SupplementaryGoals s : suppl5) {
					supplementaryGoalsService.save(s);
				}
			}

		} else if (getIntegerParameter("isEdit") == 0) {

			for (SupplementaryGoals sup : suppl1) {
				supplementaryGoalsService.save(sup);
			}
			for (SupplementaryGoals sup : suppl2) {
				supplementaryGoalsService.save(sup);
			}
			for (SupplementaryGoals sup : suppl3) {
				supplementaryGoalsService.save(sup);
			}
			for (SupplementaryGoals sup : suppl4) {
				supplementaryGoalsService.save(sup);
			}
			for (SupplementaryGoals sup : suppl5) {
				supplementaryGoalsService.save(sup);
			}
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

	@Override
	protected Integer getIntegerParameter(String name) {
		return super.getIntegerParameter(name);
	}

	// ============== Keyworker Code

	public String getToNotifyUserUsername() {
		return toNotifyUserUsername;
	}

	public void setToNotifyUserUsername(String toNotifyUserUsername) {
		this.toNotifyUserUsername = toNotifyUserUsername;
	}

	public void addToNotifyItem() {

		User toNotifyUser = userService.findOne(toNotifyUserUsername);
		if (model.getToNotifyList().stream()
				.filter(i -> i.getInternalResource().getUsername().equals(toNotifyUser.getUsername())).count() == 0)
			model.getToNotifyList().add(new ToNotify(toNotifyUser, model));
		model = service.saveAndRefresh(model);

	}

	public void removeToNotifyItem(int index) {

		if (getIntegerParameter("isEdit") == 1) {
			model.getToNotifyList().get(index).setUserAppraisal(null);
			model.getToNotifyList().remove(index);
			model = service.saveAndRefresh(model);

		} else if (getIntegerParameter("isEdit") == 0) {
			model.getToNotifyList().get(index).setUserAppraisal(null);
			model.getToNotifyList().remove(index);
			model = service.saveAndRefresh(model);

		}
	}

	@Transactional
	public String nextStep() throws IOException {
		if (!canSave()) {
			return addParameters(listPage, "faces-redirect=true");

		}
		switch (step) {
		case 1:
			if (!validateEligibleWeight())
				return null;
			if (!validateEligibleWeight1())
				return null;
			if (!validateSections())
				return null;

			saveSections();
			fillSupp1();
			fillSupp2();
			fillSupp3();
			fillSupp4();
			fillSupp5();
			initBg();
			initSuppGoals();
			step++;
			break;

		case 2:
			if (!validateWeightBusinessGoals()) {
				return null;
			}
			for (BusinessGoals b1 : businessGoalsListEdit) {
				businessGoalsService.save(b1);

			}
			editSupplementaryGoals();
			step++;
			break;
		case 3:
			if (!validateWeightSuppGoals()) {
				return null;
			}
			saveSupplementaryGoals();
			fillSupp1();
			fillSupp2();
			fillSupp3();
			fillSupp4();
			fillSupp5();
			step++;
			break;
		case 4:
			step++;
			break;
		case 5:
			if (!StringUtils.isBlank(userAppraisalComment.getContent())) {

				userAppraisalComment.setDate(new Date());
				userAppraisalComment.setTitle(
						(model.getUserAppraisalStatus().getValue().equals(UserAppraisalStatus.CREATED.getValue()))
								? "User Appraisal Creation"
								: "User Appraisal Edition");
				userAppraisalComment.setParent(model);
				userAppraisalComment.setUser(sessionView.getUser());
				model.addComment(userAppraisalComment);
				model = service.saveAndRefresh(model);

			}
			edited();
			step++;
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("addEditUserAppraisal.xhtml?id=" + model.getId() + "&pageIndex=1");
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
		if (step != 1) {
			if (step == 2) {

				initSuppGoals();
				removeBgFromDB();
				removeSections();
			}
			if (step == 3) {
				removeBgFromDB();
				removeSuppGoal();
			}
			if (step == 4) {
				removeSuppGoal();
			}
			step--;
		}
	}

	// Step Final Review

	private List<BusinessGoals> finalBusinessGoal;
	private List<SupplementaryGoals> finalSupplementaryGoal;
	private List<SupplementaryGoals> finalsup1;
	private List<SupplementaryGoals> finalsup2;
	private List<SupplementaryGoals> finalsup3;
	private List<SupplementaryGoals> finalsup4;
	private List<SupplementaryGoals> finalsup5;

	public List<BusinessGoals> getFinalBusinessGoal() {
		return finalBusinessGoal;
	}

	public void setFinalBusinessGoal(List<BusinessGoals> finalBusinessGoal) {
		this.finalBusinessGoal = finalBusinessGoal;
	}

	public List<SupplementaryGoals> getFinalSupplementaryGoal() {
		return finalSupplementaryGoal;
	}

	public void setFinalSupplementaryGoal(List<SupplementaryGoals> finalSupplementaryGoal) {
		this.finalSupplementaryGoal = finalSupplementaryGoal;
	}

	public List<SupplementaryGoals> getFinalsup1() {
		return finalsup1;
	}

	public void setFinalsup1(List<SupplementaryGoals> finalsup1) {
		this.finalsup1 = finalsup1;
	}

	public List<SupplementaryGoals> getFinalsup2() {
		return finalsup2;
	}

	public void setFinalsup2(List<SupplementaryGoals> finalsup2) {
		this.finalsup2 = finalsup2;
	}

	public List<SupplementaryGoals> getFinalsup3() {
		return finalsup3;
	}

	public void setFinalsup3(List<SupplementaryGoals> finalsup3) {
		this.finalsup3 = finalsup3;
	}

	public List<SupplementaryGoals> getFinalsup4() {
		return finalsup4;
	}

	public void setFinalsup4(List<SupplementaryGoals> finalsup4) {
		this.finalsup4 = finalsup4;
	}

	public List<SupplementaryGoals> getFinalsup5() {
		return finalsup5;
	}

	public void setFinalsup5(List<SupplementaryGoals> finalsup5) {
		this.finalsup5 = finalsup5;
	}

	public void initFinalBusinessGoal() {
		model = service.findOne(id);
		finalBusinessGoal = new ArrayList<>();
		for (BusinessGoals business : businessGoalsRepos.findBySectionsUserAppraisal(model)) {
			finalBusinessGoal.add(business);
		}

	}

	public void initFinalSupplementaryGoal() {
		model = service.findOne(id);
		finalSupplementaryGoal = new ArrayList<>();
		finalsup1 = new ArrayList<>();
		finalsup2 = new ArrayList<>();
		finalsup3 = new ArrayList<>();
		finalsup4 = new ArrayList<>();
		finalsup5 = new ArrayList<>();

		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(1, model)) {
			finalsup1.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(2, model)) {
			finalsup2.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(3, model)) {
			finalsup3.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(4, model)) {
			finalsup4.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(5, model)) {
			finalsup5.add(supplementaryGoals);
		}
	}

	public Boolean validateWeightFinalBusinessGoals() {
		for (int i = 0; i < finalBusinessGoal.size(); i++) {
			if (finalBusinessGoal.get(i).getSummaryRaiting() < 0
					|| finalBusinessGoal.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of Final year should be between 0 and 100");
			}
		}
		return true;
	}

	public Boolean validateWeightFinalSupplementaryGoals() {
		for (int i = 0; i < finalsup1.size(); i++) {
			if (finalsup1.get(i).getSummaryRaiting() < 0 || finalsup1.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of final year should be between 0 and 100");
			}
		}
		for (int i = 0; i < finalsup2.size(); i++) {
			if (finalsup2.get(i).getSummaryRaiting() < 0 || finalsup2.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of final year should be between 0 and 100");
			}
		}
		for (int i = 0; i < finalsup3.size(); i++) {
			if (finalsup3.get(i).getSummaryRaiting() < 0 || finalsup3.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of final year should be between 0 and 100");
			}
		}
		for (int i = 0; i < finalsup4.size(); i++) {
			if (finalsup4.get(i).getSummaryRaiting() < 0 || finalsup4.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of final year should be between 0 and 100");
			}
		}
		for (int i = 0; i < finalsup5.size(); i++) {
			if (finalsup5.get(i).getSummaryRaiting() < 0 || finalsup5.get(i).getSummaryRaiting() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of final year should be between 0 and 100");
			}
		}
		return true;
	}

	public Boolean canSaveFinalBusinessGoals() {
		return sessionView.getIsMyPm();
	}

	public void saveFinalBusinessGoals() {

		for (int i = 0; i < finalBusinessGoal.size(); i++) {

			BusinessGoals bg = businessGoalsService.findOne(finalBusinessGoal.get(i).getId());
			bg.setSummaryRaiting(finalBusinessGoal.get(i).getSummaryRaiting());
			businessGoalsService.save(bg);
		}
	}

	public void saveFinalSupplementaryGoals() {

		int l = 0;
		if (finalsup1.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : finalsup1) {
				supplementaryGoals.setSummaryRaiting(finalsup1.get(l).getSummaryRaiting());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (finalsup2.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : finalsup2) {
				supplementaryGoals.setSummaryRaiting(finalsup2.get(l).getSummaryRaiting());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (finalsup3.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : finalsup3) {
				supplementaryGoals.setSummaryRaiting(finalsup3.get(l).getSummaryRaiting());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (finalsup4.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : finalsup4) {
				supplementaryGoals.setSummaryRaiting(finalsup4.get(l).getSummaryRaiting());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (finalsup5.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : finalsup5) {
				supplementaryGoals.setSummaryRaiting(finalsup5.get(l).getSummaryRaiting());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
	}

	public UserAppraisalComment getCommentByTitle1() {
		model = service.findOne(id);
		return userAppraisalRepos.findCommentByTitle1(model);
	}

	public Boolean existComment1() {
		if (getCommentByTitle1() == null) {
			return false;
		}
		return true;
	}

	private int stepFinal = 1;

	public int getStepFinal() {
		return stepFinal;
	}

	public void setStepFinal(int stepFinal) {
		this.stepFinal = stepFinal;
	}

	@Transactional
	public String nextStepFinal() throws IOException {
		switch (stepFinal) {

		case 1:
			initFinalSupplementaryGoal();
			if (!validateWeightFinalBusinessGoals())
				return null;
			stepFinal++;
			saveFinalBusinessGoals();
			break;

		case 2:
			if (!validateWeightFinalSupplementaryGoals())
				return null;
			saveFinalSupplementaryGoals();

			stepFinal++;
			break;
		case 3:
			if (!StringUtils.isBlank(userAppraisalComment.getContent())) {

				userAppraisalComment.setDate(new Date());
				userAppraisalComment.setTitle((model.getUserAppraisalStatus().getValue()
						.equals(UserAppraisalStatus.FYR_SELF_ASSESSMENT.getValue())) ? "User Appraisal FYR Creation"
								: "User Appraisal FYR Edition");
				userAppraisalComment.setParent(model);
				userAppraisalComment.setUser(sessionView.getUser());
				model.addComment(userAppraisalComment);
				model = service.saveAndRefresh(model);
			}
			stepFinal++;
			FYRedited();
			finalCalculBG();
			finalCalculSupp1();
			finalCalculSupp2();
			finalCalculSupp3();
			finalCalculSupp4();
			finalCalculSupp5();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("addEditUserAppraisal.xhtml?id=" + model.getId() + "&pageIndex=1");
			break;
		}
		return null;
	}

	public void previousStepFinal() {
		if (stepFinal != 1) {
			if (stepFinal == 2) {

			}
			if (stepFinal == 3) {

			}
			stepFinal--;
		}
	}

	// StepMid Review

	private List<BusinessGoals> midBusinessGoal;
	private List<SupplementaryGoals> midSupplementaryGoal;
	private List<SupplementaryGoals> midsup1;
	private List<SupplementaryGoals> midsup2;
	private List<SupplementaryGoals> midsup3;
	private List<SupplementaryGoals> midsup4;
	private List<SupplementaryGoals> midsup5;

	public List<SupplementaryGoals> getMidsup1() {
		return midsup1;
	}

	public void setMidsup1(List<SupplementaryGoals> midsup1) {
		this.midsup1 = midsup1;
	}

	public List<SupplementaryGoals> getMidsup2() {
		return midsup2;
	}

	public List<SupplementaryGoals> getMidsup3() {
		return midsup3;
	}

	public void setMidsup3(List<SupplementaryGoals> midsup3) {
		this.midsup3 = midsup3;
	}

	public List<SupplementaryGoals> getMidsup4() {
		return midsup4;
	}

	public void setMidsup4(List<SupplementaryGoals> midsup4) {
		this.midsup4 = midsup4;
	}

	public List<SupplementaryGoals> getMidsup5() {
		return midsup5;
	}

	public void setMidsup5(List<SupplementaryGoals> midsup5) {
		this.midsup5 = midsup5;
	}

	public void setMidsup2(List<SupplementaryGoals> midsup2) {
		this.midsup2 = midsup2;
	}

	public List<SupplementaryGoals> getMidSupplementaryGoal() {
		return midSupplementaryGoal;
	}

	public void setMidSupplementaryGoal(List<SupplementaryGoals> midSupplementaryGoal) {
		this.midSupplementaryGoal = midSupplementaryGoal;
	}

	public List<BusinessGoals> getMidBusinessGoal() {
		return midBusinessGoal;
	}

	public void setMidBusinessGoal(List<BusinessGoals> midBusinessGoal) {
		this.midBusinessGoal = midBusinessGoal;
	}

	public void initMidBusinessGoal() {
		model = service.findOne(id);
		midBusinessGoal = new ArrayList<>();
		for (BusinessGoals business : businessGoalsRepos.findBySectionsUserAppraisal(model)) {
			midBusinessGoal.add(business);
		}

	}

	public void initMidSupplementaryGoal() {
		model = service.findOne(id);
		midSupplementaryGoal = new ArrayList<>();
		midsup1 = new ArrayList<>();
		midsup2 = new ArrayList<>();
		midsup3 = new ArrayList<>();
		midsup4 = new ArrayList<>();
		midsup5 = new ArrayList<>();

		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(1, model)) {
			midsup1.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(2, model)) {
			midsup2.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(3, model)) {
			midsup3.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(4, model)) {
			midsup4.add(supplementaryGoals);
		}
		for (SupplementaryGoals supplementaryGoals : userAppraisalService.findSuppBySection(5, model)) {
			midsup5.add(supplementaryGoals);
		}
	}

	public Boolean validateWeightMidBusinessGoals() {
		for (int i = 0; i < midBusinessGoal.size(); i++) {
			if (midBusinessGoal.get(i).getMidYearReview() < 0 || midBusinessGoal.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}
		return true;
	}

	public Boolean validateWeightMidSupplementaryGoals() {
		for (int i = 0; i < midsup1.size(); i++) {
			if (midsup1.get(i).getMidYearReview() < 0 || midsup1.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}

		for (int i = 0; i < midsup2.size(); i++) {
			if (midsup2.get(i).getMidYearReview() < 0 || midsup2.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}
		for (int i = 0; i < midsup3.size(); i++) {
			if (midsup3.get(i).getMidYearReview() < 0 || midsup3.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}
		for (int i = 0; i < midsup4.size(); i++) {
			if (midsup4.get(i).getMidYearReview() < 0 || midsup4.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}
		for (int i = 0; i < midsup5.size(); i++) {
			if (midsup5.get(i).getMidYearReview() < 0 || midsup5.get(i).getMidYearReview() > 100) {
				return FacesContextMessages.ErrorMessages("Rate of mid year should be between 0 and 100");
			}
		}
		return true;
	}

	public Boolean canSaveMidBusinessGoals() {
		return sessionView.getIsMyPm();
	}

	public void saveMidBusinessGoals() {

		for (int i = 0; i < midBusinessGoal.size(); i++) {

			BusinessGoals bg = businessGoalsService.findOne(midBusinessGoal.get(i).getId());
			bg.setMidYearReview(midBusinessGoal.get(i).getMidYearReview());
			businessGoalsService.save(bg);
		}
	}

	public void saveMidSupplementaryGoals() {

		int l = 0;
		if (midsup1.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : midsup1) {
				supplementaryGoals.setMidYearReview(midsup1.get(l).getMidYearReview());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (midsup2.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : midsup2) {
				supplementaryGoals.setMidYearReview(midsup2.get(l).getMidYearReview());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}

		l = 0;
		if (midsup3.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : midsup3) {
				supplementaryGoals.setMidYearReview(midsup3.get(l).getMidYearReview());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (midsup4.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : midsup4) {
				supplementaryGoals.setMidYearReview(midsup4.get(l).getMidYearReview());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
		l = 0;
		if (midsup5.size() > 0) {
			for (SupplementaryGoals supplementaryGoals : midsup5) {
				supplementaryGoals.setMidYearReview(midsup5.get(l).getMidYearReview());
				l++;
				supplementaryGoalsService.save(supplementaryGoals);
			}
		}
	}

	public void addCommentApproved() {

		comment.setTitle("Final comment Line manager");
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	public UserAppraisalComment getCommentByTitle() {
		model = service.findOne(id);
		return userAppraisalRepos.findCommentByTitle(model);
	}

	public Boolean existComment() {
		if (getCommentByTitle() == null) {
			return false;
		}
		return true;
	}

	private int stepMid = 1;

	public int getStepMid() {
		return stepMid;
	}

	public void setStepMid(int stepMid) {
		this.stepMid = stepMid;
	}

	@Transactional
	public String nextStepMid() throws IOException {
		switch (stepMid) {

		case 1:
			initMidSupplementaryGoal();
			if (!validateWeightMidBusinessGoals())
				return null;

			stepMid++;
			saveMidBusinessGoals();
			break;

		case 2:
			if (!validateWeightMidSupplementaryGoals())
				return null;
			saveMidSupplementaryGoals();

			stepMid++;
			break;
		case 3:
			if (!StringUtils.isBlank(userAppraisalComment.getContent())) {

				userAppraisalComment.setDate(new Date());
				userAppraisalComment.setTitle((model.getUserAppraisalStatus().getValue()
						.equals(UserAppraisalStatus.MYR_SELF_ASSESSMENT.getValue())) ? "User Appraisal MYR Creation"
								: "User Appraisal MYR Edition");
				userAppraisalComment.setParent(model);
				userAppraisalComment.setUser(sessionView.getUser());
				model.addComment(userAppraisalComment);
				model = service.saveAndRefresh(model);

			}
			MYRedited();

			stepMid++;
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("addEditUserAppraisal.xhtml?id=" + model.getId() + "&pageIndex=1");
			finalCalculBG();
			finalCalculSupp1();
			finalCalculSupp2();
			finalCalculSupp3();
			finalCalculSupp4();
			finalCalculSupp5();

			break;
		}
		return null;
	}

	public void previousStepMid() {
		if (stepMid != 1) {
			if (stepMid == 2) {

			}
			if (stepMid == 3) {

			}
			stepMid--;
		}
	}
	// New form BusinessGoal add ################################"

	private BusinessGoals bs = new BusinessGoals();

	public BusinessGoals getBs() {
		return bs;
	}

	public void setBs(BusinessGoals bs) {
		this.bs = bs;
	}

	public void initBankAccount() {
		bs = new BusinessGoals();
	}

	List<BusinessGoals> b1 = new ArrayList<>();

	public String saveBusinessGoals1() {

		if (getIntegerParameter("isEdit") == 0) {

			bs.setSections(findSectionId());
			if (!validateWeightBusinessGoals1(bs)) {
				return null;
			}

			if (goaltitlecount >= 1) {
				goalTitleList.remove(businessGoalsListEdit.get(businessGoalsListEdit.size() - 1).getGoalTitle());
			}

			int count = 0;
			for (BusinessGoals b : businessGoalsListEdit) {
				if (b.equals(bs))
					count++;
			}

			if (count == 0) {
				businessGoalsListEdit.add(bs);
			}
			execJavascript("PF('addBusinessGoalDlg').hide()");
			resetRemoveBusiness(bs);
			initBankAccount();
		}

		if (getIntegerParameter("isEdit") == 1) {
			bs.setSections(findSectionId());
			if (!validateWeightBusinessGoals1(bs)) {
				return null;
			}

			businessGoalsService.save(bs);
			if (goaltitlecount >= 1) {

				goalTitleList.remove(businessGoalsListEdit.get(businessGoalsListEdit.size() - 1).getGoalTitle());
			}
			int count = 0;
			for (BusinessGoals b : businessGoalsListEdit) {
				if (b.equals(bs))
					count++;
			}

			if (count == 0) {
				businessGoalsListEdit.add(bs);

			}
			resetRemoveBusiness(bs);
			execJavascript("PF('addBusinessGoalDlg').hide()");
			initBankAccount();

		}
		return null;
	}

	public void resetBusiness(BusinessGoals bg) {
		goalTitleList.add(bg.getGoalTitle());

	}

	public void resetRemoveBusiness(BusinessGoals bg) {
		if (bg != null) {
			goalTitleList.remove(bg.getGoalTitle());
		}
		initBankAccount();
	}

	public Boolean validateWeightBusinessGoals1(BusinessGoals b2) {
		if (getIntegerParameter("isEdit") == 0) {
			if (b2.getGoalWeight() <= 0 || b2.getGoalWeight() > 100) {
				return FacesContextMessages.ErrorMessages("Weight should be between 1 and 100");
			}

		}
		if (getIntegerParameter("isEdit") == 1) {
			if (b2.getGoalWeight() <= 0 || b2.getGoalWeight() > 100) {
				return FacesContextMessages.ErrorMessages("Weight should be between 1 and 100");
			}
		}
		return true;
	}

	public void finalCalculBG() {
		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (BusinessGoals bg : businessGoalsRepos.findBySectionsUserAppraisal(model)) {
				sommeMid += bg.getGoalWeight() * bg.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 0);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);

		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 0);

			sommeFinal = s.getSummaryRaiting();

			for (BusinessGoals bg : businessGoalsRepos.findBySectionsUserAppraisal(model)) {

				sommeFinal += bg.getGoalWeight() * bg.getSummaryRaiting();

			}
			somme1 = sommeFinal / 100;
			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 0);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}

	}

	public void finalCalculSupp1() {
		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 1)) {
				sommeMid += supp.getWeight() * supp.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 1);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);
		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 1);

			sommeFinal = s.getSummaryRaiting();

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 1)) {
				sommeFinal += supp.getWeight() * supp.getSummaryRaiting();
			}
			somme1 = sommeFinal / 100;

			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 1);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}
	}

	public void finalCalculSupp2() {
		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 2)) {
				sommeMid += supp.getWeight() * supp.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 2);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);
		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 2);

			sommeFinal = s.getSummaryRaiting();

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 2)) {
				sommeFinal += supp.getWeight() * supp.getSummaryRaiting();
			}
			somme1 = sommeFinal / 100;

			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 2);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}
	}

	public void finalCalculSupp3() {

		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 3)) {
				sommeMid += supp.getWeight() * supp.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 3);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);
		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 3);

			sommeFinal = s.getSummaryRaiting();

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 3)) {
				sommeFinal += supp.getWeight() * supp.getSummaryRaiting();
			}
			somme1 = sommeFinal / 100;

			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 3);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}
	}

	public void finalCalculSupp4() {
		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 4)) {
				sommeMid += supp.getWeight() * supp.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 4);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);
		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 4);

			sommeFinal = s.getSummaryRaiting();

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 4)) {
				sommeFinal += supp.getWeight() * supp.getSummaryRaiting();
			}
			somme1 = sommeFinal / 100;

			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 4);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}
	}

	public void finalCalculSupp5() {

		double somme;
		double sommeMid;
		double sommeFinal;
		double somme1;

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.MYR_EDITED)) {
			somme = 0;
			sommeMid = 0;
			somme1 = 0;

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 5)) {
				sommeMid += supp.getWeight() * supp.getMidYearReview();
			}
			somme = sommeMid / 100;
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 5);
			s.setMidYearReview(somme);
			s.setSummaryRaiting(somme1);
			sectionsService.save(s);
		}

		if (model.getUserAppraisalStatus().equals(UserAppraisalStatus.FYR_EDITED)) {
			Sections s = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 5);

			sommeFinal = s.getSummaryRaiting();

			for (SupplementaryGoals supp : supplementaryGoalsRepos.findByUserAppraisal(model, 5)) {
				sommeFinal += supp.getWeight() * supp.getSummaryRaiting();
			}
			somme1 = sommeFinal / 100;

			Sections ss = userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, 5);
			ss.setSummaryRaiting(somme1);
			sectionsService.save(ss);
		}
	}

	public Sections getSection(int s) {

		return userAppraisalRepos.findSectionByUserAppraisalAndNumber(model, s);
	}

	public void deleteUserAppraisal1() {
		int id = model.getAppraisal().getId();
		try {
			service.delete(model);
			evictCache();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("viewAppraisals.xhtml?id=" + id + "&pageIndex=9");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ToNotify getTnt() {
		return tnt;
	}

	public void setTnt(ToNotify tnt) {
		this.tnt = tnt;
	}

	public String keyworkerSave() {
		if(!validateMidKeyWorker())
			return null;
		model = service.findOne(id);
		ToNotify toNotify = service.findToNotify(sessionView.getUsername(), model);
		toNotify.setRateMid(tnt.getRateMid());

		List<ToNotify> toNotifyList = model.getToNotifyList();
		boolean found = false;

		for (int i = 0; i < toNotifyList.size(); i++) {
			ToNotify toNotify2 = toNotifyList.get(i);
			if (toNotify2.equals(toNotify)) {
				toNotifyList.set(i, toNotify);
				found = true;
				break;
			}
		}

		if (!found) {
			toNotifyList.add(toNotify);
		}
		model = service.saveAndRefresh(model);
		if (!StringUtils.isBlank(userAppraisalComment.getContent())) {

			userAppraisalComment.setDate(new Date());
			userAppraisalComment.setTitle("Mid KeyWorker Comment");
			userAppraisalComment.setParent(model);
			userAppraisalComment.setUser(sessionView.getUser());
			model.addComment(userAppraisalComment);
			model = service.saveAndRefresh(model);

		}
		ExternalContext externalContext1 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext1.redirect("addEditUserAppraisal.xhtml?id=" + model.getId() + "&pageIndex=1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String keyworkerSaveFinal() {
		if(!validateFinalKeyWorker())
			return null;
		model = service.findOne(id);
		ToNotify toNotify = service.findToNotify(sessionView.getUsername(), model);
		toNotify.setRateFinal(tnt.getRateFinal());

		List<ToNotify> toNotifyList = model.getToNotifyList();
		boolean found = false;

		for (int i = 0; i < toNotifyList.size(); i++) {
			ToNotify toNotify2 = toNotifyList.get(i);
			if (toNotify2.equals(toNotify)) {
				toNotifyList.set(i, toNotify);
				found = true;
				break;
			}
		}

		if (!found) {
			toNotifyList.add(toNotify);
		}
		model = service.saveAndRefresh(model);
		if (!StringUtils.isBlank(userAppraisalComment.getContent())) {

			userAppraisalComment.setDate(new Date());
			userAppraisalComment.setTitle("Final KeyWorker Comment");
			userAppraisalComment.setParent(model);
			userAppraisalComment.setUser(sessionView.getUser());
			model.addComment(userAppraisalComment);
			model = service.saveAndRefresh(model);

		}
		ExternalContext externalContext1 = FacesContext.getCurrentInstance().getExternalContext();
		try {
			externalContext1.redirect("addEditUserAppraisal.xhtml?id=" + model.getId() + "&pageIndex=1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Boolean validateMidKeyWorker() {
	
		int rating = tnt.getRateMid();
		if (rating < 0 || rating > 100) {
			return FacesContextMessages.ErrorMessages("The RateMid should be between 0 and 100");
		}
		return true;
	}
	
	public Boolean validateFinalKeyWorker() {
		
		int rating = tnt.getRateFinal();
		if (rating < 0 || rating > 100) {
			return FacesContextMessages.ErrorMessages("The Final Rate should be between 0 and 100");
		}
		return true;
	}
	
	
	public Boolean isInTonotifyList() {
		
		List<ToNotify> tnts =service.findToNotifyByUserAppraisalFinal(model);
		
		int is = 0;
		for (ToNotify toNotify : tnts) {
			if (sessionView.getUsername().equals(toNotify.getInternalResource().getUsername())) {
				is++;
			}
		}
		
		if (is==0) {
			return false;
		}

		return true;
	}
	
	
	/*
	 * public List<ToNotify> findToNotifyByUserAppraisalFinal(){
	 * 
	 * for (ToNotify notif : service.findToNotifyByUserAppraisalFinal(model)) {
	 * toNotifyList.add(notif); }
	 * 
	 * return service.findToNotifyByUserAppraisalFinal(model); }
	 */
	
	public List<ToNotify> findToNotifyByUserAppraisalFinal() {

		for (ToNotify notif : service.findToNotifyByUserAppraisalFinal(model)) {
			toNotifyList.add(notif);
		}

		return service.findToNotifyByUserAppraisalFinal(model);
	}

	public List<ToNotify> findToNotifyByUserAppraisal() {
		System.out.println("methode to update tonotifylist");
		for (ToNotify notif : service.findToNotifyByUserAppraisal(model)) {
			toNotifyList.add(notif);
		}
		
		return service.findToNotifyByUserAppraisal(model);
	}
	
	public Boolean isSavedKeyworkerFinal() {
		ToNotify toNotify = service.findToNotify(sessionView.getUsername(), model);	
		if (toNotify.getRateFinal()!=null) {
			return false;
		}
		return true;
	}
	
	public Boolean isSavedKeyworkerMid() {
		ToNotify toNotify = service.findToNotify(sessionView.getUsername(), model);	
		if (toNotify.getRateMid()!=null) {
			return false;
		}
		return true;
	}
	
	
	//BG img url
	
		public String bgImgUrl(String goalTitle) {
			if(goalTitle.equals("Financial Excellence"))
			return "resources/img/FE.png";
			if(goalTitle.equals("Technical Excellence"))
				return "resources/img/TE.png";
			if(goalTitle.equals("Business Development"))
				return "resources/img/BD.png";
			if(goalTitle.equals("Governance "))
				return "resources/img/G.png";
			if(goalTitle.equals("Customer satisfaction"))
				return "resources/img/CS.png";
			
			else return "resources/img/logo.png"; 
		}
		
		
}