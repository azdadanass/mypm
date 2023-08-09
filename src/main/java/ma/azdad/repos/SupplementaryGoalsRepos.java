package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SupplementaryGoals;

@Repository
public interface SupplementaryGoalsRepos extends JpaRepository<SupplementaryGoals, Integer>{

}
