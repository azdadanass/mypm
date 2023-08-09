package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DelegationDetail;

@Repository
public interface DelegationDetailRepos extends JpaRepository<DelegationDetail, Integer> {

	String constructor1 = "select new DelegationDetail(id,type,a.delegation.startDate,a.delegation.endDate, a.delegation.delegate.fullName, a.delegation.delegate.photo,a.delegation.delegator.fullName,(select b.fullName from User b where b.username = a.user.username),(select b.name from Project b where b.id = a.project.id))";

	@Query(constructor1 + "from DelegationDetail a where a.delegation.delegate.username = ?1 and current_date between a.delegation.startDate and a.delegation.endDate")
	List<DelegationDetail> findByDelegateAndActive(String username);

	@Query(constructor1 + "from DelegationDetail a where a.delegation.delegate.username = ?1 and current_date not between a.delegation.startDate and a.delegation.endDate")
	List<DelegationDetail> findByDelegateAndInactive(String username);

	@Query(constructor1 + "from DelegationDetail a where a.delegation.delegator.username = ?1 and current_date between a.delegation.startDate and a.delegation.endDate")
	List<DelegationDetail> findByDelegatorAndActive(String username);

	@Query(constructor1 + "from DelegationDetail a where a.delegation.delegator.username = ?1 and current_date not between a.delegation.startDate and a.delegation.endDate")
	List<DelegationDetail> findByDelegatorAndInactive(String username);

}
