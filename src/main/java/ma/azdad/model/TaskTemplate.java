package ma.azdad.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class TaskTemplate extends GenericModel<Integer> {

	private String name;

	private List<TaskTemplateDetail> detailList = new ArrayList<>();

	@Override
	public boolean filter(String query) {
		return contains(query, name);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
	}

	public void addDetail(TaskTemplateDetail detail) {
		detail.setTaskTemplate(this);
		detailList.add(detail);
	}

	public void removeDetail(TaskTemplateDetail detail) {
		detail.setTaskTemplate(null);
		detailList.remove(detail);
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "taskTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TaskTemplateDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<TaskTemplateDetail> detailList) {
		this.detailList = detailList;
	}

}
