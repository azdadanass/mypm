package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class CompanyProfile extends GenericModel<Integer> {

	private Profile profile;
	private String erp;

	private User user;
	private Company company;

	public CompanyProfile() {
	}

	public CompanyProfile(Integer id, Profile profile, String erp, String userFullName, String userPhoto, Boolean userActive, String companyName) {
		super(id);
		this.profile = profile;
		this.erp = erp;
		this.user = new User();
		this.user.setFullName(userFullName);
		this.user.setPhoto(userPhoto);
		this.user.setActive(userActive);
		this.company = new Company();
		this.company.setName(companyName);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, erp, getUserFullName(), profile.name(),getUserActive()?"Active":"Inactive");
	}

	// getters & setters
	@Transient
	public String getUserFullName() {
		return user != null ? user.getFullName() : null;
	}

	@Transient
	public String getUserPhoto() {
		return user != null ? user.getPhoto() : null;
	}

	@Transient
	public String getCompanyName() {
		return company != null ? company.getName() : null;
	}

	@Transient
	public Boolean getUserActive() {
		return user != null ? user.getActive() : null;
	}

	@Transient
	public String getUserUsername() {
		return user == null ? null : user.getUsername();
	}

	@Transient
	public void setUserUsername(String userUsername) {
		if (user == null)
			user = new User();
		user.setUsername(userUsername);
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public void setErp(String erp) {
		this.erp = erp;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getErp() {
		return erp;
	}

}
