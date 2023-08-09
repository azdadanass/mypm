package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;

@Repository
public interface CompanyProfileRepos extends JpaRepository<CompanyProfile, Integer> {

	String c1 = "select new CompanyProfile(id,profile,erp,a.user.fullName,a.user.photo,a.user.active,a.company.name) ";

	@Query("select company.id from CompanyProfile where erp = ?1 and user.username = ?2 and profile = ?3")
	public List<Integer> findCompanyIdList(String erp, String username, Profile profile);

	@Query("select company.id from CompanyProfile where erp = ?1 and user.username = ?2 and profile in (?3)")
	public List<Integer> findCompanyIdList(String erp, String username, List<Profile> profileList);

	public List<CompanyProfile> findByErp(String erp);

	@Query(c1 + "from CompanyProfile a where a.company.id = ?1 order by a.erp,a.user.fullName")
	List<CompanyProfile> findByCompany(Integer companyId);

	@Query("select count(*) from CompanyProfile a where a.erp = ?1 and a.profile = ?2 and a.user.username = ?3 and a.company.id = ?4")
	Long countByErpAndProfileAndUserAndCompany(String erp, Profile profile, String userUsername, Integer companyId);
	
	@Query("select a.profile from CompanyProfile a where a.company.id = ?1 and a.erp = ?2 and a.user.username = ?3")
	List<Profile> findProfileListByCompanyAndErpAndUser(Integer companyId,String erp,String username);

}
