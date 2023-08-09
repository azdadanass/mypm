package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.SupplierCategory;
import ma.azdad.repos.SupplierCategoryRepos;

@Component
public class SupplierCategoryService extends GenericService<Integer, SupplierCategory, SupplierCategoryRepos> {

	@Override
	@Cacheable("supplierCategoryService.findAll")
	public List<SupplierCategory> findAll() {
		return repos.findAll();
	}

	@Cacheable("supplierCategoryService.find")
	public List<SupplierCategory> find() {
		return repos.find();
	}

	@Override
	@Cacheable("supplierCategoryService.findOne")
	public SupplierCategory findOne(Integer id) {
		SupplierCategory supplierCategory = super.findOne(id);

		return supplierCategory;
	}

}
