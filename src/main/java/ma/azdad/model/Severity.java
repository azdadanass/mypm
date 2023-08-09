package ma.azdad.model;

import ma.azdad.utils.Color;

public enum Severity {
	LOW("Low", Color.GREEN),
	MEDIUM("Medium", Color.BLUE),
	HIGH("High", Color.ORANGE),
	CRITICAL("Critical", Color.RED);

	private final String value;
	private final Color color;

	private Severity(String value, Color color) {
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