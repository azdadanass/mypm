package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.TaskTemplate;
import ma.azdad.repos.TaskTemplateRepos;

@Component
public class TaskTemplateService extends GenericService<Integer,TaskTemplate, TaskTemplateRepos> {

	@Override
	@Cacheable("taskTemplateService.findAll")
	public List<TaskTemplate> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("taskTemplateService.findOne")
	public TaskTemplate findOne(Integer id) {
		TaskTemplate taskTemplate = super.findOne(id);
		initialize(taskTemplate.getDetailList());

		return taskTemplate;
	}

}
