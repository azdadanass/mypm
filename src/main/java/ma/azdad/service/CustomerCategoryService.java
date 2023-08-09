package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.CustomerCategory;
import ma.azdad.repos.CustomerCategoryRepos;

@Component
public class CustomerCategoryService extends GenericService<Integer, CustomerCategory, CustomerCategoryRepos> {

	@Override
	@Cacheable("customerCategoryService.findAll")
	public List<CustomerCategory> findAll() {
		return repos.findAll();
	}

	@Cacheable("customerCategoryService.find")
	public List<CustomerCategory> find() {
		return repos.find();
	}

	@Override
	@Cacheable("customerCategoryService.findOne")
	public CustomerCategory findOne(Integer id) {
		CustomerCategory customerCategory = super.findOne(id);

		return customerCategory;
	}

}
