package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SectionsData;

@Repository
public interface SectionsDataRepos extends JpaRepository<SectionsData, Integer>{

}
