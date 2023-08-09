package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DocType;
import ma.azdad.repos.DocTypeRepos;

@Component
@Transactional
public class DocTypeService extends GenericService<Integer,DocType, DocTypeRepos> {

	@Value("${application}")
	private String application;

	public List<String> findByType(String type) {
		return repos.findByAppAndType(application, type);
	}

	public List<String> findByType(String type, Integer filter) {
		return repos.findByAppAndType(application, type, filter);
	}

}