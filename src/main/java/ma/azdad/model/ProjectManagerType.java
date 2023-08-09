package ma.azdad.model;

import ma.azdad.utils.Color;

public enum ProjectManagerType {
	PRESALES_MANAGER("Pre-Sales Manager", Color.GREEN),
	TECHNICAL_MANAGER("Technical Manager", Color.BLUE),
	HARDWARE_MANAGER("Hardware Manager", Color.ORANGE),
	QUALITY_MANAGER("Quality Manager", Color.RED),
	SYSTEM_ENGINEER("System Engineer", Color.PURPLE);

	private final String value;
	private final Color color;

	private ProjectManagerType(String value, Color color) {
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