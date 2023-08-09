package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Contract;
import ma.azdad.repos.ContractRepos;
import ma.azdad.service.ContractService;

@ManagedBean
@Component
@Scope("view")
public class ContractView extends GenericView<Integer, Contract, ContractRepos, ContractService> {

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	// generic

	public List<Contract> findByCustomerId(Integer customerId) {
		return service.findByCustomerId(customerId);
	}

	// getters & setters
	public Contract getModel() {
		return model;
	}

	public void setModel(Contract model) {
		this.model = model;
	}

}
