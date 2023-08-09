package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Contract;

@Repository
public interface ContractRepos extends JpaRepository<Contract, Integer> {

	@Query("select new Contract(id,reference) from Contract where customer.id = ?1")
	List<Contract> findByCustomerId(Integer customerId);

}
