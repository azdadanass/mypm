package ma.azdad.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.BankAccount;
import ma.azdad.repos.BankAccountRepos;

@Component
@Transactional
public class BankAccountService extends GenericService<Integer,BankAccount, BankAccountRepos> {

	public Map<Integer, String> getFullNameMap() {
		return repos.getFullNameMap().stream().collect(Collectors.toMap(i -> (Integer) i[0], i -> (String) i[1]));
	}
	
	
	public void generateFullNameByCompany(Integer companyId) {
		repos.findByCompany(companyId).forEach(b->{
			b.generateFullName();
			repos.save(b);
		});
		evictCache();
	}
	
	public void generateFullNameScript() {
		repos.findWithoutFullName().forEach(b->{
			b.generateFullName();
			repos.save(b);
		});
		evictCache();
	}

}