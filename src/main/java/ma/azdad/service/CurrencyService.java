package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Currency;
import ma.azdad.repos.CurrencyRepos;

@Component
public class CurrencyService extends GenericService<Integer, Currency, CurrencyRepos> {

	@Override
	@Cacheable("currencyService.findAll")
	public List<Currency> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("currencyService.findOne")
	public Currency findOne(Integer id) {
		Currency currency = super.findOne(id);

		return currency;
	}

	@Cacheable("currencyService.findByName")
	public Currency findByName(String name) {
		return repos.findByName(name);
	}

	public Long countByName(Currency currency) {
		return repos.countByName(currency.getName(), currency.getId());
	}

}
