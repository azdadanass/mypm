package ma.azdad.model;

import ma.azdad.utils.Color;

public enum ProjectCustomerType {
	CUSTOMER("Customer", Color.BLUE),
	COMPANY("Company", Color.GREEN);

	private final String value;
	private final Color color;

	private ProjectCustomerType(String value, Color color) {
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