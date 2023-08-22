package ma.azdad.model;


import java.util.stream.Stream;

import ma.azdad.utils.Color;

public enum AppraisalsStatus {
	
	CLOSED("Closed", Color.RED),
	MID_YEAR_REVIEW("MidYearReview", Color.BLUE),
	FINAL_REVIEW("FinalReview", Color.GREY),
	OPEN("Open", Color.GREEN);
	;
	private final String value;
	private final Color color;

	
	private AppraisalsStatus(String value, Color color) {
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
	
	public static AppraisalsStatus getByValue(String status) {
		try {
			return Stream.of(values()).filter(i -> status.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}
