package ma.azdad.model;

import ma.azdad.utils.Color;

public enum SupplierType {
	PRIVATE("Private", Color.BLUE),
	PUBLIC("Public", Color.GREEN),;

	private final String value;
	private final Color color;

	private SupplierType(String value, Color color) {
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

	public static SupplierType get(Integer ordinal) {
		try {
			return SupplierType.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}
}
