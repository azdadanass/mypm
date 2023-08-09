package ma.azdad.model;

import ma.azdad.utils.Color;

public enum DelegationDetailType {
	PM("PM", Color.GREEN),
	LM("LM", Color.ORANGE),
	LOB("LOB", Color.BLUE),;

	private final String value;
	private final Color color;

	private DelegationDetailType(String value, Color color) {
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