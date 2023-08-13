package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.Sections;
import ma.azdad.model.SectionsData;
import ma.azdad.model.UserAppraisal;

@Repository
public interface SectionsRepos extends JpaRepository<Sections, Integer>{
	
	@Query("SELECT DISTINCT s.sectionsTitle FROM Sections s")
    List<String> findDistinctSectionTitles();
	
	@Query("from Sections  s where s.userappraisal=?1 ")
	List<Sections> findByUserAppraisal(UserAppraisal uap);

}
