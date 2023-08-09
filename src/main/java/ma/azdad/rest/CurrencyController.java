package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.model.Currency;
import ma.azdad.service.CurrencyService;

@RestController
public class CurrencyController {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CurrencyService currencyService;

	@GetMapping("/rest/currency/get")
	public Currency test() {
		return currencyService.findOne(1);
	}

}
