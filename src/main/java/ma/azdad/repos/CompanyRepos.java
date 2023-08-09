package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Company;

@Repository
public interface CompanyRepos extends JpaRepository<Company, Integer> {

	@Query("select new Company(id,name) from Company")
	List<Company> findLight();

}
