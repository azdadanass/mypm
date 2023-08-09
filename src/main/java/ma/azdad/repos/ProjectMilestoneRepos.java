package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ProjectMilestone;

@Repository
public interface ProjectMilestoneRepos extends JpaRepository<ProjectMilestone, Integer> {

	@Query("from ProjectMilestone a where a.project.id = ?1")
	List<ProjectMilestone> findByProject(Integer projectId);

}
