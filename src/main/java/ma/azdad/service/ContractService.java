package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Contract;
import ma.azdad.repos.ContractRepos;

@Component
public class ContractService extends GenericService<Integer, Contract, ContractRepos> {

	@Override
	@Cacheable("contractService.findAll")
	public List<Contract> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("contractService.findOne")
	public Contract findOne(Integer id) {
		Contract contract = super.findOne(id);

		return contract;
	}

	public List<Contract> findByCustomerId(Integer customerId) {
		return repos.findByCustomerId(customerId);
	}

}
