package ma.azdad.service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import ma.azdad.model.AccountingSystem;
import ma.azdad.repos.AccountingSystemRepos;

@Component
public class AccountingSystemService extends GenericService<Integer, AccountingSystem, AccountingSystemRepos> {

	public Boolean isNumeroExists(Integer numero, Integer companyId) {
		return ObjectUtils.firstNonNull(repos.countByCompanyAndNumero(companyId, numero), 0l) > 0;
	}
}
