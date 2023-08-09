package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(eager = true)
@Component
@Scope("application")
public class StaticView {

	@Value("${applicationName}")
	private String applicationName;

	@Value("${spring.profiles.active}")
	private String erp;

	private List<String> countryList = new ArrayList<>();

	@PostConstruct
	public void init() {
		initCountryList();
	}

	private void initCountryList() {
		String[] locales = Locale.getISOCountries();
		for (String countryCode : locales)
			countryList.add(new Locale("", countryCode).getDisplayCountry(Locale.US));
	}

	public Boolean getGcom() {
		return "gcom".contentEquals(erp);
	}

	public Boolean getMise() {
		return "mise".contentEquals(erp);
	}

	public String getApplicationName() {
		return applicationName;
	}

	public List<String> getCountryList() {
		return countryList;
	}

	public String getErp() {
		return erp;
	}

}