package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CustomerCategory;

@Repository
public interface CustomerCategoryRepos extends JpaRepository<CustomerCategory, Integer> {

	String constructor1 = "select new CustomerCategory(id,name,photo)";

	@Query(constructor1 + "from CustomerCategory")
	public List<CustomerCategory> find();
}
