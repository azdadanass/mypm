package ma.azdad.model;

import ma.azdad.utils.Color;

public enum AccessLogType {
	INFO("Info", Color.BLUE),
	SUCCESS("Success", Color.GREEN),
	WARNING("Warning", Color.ORANGE),
	ERROR("Error", Color.RED);

	private final String value;
	private final Color color;

	private AccessLogType(String value, Color color) {
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