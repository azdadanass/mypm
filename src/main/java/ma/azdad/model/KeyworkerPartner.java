package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mypm_Keyworke_Partner")
public class KeyworkerPartner extends GenericModel<Integer>{

	
	private UserAppraisal userappraisal;
	private User resource;

	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public UserAppraisal getUserappraisal() {
		return userappraisal;
	}

	public void setUserappraisal(UserAppraisal userappraisal) {
		this.userappraisal = userappraisal;
	}
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public User getResource() {
		return resource;
	}

	public void setResource(User resource) {
		this.resource = resource;
	}
	
	
}
