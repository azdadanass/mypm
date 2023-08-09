package ma.azdad.model;



import java.util.stream.Stream;
import ma.azdad.utils.Color;


public enum UserAppraisalStatus {
	CREATED("Created", Color.GREEN),
	
	SUBMITED("Submited", Color.BLUE),
	APPROVED_LM("Approved 1", Color.GREY),
	APPROVED("Approved 2", Color.PINK),
	
	//Mid Year 
	SUBMITED_MID_YEAR("Submited", Color.L_BLUE),
	MYR_APPROVED_LM("Approved 1", Color.PURPLE),
	MYR_APPROVED("Approved 2", Color.YELLOW),
	
	//Final year
	SUBMITED_FINAL_YEAR("Submited", Color.L_BLUE),
	FYR_APPROVED_LM("Approved 1", Color.PURPLE),
	FYR_APPROVED("Approved 2", Color.YELLOW),
	
	//CANCELED("Canceled", Color.RED),
	CLOSED("Closed", Color.RED),
	;
	private final String value;
	private final Color color;

	


	private UserAppraisalStatus(String value, Color color) {
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
	
	public static UserAppraisalStatus getByValue(String status) {
		try {
			return Stream.of(values()).filter(i -> status.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}

}
