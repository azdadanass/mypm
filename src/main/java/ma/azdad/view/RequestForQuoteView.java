package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.RequestForQuote;
import ma.azdad.repos.RequestForQuoteRepos;
import ma.azdad.service.RequestForQuoteService;

@ManagedBean
@Component
@Scope("view")
public class RequestForQuoteView extends GenericView<Integer, RequestForQuote, RequestForQuoteRepos, RequestForQuoteService> {

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	public List<RequestForQuote> findAll() {
		return service.findAll();
	}

	public List<RequestForQuote> findByCustomerOrCustomerNull(Integer customerId) {
		return service.findByCustomerOrCustomerNull(customerId);
	}
}
