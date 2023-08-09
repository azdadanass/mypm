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

@ManagedBean
@Component
@Scope("view")
public class TaskView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProjectService projectService;

	@Autowired
	private RestrictionService restrictionService;

	private Long totalProject;
	private Long totalActiveRestriction;
	private Long total = 0l;

	@PostConstruct
	public void init() {
		totalProject = projectService.countWithoutTasks();
		totalActiveRestriction = restrictionService.countActive();
		total = totalProject + totalActiveRestriction;
	}

	public Long getTotalProject() {
		return totalProject;
	}

	public Long getTotal() {
		return total;
	}

	public Long getTotalActiveRestriction() {
		return totalActiveRestriction;
	}

}