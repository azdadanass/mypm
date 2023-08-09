package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.RequestForQuote;

@Repository
public interface RequestForQuoteRepos extends JpaRepository<RequestForQuote, Integer> {

	@Query("from RequestForQuote where (customer is null or customer.id = ?1)")
	List<RequestForQuote> findByCustomerOrCustomerNull(Integer customerId);

	@Modifying
	@Query("update RequestForQuote set existing = ?2,customer.id = ?3 where id = ?1")
	void updateExistingAndCustomer(Integer id, Boolean existing, Integer customerId);

}
