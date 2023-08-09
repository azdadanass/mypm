package ma.azdad.service;

import java.util.List;

import org.springframework.stereotype.Component;

import ma.azdad.model.Office;
import ma.azdad.repos.OfficeRepos;

@Component
public class OfficeService extends GenericService<Integer,Office, OfficeRepos> {

	public List<Office> findAll() {
		return repos.findAll();
	}

	public Office findOne(Integer id) {
		Office office = super.findOne(id);

		return office;
	}

	public List<Office> find() {
		return repos.find();
	}

}
