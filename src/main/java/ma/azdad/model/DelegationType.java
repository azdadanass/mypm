package ma.azdad.model;

import ma.azdad.utils.Color;

public enum DelegationType {
	PROJECT("Project Management", Color.BLUE),
	LOB("LOB management", Color.GREEN),
	USER("Resource Management", Color.ORANGE),;

	private final String value;
	private final Color color;

	private DelegationType(String value, Color color) {
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