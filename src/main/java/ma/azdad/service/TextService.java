package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Text;
import ma.azdad.repos.TextRepos;

@Component
public class TextService extends GenericService<Integer,Text, TextRepos> {

	@Value("${application}")
	private String application;

	@Override
	@Cacheable("textService.findAll")
	public List<Text> findAll() {
		return repos.findAll();
	}

	@Cacheable("textService.find")
	public List<Text> find() {
		return repos.find(application);
	}

	@Override
	@Cacheable("textService.findOne")
	public Text findOne(Integer id) {
		Text text = super.findOne(id);

		return text;
	}

	@Cacheable("textService.findValueList")
	public List<String> findValueList(String beanName) {
		return repos.findValueList(application, beanName);
	}

	@Cacheable("textService.findValueList")
	public List<String> findValueList(String beanName, String type) {
		return repos.findValueList(application, beanName, type);
	}

}
