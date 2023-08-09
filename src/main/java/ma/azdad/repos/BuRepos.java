package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Bu;

@Repository
public interface BuRepos extends JpaRepository<Bu, Integer> {

	@Query("select new Bu(id,name,operatingSector,countFiles,company.name,director.fullName,operationsDirector.fullName,financeManager.fullName,technicalDirector.fullName,hrDirector.fullName) from Bu")
	List<Bu> findLight();

	@Query("select new Bu(id,name,operatingSector,countFiles,company.name,director.fullName,operationsDirector.fullName,financeManager.fullName,technicalDirector.fullName,hrDirector.fullName) from Bu where company.id = ?1")
	List<Bu> findLightByCompany(Integer companyId);

	@Query("select count(*) from Bu where name = ?1 and company.id = ?2 and (?3 is null or id != ?3)")
	Long countByNameAndCompany(String name, Integer companyId, Integer id);

}
