package ma.azdad.model;

import java.io.Serializable;

import ma.azdad.utils.Color;

public enum ProjectStatus implements Serializable {
	CLOSED("Closed", Color.GREY),
	OPEN("Open", Color.BLUE);

	private final String value;
	private final Color color;

	private ProjectStatus(String value, Color color) {
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

	public static ProjectStatus get(Integer ordinal) {
		try {
			return ProjectStatus.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}
}
