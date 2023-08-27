package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Contract;
import ma.azdad.model.EGeneralData;

@Repository
public interface ContractRepos extends JpaRepository<Contract, Integer> {

	/*
	 * @Query("select new Contract(id,reference) from Contract where customer.id = ?1"
	 * ) List<Contract> findByCustomerId(Integer customerId);
	 */
		
	
	
	@Query("select c.resource from Contract c  where c.hireDate <=  ?1  and c.contractStatus = 'active'") 
	List<EGeneralData> findByContractStatus(Date d);
}
