package ma.azdad.model;

import ma.azdad.utils.Color;

public enum PaymentTermStartPoint {
	INVOICE_DATE("Invoice Date", Color.GREEN),
	PAC_DATE("PAC Date", Color.BLUE),
	FAC_DATE("FAC Date", Color.ORANGE),;

	private final String value;
	private final Color color;

	private PaymentTermStartPoint(String value, Color color) {
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

	public static PaymentTermStartPoint get(Integer ordinal) {
		try {
			return PaymentTermStartPoint.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}
}