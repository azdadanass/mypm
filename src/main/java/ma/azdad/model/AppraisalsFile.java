package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_appraisals_file")
public class AppraisalsFile extends GenericFile<Appraisals> {

	public AppraisalsFile() {

	}

	public AppraisalsFile(File file, String type, String name, User user, Appraisals parent) {
		super(file, type, name, user, parent);
	}

	public AppraisalsFile(File file, String type, String name, User user) {
		super(file, type, name, user);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
