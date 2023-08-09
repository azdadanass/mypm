package ma.azdad.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;
import ma.azdad.model.User;
import ma.azdad.service.AffectationService;
import ma.azdad.service.BankAccountService;
import ma.azdad.service.CompanyProfileService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.RestTemplateService;
import ma.azdad.service.UserService;
import ma.azdad.utils.App;
import ma.azdad.utils.DataTypes;

@ManagedBean
@Component
@Scope("session")
public class CacheView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AffectationService affectationService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyProfileService companyProfileService;

	@Autowired
	private RestTemplateService restTemplateService;

	@Autowired
	private BankAccountService bankAccountService;

	private List<CompanyProfile> companyProfileList;

	private Map<String, String> lm = new HashMap<String, String>();
	private Map<Integer, String> bankAccountFullNames = new HashMap<Integer, String>();

	@PostConstruct
	public void init() {
		lm = affectationService.getDatas(DataTypes.LM.getValue());
		bankAccountFullNames = bankAccountService.getFullNameMap();
		companyProfileList = companyProfileService.findByErp();
	}

	public String getFirstUsernameByCompanyAndProfile(Integer companyId, Profile profile) {
		try {
			return companyProfileList.stream().filter(i -> companyId.equals(i.getCompany().getId()) && profile.equals(i.getProfile())).findFirst().get().getUser().getUsername();
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			log.error("companyId : " + companyId + "\tprofile : " + profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void accessDenied() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "ad.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Async
	public void updateProjectReport(Integer projectId) {
		String response = restTemplateService.consumRest(App.MYREPORTS.getLink() + "/rest/service/update/pr/" + projectId, String.class);
		log.info("update project report response : " + response);
		response = restTemplateService.consumRest(App.MYREPORTS.getLink() + "/rest/service/update/pmr/" + projectId, String.class);
		log.info("update project monthly report response : " + response);
	}

	public Double getAvailableBudget(Integer projectId) {
		return restTemplateService.consumRest(App.MYREPORTS.getLink() + "/rest/service/get/ab/" + projectId, Double.class);
	}

	public String getProjectManager(Integer projectId) {
		return projectService.getManagerFullNameMap().get(projectId).getManagerFullName();
	}

	public Boolean isTheLineManagerOf(String resourceUsername) {
		return sessionView.isTheConnectedUser(getLineManager(resourceUsername));
	}

	public String getPhoto(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getPhoto();
	}

	public String getFullName(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getFullName();
	}

	public String getFullname(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getFullName();
	}

	public String getJobTitle(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getJob();
	}

	public String getMobileNumber(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getPhone();
	}

	public String getEmail(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getEmail();
	}

	public String getLineManager(String username) {
		return lm.get(username);
	}

	public String getBankAccountFullName(Integer id) {
		return bankAccountFullNames.get(id);
	}

	public Boolean getActive(String username) {
		return userService.findAsMap().getOrDefault(username, new User()).getActive();
	}

	public static String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

}
