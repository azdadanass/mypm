package ma.azdad.model;

import java.io.Serializable;

import ma.azdad.utils.Color;

public enum ProjectCategory implements Serializable {
	PROJECT("project", Color.GREY),
	COSTCENTER("costcenter", Color.BLUE);

	private final String value;
	private final Color color;

	private ProjectCategory(String value, Color color) {
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

	public static ProjectCategory get(Integer ordinal) {
		try {
			return ProjectCategory.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}
}
