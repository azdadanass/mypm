package ma.azdad.model;

public enum Priority {
	MINOR("Minor", "green"),
	MEDIUM("Medium", "blue"),
	HIGH("High", "orange"),
	CRITICAL("Critical", "red");

	private final String value;
	private final String color;

	private Priority(String value, String color) {
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return this.value;
	}

}