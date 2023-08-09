package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Company;
import ma.azdad.repos.CompanyRepos;

@Component
public class CompanyService extends GenericService<Integer,Company, CompanyRepos> {

	@Override
	@Cacheable("companyService.findAll")
	public List<Company> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("companyService.findOne")
	public Company findOne(Integer id) {
		Company company = super.findOne(id);
		initialize(company.getAccountingCurrency());
		initialize(company.getDefaultReportingCurrency());
		initialize(company.getCeo());
		initialize(company.getCfo());
		initialize(company.getCto());
		initialize(company.getCoo());
		initialize(company.getLom());
		initialize(company.getCashController());
		initialize(company.getExpenseManager());
		initialize(company.getAccountant());
		initialize(company.getBankAccountList());
		initialize(company.getProfileList());
		initialize(company.getFileList());
		initialize(company.getHistoryList());
		initialize(company.getCommentList());
		return company;
	}

	@Cacheable("companyService.findLight")
	public List<Company> findLight() {
		return repos.findLight();
	}
}
