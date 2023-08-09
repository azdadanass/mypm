package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;
import ma.azdad.repos.CompanyProfileRepos;

@Component
public class CompanyProfileService extends GenericService<Integer, CompanyProfile, CompanyProfileRepos> {

	@Value("${application}")
	private String application;

	public List<Integer> findCompanyIdList(String username, Profile profile) {
		return repos.findCompanyIdList(application, username, profile);
	}

	public List<CompanyProfile> findByErp() {
		return repos.findByErp(application);
	}

	@Override
	public CompanyProfile findOne(Integer id) {
		CompanyProfile companyProfile = super.findOne(id);
		initialize(companyProfile.getUser());
		return companyProfile;
	}

	@Override
	public CompanyProfile save(CompanyProfile model) {
		evictCache("companyService");
		return super.save(model);
	}

	@Cacheable("companyProfileService.findByCompany")
	public List<CompanyProfile> findByCompany(Integer companyId) {
		return repos.findByCompany(companyId);
	}

	public Long countByErpAndProfileAndUserAndCompany(String erp, Profile profile, String userUsername, Integer companyId) {
		return repos.countByErpAndProfileAndUserAndCompany(erp, profile, userUsername, companyId);
	}

	public List<Profile> findProfileListByCompanyAndErpAndUser(Integer companyId, String erp, String username) {
		return repos.findProfileListByCompanyAndErpAndUser(companyId, erp, username);
	}
}
