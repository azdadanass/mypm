package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Restriction;

@Repository
public interface RestrictionRepos extends JpaRepository<Restriction, Integer> {

	String c1 = "select new Restriction(a.id,a.type,a.startDate,a.endDate,a.resolutionDate,a.reason,a.manual,a.app,a.iadmin,a.ibuy,a.iexpense,a.invoice,a.myhr,a.myreports,a.mytools,a.wtr,a.countFiles,a.user.fullName,a.user.photo,a.user.internal,a.user.companyType,(select b.name from Company b where b.id = a.user.company.id),(select b.name from Customer b where b.id = a.user.customer.id),(select b.name from Supplier b where b.id = a.user.supplier.id),(select b.fullName from User b where b.username = a.requester.username)) ";

	@Query(c1 + "from Restriction a order by id desc")
	List<Restriction> findLight();

	@Query(c1 + "from Restriction a where current_date between a.startDate and (case when a.endDate is not null then a.endDate else current_date end) order by a.id desc")
	List<Restriction> findLightActive();

	@Query("select count(*) from Restriction a where current_date between startDate and (case when endDate is not null then endDate else current_date end) ")
	Long countActive();

	@Query(c1 + "from Restriction a where current_date not between a.startDate and (case when a.endDate is not null then a.endDate else current_date end) order by a.id desc")
	List<Restriction> findLightInactive();

	@Query(c1 + "from Restriction a where a.user.username = ?1 order by a.id desc")
	List<Restriction> findLightByUser(String userUsername);

	@Query(c1 + "from Restriction a where a.user.username = ?1 and current_date between a.startDate and (case when a.endDate is not null then a.endDate else current_date end) order by a.id desc")
	List<Restriction> findLightActiveByUser(String userUsername);

	@Query(c1 + "from Restriction a where a.user.username = ?1 and not(current_date between a.startDate and (case when a.endDate is not null then a.endDate else current_date end)) order by a.id desc")
	List<Restriction> findLightInactiveByUser(String userUsername);

}
