package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "e_general_data")
public class EGeneralData implements Serializable{
	private String username;
	private String firstname;
	private String lastname;
	private String fullname;
	private String photo;
	private String email;
	private String email2;
	private Date birthday;
	private String cin;
	


	public EGeneralData() {
	}

	public EGeneralData(String username) {
		this.username = username;
	}

	public boolean equals(Object obj) {
		return (this.username).equals(((EGeneralData) obj).getUsername());
	}

	@Id
	@Column(name = "RESURCE_ID", unique = true, nullable = false, length = 500)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "NAME", length = 500)
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "SURNAME", length = 500)
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "ID_PHOTO", length = 100)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "email", length = 500)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDAY", length = 10)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "CIN", length = 100)
	public String getCin() {
		return this.cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	

	

	

	@Column(name = "secondary_mail_adresse", length = 200)
	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	

	


	@Column(name = "fullname", length = 100)
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	
}
