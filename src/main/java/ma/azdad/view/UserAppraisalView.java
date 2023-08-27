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

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.AppraisalsStatus;
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
import ma.azdad.repos.SupplementaryGoalsRepos;
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

	@Autowired
	SupplementaryGoalsRepos supplementaryGoalsRepos;

	private List<String> titleList;
	private List<Boolean> eligibleList;
	private List<Integer> weightList;

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

	public void onDebut() {
		isMid = 1;

		System.out.println(isMid);

	}

	public void onMid() {
		isMid = 2;
		System.out.println(isMid);

	}

	public void onFinal() {
		isMid = 3;
		System.out.println(isMid);

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
		// chart*****************************
		businessGoalsListEdit = new ArrayList<>();
		editBusinessGoals();
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
				System.out.println(sectionEditList.get(i).getWeight());
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
		System.out.println(model);
		return sectionsService.findSectionsByUserAppraisal(model);
	}

	public List<Sections> findSectionByUserAppraisal(int idedit) {
		// System.out.println(model);
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
						System.out.println(editsuppl1.get(k));
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

					/*
					 * System.out.println("last item in businessGoalList edited : " +
					 * businessGoalsList.get(businessGoalsList.size() - 1).getGoalTitle());
					 */

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

			businessGoalsList.remove(bg);
			goaltitlecount--;
		}

	}

	public Sections findSectionId() {
		return businessGoalsRepos.findSectionId(id);
	}

	public Boolean validateWeightBusinessGoals() {
		if (getIntegerParameter("isEdit") == 0) {
			double weightTotal = 0;
			for (int i = 0; i < businessGoalsList.size(); i++) {
				weightTotal = weightTotal + businessGoalsList.get(i).getGoalWeight();
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
				System.out.println(suppl4);
				weightTotal4 = weightTotal4 + suppl4.get(i).getWeight();

			}
			for (int i = 0; i < suppl5.size(); i++) {
				System.out.println(i);
				System.out.println(weightTotal5);
				System.out.println(suppl5);
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

			for (int i = 0; i < editsuppl1.size(); i++) {
				weightTotal1 = weightTotal1 + editsuppl1.get(i).getWeight();
			}
			for (int i = 0; i < editsuppl2.size(); i++) {
				weightTotal2 = weightTotal2 + editsuppl2.get(i).getWeight();
			}
			for (int i = 0; i < editsuppl3.size(); i++) {
				weightTotal3 = weightTotal3 + editsuppl3.get(i).getWeight();
			}
			for (int i = 0; i < editsuppl4.size(); i++) {
				System.out.println(editsuppl4);
				weightTotal4 = weightTotal4 + editsuppl4.get(i).getWeight();

			}
			for (int i = 0; i < editsuppl5.size(); i++) {
				System.out.println(i);
				System.out.println(weightTotal5);
				System.out.println(editsuppl5);
				weightTotal5 = weightTotal5 + editsuppl5.get(i).getWeight();
			}

			if (editsuppl1.size() > 0 && weightTotal1 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (editsuppl2.size() > 0 && weightTotal2 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (editsuppl3.size() > 0 && weightTotal3 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (editsuppl4.size() > 0 && weightTotal4 != 100) {
				return FacesContextMessages.ErrorMessages("Total of Weight should be equal 100");
			}
			if (editsuppl5.size() > 0 && weightTotal5 != 100) {
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
						businessGoalsList.get(i).getSections()

				);
				businessGoalsService.save(businessGoals);
			}
		}
		if (getIntegerParameter("isEdit") == 1) {
			for (int i = 0; i < businessGoalsListEdit.size(); i++) {

				BusinessGoals businessGoals = businessGoalsListEdit.get(i);
				
				System.out.println("edited mode bg ="+businessGoals);
				if (businessGoals.getId()==null) {
					System.out.println("id not exist");
					BusinessGoals bg = new BusinessGoals(businessGoals.getGoalDetails(),
							businessGoals.getGoalTitle(), businessGoals.getGoalWeight(),
							businessGoals.getMidYearReview(),
							businessGoals.getSummaryRaiting(), businessGoals.getSections()

					);
					businessGoalsService.save(bg);
				}else {
					System.out.println("id exist in list");
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
				&& sessionView.getIsMyPmLineManager();
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
				&& sessionView.getIsMyPmLineManager();
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
		System.out.println(sessionView.getIsMyPmHr());
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmHr();
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
				&& sessionView.getIsMyPmHr();
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

	// ################################# MID YEAr
	// ################################################

	public Boolean canMidSelfAssessment() {
		return UserAppraisalStatus.APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW);
	}

	public void midSelfAssessment() {
		if (!canMidSelfAssessment())
			return;

		model.setDateStatsSelfAssessmentMidYear(new Date());
		model.setUserStatsSelfAssessmentMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Edited " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canSubmitedMidYear() {
		return UserAppraisalStatus.MYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus())
				&& sessionView.getIsMyPm();
	}

	public void submitedMidYear() {
		if (!canSubmitedMidYear())
			return;

		model.setDateStatsSubmitedMidYear(new Date());
		model.setUserStatsSubmitedMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_MID_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Submitted " + model.getEmploy().getFullName() + " Appraisal"));
		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApprovedLMMidYear() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW);
	}

	public void approvedLMMidYear() {
		if (!canApprovedLMMidYear())
			return;

		model.setDateStatsApprovedLMMidYear(new Date());
		model.setUserStatsApprovedLMMidYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejectedLMMid() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmHr()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.MID_YEAR_REVIEW);
	}

	public void rejectedLMMid() {
		if (!canRejected())
			return;

		model.setDateStatsSubmitedMidYear(null);
		model.setDateStatsApprovedLMMidYear(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.MYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.MYR_REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	// ################################# Final YEAr
	// ###############################################

	public Boolean canFinalSelfAssessment() {
		return UserAppraisalStatus.MYR_APPROVED_LM.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void finalSelfAssessment() {
		if (!canFinalSelfAssessment())
			return;

		model.setDateStatsSelfAssessmentFinalYear(new Date());
		model.setUserStatsSelfAssessmentFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Edited " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canSubmitedFinalYear() {
		return UserAppraisalStatus.FYR_SELF_ASSESSMENT.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void submitedFinalYear() {
		if (!canSubmitedFinalYear())
			return;

		model.setDateStatsSubmitedFinalYear(new Date());
		model.setUserStatsSubmitedFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.SUBMITED_FINAL_YEAR);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Submitted " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canApprovedLMFinalYear() {
		return UserAppraisalStatus.SUBMITED_FINAL_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void approvedLMFinalYear() {
		if (!canApprovedLMFinalYear())
			return;

		model.setDateStatsApprovedLMFinalYear(new Date());
		model.setUserStatsApprovedLMFinalYear(sessionView.getUser());
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_APPROVED_LM);
		model.addHistory(new UserAppraisalHistory(model.getUserAppraisalStatus().getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Approved " + model.getEmploy().getFullName() + " Appraisal"));

		service.save(model);
		model = service.findOne(model.getId());
		evictCache();
	}

	public Boolean canRejectedLMFinal() {
		return UserAppraisalStatus.SUBMITED_MID_YEAR.equals(model.getUserAppraisalStatus()) && sessionView.getIsMyPm()
				&& sessionView.getIsMyPmHr()
				&& model.getAppraisal().getAppraisalsStatus().equals(AppraisalsStatus.FINAL_REVIEW);
	}

	public void rejectedLMFinal() {
		if (!canRejected())
			return;

		model.setDateStatsSubmitedFinalYear(null);
		model.setDateStatsApprovedLMFinalYear(null);
		model.setUserAppraisalStatus(UserAppraisalStatus.FYR_SELF_ASSESSMENT);
		model.addHistory(new UserAppraisalHistory(UserAppraisalStatus.FYR_REJECTED.getValue(), sessionView.getUser(),
				sessionView.getUser() + " has Rejected " + model.getEmploy().getFullName() + " Appraisal"));

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
//		 ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		 externalContext.redirect("addEditUserAppraisal.xhtml?id="+model.getId()+"&pageIndex=1");
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
			// supplementaryGoalsListBg.add(new SupplementaryGoals(se));
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

	public void initSuppGoals() {

		supplementaryGoalsListBg = new ArrayList<>();
		if(getIntegerParameter("isEdit")==1) {
			
			for (Sections section : sectionEditList) {
				if (section.getEligible()==false) {
					for (SupplementaryGoals supp : userAppraisalService.findSupGoalsBySection(section.getSectionsNumber(), model)) {
						
						try {
							supplementaryGoalsService.delete(supp);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
			
			
			
			
			
			/*
			 * for(SupplementaryGoals sup:supplementaryGoalsListEdit) {
			 * if(!isElig(sup.getSectionsData().getGoaldId())) {
			 * System.out.println("before remove from list "+ sup);
			 * supplementaryGoalsListEdit.remove(sup); try {
			 * System.out.println(" afted list remove need to delete : "+ sup);
			 * supplementaryGoalsService.delete(sup); } catch
			 * (DataIntegrityViolationException e) { e.printStackTrace(); } catch (Exception
			 * e) { e.printStackTrace(); } }
			 * 
			 * }
			 */

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
			System.out.println("size spl1 " + spl1.size());

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
			System.out.println("size spl2" + spl2.size());
			
			if(isElig(2)) {
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
			System.out.println("size spl3" + spl3.size());

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
			System.out.println("size spl4" + spl4.size());

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
			System.out.println("size spl5" + spl5.size());

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
			saveBusinessGoals();
			//initSuppGoals();

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
			edited();

			// step++;
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

			step--;
		}
	}

}