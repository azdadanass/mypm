package ma.azdad.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import ma.azdad.service.BankAccountService;
import ma.azdad.service.CacheService;
import ma.azdad.service.DelegationService;
import ma.azdad.service.ProjectService;

@ManagedBean
@Component
@Scope("view")
@Secured("ROLE_IT_MANAGER")
public class ScriptView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CacheService cacheService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private ProjectService projectService;

	
	@Autowired
	private DelegationService delegationService;
	
	public void evictCavhe() {
		System.out.println("evictCavhe ! ");
		cacheService.evictAllCaches();
	}
	
	public void splitDelegationPerTypeScript() {
		delegationService.splitDelegationPerTypeScript();
	}

	public void generateBankAccountFullNameScript() {
		bankAccountService.generateFullNameScript();
	}

	public void initProjectManagerScript() {
		projectService.initProjectManagerScript();
	}
}