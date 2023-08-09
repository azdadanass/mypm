package ma.azdad.model;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bankaccount_file")
public class BankAccountFile extends GenericFile<BankAccount> {

	public BankAccountFile() {
	}

	public BankAccountFile(File file, String type, String name, User user) {
		super(file, type, name, user);
	}

	public BankAccountFile(File file, String type, String name, User user, BankAccount parent) {
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
