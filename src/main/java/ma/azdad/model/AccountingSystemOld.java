package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum AccountingSystemOld {
	MOR("MOR", Color.GREEN);

	private final String value;
	private final Color color;

	private AccountingSystemOld(String value, Color color) {
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

	public static AccountingSystemOld getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}