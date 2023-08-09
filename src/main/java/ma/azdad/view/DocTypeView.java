package ma.azdad.view;

import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.service.DocTypeService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class DocTypeView {

	@Autowired
	protected DocTypeService docTypeService;

	public List<String> findByType(String type) {
		return docTypeService.findByType(type);
	}

	public List<String> findByType(String type, Integer filter) {
		return docTypeService.findByType(type, filter);
	}

}