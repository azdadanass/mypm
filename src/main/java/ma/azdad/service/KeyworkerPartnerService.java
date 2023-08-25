package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.KeyworkerPartner;
import ma.azdad.repos.KeyworkerPartnerRepos;

@Component
public class KeyworkerPartnerService extends GenericService<Integer,KeyworkerPartner, KeyworkerPartnerRepos>{

	
	@Override
	@Cacheable("KeyworkerPartnerService.findAll")
	public List<KeyworkerPartner> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("KeyworkerPartnerService.findOne")
	public KeyworkerPartner findOne(Integer id) {
		KeyworkerPartner keyworkerPartner = super.findOne(id);

		return keyworkerPartner;
	}
}
