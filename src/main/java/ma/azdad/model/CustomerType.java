package ma.azdad.model;

import ma.azdad.utils.Color;

public enum CustomerType {
	PRIVATE("Private", Color.BLUE),
	PUBLIC("Public", Color.GREEN),
	DISTRIBUTOR("Distributor", Color.GREEN),
	INTERCOMPANY("Intercompany", Color.BLUE),
	;

	private final String value;
	private final Color color;

	private CustomerType(String value, Color color) {
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

	public static CustomerType get(Integer ordinal) {
		try {
			return CustomerType.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}
}
