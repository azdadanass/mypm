package ma.azdad.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.service.ProjectService;
import ma.azdad.service.RestrictionService;
import ma.azdad.service.UserAppraisalService;

@ManagedBean
@Component
@Scope("view")
public class TaskView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserAppraisalService userAppraisalService;
	@Autowired
	private SessionView sessionView;
	

	private Long totalToSubmit;
	private Long totalToApprove;


	
	private Long total = 0l;

	@PostConstruct
	public void init() {
		totalToSubmit = userAppraisalService.countToSubmitted(sessionView.getUser().getUsername());
		totalToApprove = userAppraisalService.countToApproved(sessionView.getUser());

		total = totalToSubmit+totalToApprove ;
	}

	public Long getTotalToApprove() {
		return totalToApprove;
	}

	public Long getTotalToSubmit() {
		return totalToSubmit;
	}

	public Long getTotal() {
		return total;
	}

	
}