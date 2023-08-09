package ma.azdad.model;

import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum CustomerStatus {
	EDITED("Edited", Color.ORANGE),
	APPROVED("Approved", Color.GREEN),
	REJECTED("Rejected", Color.RED),;

	private final String value;
	private final Color color;

	private CustomerStatus(String value, Color color) {
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

	public static CustomerStatus get(Integer ordinal) {
		try {
			return CustomerStatus.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}

	public static CustomerStatus getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}