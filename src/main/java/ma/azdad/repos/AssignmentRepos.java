package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Assignment;

@Repository
public interface AssignmentRepos extends JpaRepository<Assignment, Integer> {

}
