package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.SupplierCategory;

@Repository
public interface SupplierCategoryRepos extends JpaRepository<SupplierCategory, Integer> {

	String constructor1 = "select new SupplierCategory(id,name,photo)";

	@Query(constructor1 + "from SupplierCategory")
	public List<SupplierCategory> find();
}
