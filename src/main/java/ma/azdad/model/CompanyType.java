package ma.azdad.model;

import ma.azdad.utils.Color;

public enum CompanyType {
	CUSTOMER("Customer", Color.GREEN),
	SUPPLIER("Supplier", Color.BLUE),
	OTHER("Other", Color.RED),
	COMPANY("Company", Color.PURPLE),
	CONSULTANT("Consultant", Color.PINK),;

	private final String value;
	private final Color color;

	private CompanyType(String value, Color color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}
}