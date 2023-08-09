package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mypm_appraisals_comment")
public class AppraisalsComment extends GenericComment<Appraisals> {

	public AppraisalsComment() {
		super();
	}

	public AppraisalsComment(String title, User user, Appraisals parent) {
		super(title, user, parent);
	}

	public AppraisalsComment(String title, User user) {
		super(title, user);
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
