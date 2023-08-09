package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Sections;

@Repository
public interface SectionsRepos extends JpaRepository<Sections, Integer>{
	
	@Query("SELECT DISTINCT s.sectionsTitle FROM Sections s")
    List<String> findDistinctSectionTitles();

}
