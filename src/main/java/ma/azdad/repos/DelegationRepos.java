package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Delegation;
import ma.azdad.model.DelegationDetailType;

@Repository
public interface DelegationRepos extends JpaRepository<Delegation, Integer> {

	String constructor1 = "select new Delegation(id,startDate,endDate,description,status,delegate.fullName,delegate.photo,delegator.fullName) ";

	@Query("select distinct a.project.id from DelegationDetail a where a.delegation.delegate.username = ?1 and a.delegation.status = 'Active' and a.type = ?2")
	public List<Integer> findDelegatedProjectIdList(String username, DelegationDetailType type);

	@Query(constructor1 + "from Delegation where delegate.username = ?1 and current_date between startDate and endDate order by endDate desc")
	List<Delegation> findByDelegateAndActive(String username);

	@Query(constructor1 + "from Delegation where delegate.username = ?1 and current_date not between startDate and endDate order by endDate desc")
	List<Delegation> findByDelegateAndInactive(String username);

	@Query(constructor1 + "from Delegation where delegator.username = ?1 and current_date between startDate and endDate order by endDate desc")
	List<Delegation> findByDelegatorAndActive(String username);

	@Query(constructor1 + "from Delegation where delegator.username = ?1 and current_date not between startDate and endDate order by endDate desc")
	List<Delegation> findByDelegatorAndInactive(String username);

	@Modifying
	@Query("update Delegation set status = (case when current_date < startDate then 'Pending' else (case when current_date > endDate then 'Expired' else 'Active' end) end)")
	void updateStatus();

}
