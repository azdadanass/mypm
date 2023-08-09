package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity

public class Office extends GenericModel<Integer> {

	private String name;
	protected Double latitude = 33.966171;
	protected Double longitude = -6.8678663;
	private String address1;
	private String address2;
	private String address3;
	private String phone;
	private String fax;

	public Office() {
		super();
	}

	public Office(Integer id, String name, String address1, String address2, String address3, String phone, String fax) {
		super(id);
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.phone = phone;
		this.fax = fax;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && phone != null)
			result = result || phone.toLowerCase().contains(query);
		if (!result && fax != null)
			result = result || fax.toLowerCase().contains(query);
		if (!result && address1 != null)
			result = result || address1.toLowerCase().contains(query);
		if (!result && address2 != null)
			result = result || address2.toLowerCase().contains(query);
		if (!result && address3 != null)
			result = result || address3.toLowerCase().contains(query);
		return result;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return this.name;
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
