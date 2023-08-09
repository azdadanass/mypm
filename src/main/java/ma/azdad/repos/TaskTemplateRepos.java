package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.TaskTemplate;

@Repository
public interface TaskTemplateRepos extends JpaRepository<TaskTemplate, Integer> {

}
