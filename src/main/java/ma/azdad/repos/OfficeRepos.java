package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Office;

@Repository
public interface OfficeRepos extends JpaRepository<Office, Integer> {

	String select1 = "select new Office(id,name,address1,address2,address3,phone,fax) ";

	@Query(select1 + " from Office")
	public List<Office> find();

}
