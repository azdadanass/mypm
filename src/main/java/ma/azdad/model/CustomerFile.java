package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerFile extends GenericFile<Customer> {

	public CustomerFile() {
	}

	public CustomerFile(File file, String type, String name, User user) {
		super(file, type, name, user);
	}

	public CustomerFile(File file, String type, String name, User user, Customer parent) {
		super(file, type, name, user, parent);
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
