package ma.azdad.view;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ma.azdad.model.Profile;
import ma.azdad.model.User;
import ma.azdad.service.CompanyProfileService;
import ma.azdad.service.DelegationService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;

@ManagedBean
@Component
@Scope("session")

public class SessionView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected UserService userService;

	@Autowired
	protected CompanyProfileService companyProfileService;

	@Autowired
	protected DelegationService delegationService;

	@Autowired
	protected ProjectService projectService;

	private String username;
	private User user;

	private List<Integer> companyIdList = new ArrayList<>();
	private List<Integer> companyAssistantIdList = new ArrayList<>(Arrays.asList(-1));
	private List<Integer> companyBuyerIdList = new ArrayList<>(Arrays.asList(-1));
	private List<Integer> companyAccountantIdList = new ArrayList<>(Arrays.asList(-1));
	private List<Integer> companyCfoIdList = new ArrayList<>(Arrays.asList(-1));
	private List<Integer> companyCashControllerIdList = new ArrayList<>(Arrays.asList(-1));
	private List<Integer> delegatedProjectIdList = new ArrayList<>(Arrays.asList(-1));
	private Integer menu = 1;
	private Integer supplierId;

	@PostConstruct
	public void init() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		username = auth.getName().toLowerCase().trim();
		username = ((User) auth.getPrincipal()).getUsername();
		user = userService.findOne(username);
		log.info("init SessionView !");
		log.info(username + " is connected");
		companyAssistantIdList.addAll(companyProfileService.findCompanyIdList(username, Profile.ASSISTANT));
		companyBuyerIdList.addAll(companyProfileService.findCompanyIdList(username, Profile.BUYER));
		companyAccountantIdList.addAll(companyProfileService.findCompanyIdList(username, Profile.ACCOUNTANT));
		companyCfoIdList.addAll(companyProfileService.findCompanyIdList(username, Profile.CFO));
		companyCashControllerIdList.addAll(companyProfileService.findCompanyIdList(username, Profile.CASH_CONTROLLER));
		companyIdList.addAll(companyAssistantIdList);
		companyIdList.addAll(companyBuyerIdList);
		companyIdList.addAll(companyAccountantIdList);
		companyIdList.addAll(companyCfoIdList);
		companyIdList.addAll(companyCashControllerIdList);
		delegatedProjectIdList.addAll(delegationService.findDelegatedProjectIdList(username));
		
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("username"); 
			String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
			System.out.println("VALUEEEEEE : "+value);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public Boolean isCompanyBuyer(Integer companyId) {
		return companyBuyerIdList.contains(companyId);
	}

	public Boolean isCompanyAssistant(Integer companyId) {
		return companyAssistantIdList.contains(companyId);
	}

	public Boolean isCompanyAccountant(Integer companyId) {
		return companyAccountantIdList.contains(companyId);
	}

	public Boolean isCompanyCfo(Integer companyId) {
		return companyCfoIdList.contains(companyId);
	}

	public Boolean isCompanyCashController(Integer companyId) {
		return companyCashControllerIdList.contains(companyId);
	}

	public Boolean isTheConnectedUser(String username) {
		return this.username.equalsIgnoreCase(username);
	}

	public Boolean isTheConnectedUser(String... usernameTab) {
		for (int i = 0; i < usernameTab.length; i++)
			if (this.username.equalsIgnoreCase(usernameTab[i]))
				return true;
		return false;
	}

	public Boolean isTheConnectedUser(User user) {
		return isTheConnectedUser(user.getUsername());
	}

	public Boolean getIsUser() {
		return user.getIsUser();
	}

	public Boolean getIsAdmin() {
		return user.getIsAdmin();
	}
	
	public Boolean getIsMyPm() {
		return user.getIsMyPm();
	}
	
	public Boolean getIsMyPmHr() {
		return user.getIsMyPmHr();
	}
	public Boolean getIsMyPmLineManager() {
		return user.getIsMyPmLineManager();
	}
	
	
	/*
	 * public Boolean getIsHr() { return user.getIsHr(); }
	 * 
	 * public Boolean getIsLineManager() { return user.getIsLineManager(); }
	 */
	public Boolean getIsInternalAdmin() {
		return user.getIsMyPm();
	}

	public Boolean getIsExternalAdmin() {
		return user.getIsAdmin() && !user.getInternal();
	}

	public Boolean getInternal() {
		return user.getInternal();
	}

	public Boolean isAssistant() {
		return companyAssistantIdList.size() > 1;
	}

	public Boolean getIsAssistant() {
		return isAssistant();
	}

	public Boolean isAccountant() {
		return companyAccountantIdList.size() > 1;
	}

	public Boolean getIsAccountant() {
		return isAccountant();
	}

	public Boolean isBuyer() {
		return companyBuyerIdList.size() > 1;
	}

	public Boolean getIsBuyer() {
		return isBuyer();
	}

	public Boolean isCfo() {
		return companyCfoIdList.size() > 1;
	}

	public Boolean getIsCfo() {
		return isCfo();
	}

	public Boolean isCashController() {
		return companyCashControllerIdList.size() > 1;
	}

	public Boolean getIsCashController() {
		return isCashController();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Integer> getCompanyIdList() {
		return companyIdList;
	}

	public void setCompanyIdList(List<Integer> companyIdList) {
		this.companyIdList = companyIdList;
	}

	public List<Integer> getCompanyAssistantIdList() {
		return companyAssistantIdList;
	}

	public List<Integer> getCompanyBuyerIdList() {
		return companyBuyerIdList;
	}

	public List<Integer> getCompanyAccountantIdList() {
		return companyAccountantIdList;
	}

	public List<Integer> getCompanyCfoIdList() {
		return companyCfoIdList;
	}

	public List<Integer> getCompanyCashControllerIdList() {
		return companyCashControllerIdList;
	}

	public List<Integer> getDelegatedProjectIdList() {
		return delegatedProjectIdList;
	}

	public void setDelegatedProjectIdList(List<Integer> delegatedProjectIdList) {
		this.delegatedProjectIdList = delegatedProjectIdList;
	}

	public void setCompanyAssistantIdList(List<Integer> companyAssistantIdList) {
		this.companyAssistantIdList = companyAssistantIdList;
	}

	public void setCompanyBuyerIdList(List<Integer> companyBuyerIdList) {
		this.companyBuyerIdList = companyBuyerIdList;
	}

	public void setCompanyAccountantIdList(List<Integer> companyAccountantIdList) {
		this.companyAccountantIdList = companyAccountantIdList;
	}

	public void setCompanyCfoIdList(List<Integer> companyCfoIdList) {
		this.companyCfoIdList = companyCfoIdList;
	}

	public void setCompanyCashControllerIdList(List<Integer> companyCashControllerIdList) {
		this.companyCashControllerIdList = companyCashControllerIdList;
	}

	public Integer getMenu() {
		return menu;
	}

	public void setMenu(Integer menu) {
		this.menu = menu;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

}