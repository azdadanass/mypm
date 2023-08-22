package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Logistique;
import ma.azdad.repos.LogistiqueRepos;

@Component
public class LogistiqueService extends GenericService<Integer, Logistique ,LogistiqueRepos>{

	@Override
	@Cacheable("logistiqueService.findOne")
	public Logistique findOne(Integer id) {
		Logistique logistique = super.findOne(id);
		return logistique;
	}

	@Override
	@Cacheable("logistiqueService.findAll")
	public List<Logistique> findAll() {
		return repos.findAll();
	}


}
