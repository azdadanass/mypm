package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.RequestForQuote;
import ma.azdad.repos.RequestForQuoteRepos;

@Component
public class RequestForQuoteService extends GenericService<Integer, RequestForQuote, RequestForQuoteRepos> {

	@Override
	@Cacheable("requestForQuoteService.findAll")
	public List<RequestForQuote> findAll() {
		return super.findAll();
	}

	@Cacheable("requestForQuoteService.findByCustomerOrCustomerNull")
	public List<RequestForQuote> findByCustomerOrCustomerNull(Integer customerId) {
		return repos.findByCustomerOrCustomerNull(customerId);
	}

	public void updateExistingAndCustomer(Integer id, Boolean existing, Integer customerId) {
		repos.updateExistingAndCustomer(id, existing, customerId);
		evictCache();
	}

}
