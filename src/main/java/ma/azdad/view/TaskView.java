package ma.azdad.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.UserAppraisalStatus;
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
	private Long totalToSubmitMid;
	private Long totalToApproveMid;
	private Long totalToSubmitFinal;
	private Long totalToApproveFinal;

	
	
	private Long total = 0l;

	@PostConstruct
	public void init() {
		totalToSubmit = userAppraisalService.countToSubmitted(sessionView.getUser().getUsername(),UserAppraisalStatus.EDITED);
		totalToApprove = userAppraisalService.countToApproved(sessionView.getUser());
		totalToSubmitMid = userAppraisalService.countToSubmitted(sessionView.getUser().getUsername(),UserAppraisalStatus.MYR_EDITED);
		totalToSubmitFinal = userAppraisalService.countToSubmitted(sessionView.getUser().getUsername(),UserAppraisalStatus.FYR_EDITED);
		totalToApproveMid = userAppraisalService.countToApprovedMid(sessionView.getUser());
		totalToApproveFinal = userAppraisalService.countToApprovedFinal(sessionView.getUser());


		total = totalToSubmit+totalToApprove+totalToSubmitMid+totalToSubmitFinal+totalToApproveMid+totalToApproveFinal;
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

	public Long getTotalToSubmitMid() {
		return totalToSubmitMid;
	}

	public void setTotalToSubmitMid(Long totalToSubmitMid) {
		this.totalToSubmitMid = totalToSubmitMid;
	}

	public Long getTotalToApproveMid() {
		return totalToApproveMid;
	}

	public void setTotalToApproveMid(Long totalToApproveMid) {
		this.totalToApproveMid = totalToApproveMid;
	}

	public Long getTotalToSubmitFinal() {
		return totalToSubmitFinal;
	}

	public void setTotalToSubmitFinal(Long totalToSubmitFinal) {
		this.totalToSubmitFinal = totalToSubmitFinal;
	}

	public Long getTotalToApproveFinal() {
		return totalToApproveFinal;
	}

	public void setTotalToApproveFinal(Long totalToApproveFinal) {
		this.totalToApproveFinal = totalToApproveFinal;
	}

}