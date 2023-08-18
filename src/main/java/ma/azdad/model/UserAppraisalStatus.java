package ma.azdad.model;



import java.util.stream.Stream;
import ma.azdad.utils.Color;


public enum UserAppraisalStatus {
	CREATED("CREATED", Color.GREEN),
	EDITED("EDITED", Color.ORANGE),
	SUBMITED("SUBMITED", Color.BLUE),
	//REJECTED("REJECTED", Color.BLUE),
	APPROVED_LM("APPROVED_LM", Color.GREY),
	APPROVED("APPROVED", Color.PINK),
	
	//Mid Year 
	SUBMITED_MID_YEAR("SUBMITED_MID_YEAR", Color.L_BLUE),
	MYR_APPROVED_LM("MYR_APPROVED_LM", Color.PURPLE),
	MYR_APPROVED("MYR_APPROVED", Color.YELLOW),
	
	//Final year
	SUBMITED_FINAL_YEAR("SUBMITED_FINAL_YEAR", Color.L_BLUE),
	FYR_APPROVED_LM("FYR_APPROVED_LM", Color.PURPLE),
	FYR_APPROVED("FYR_APPROVED", Color.YELLOW),
	
	//CANCELED("Canceled", Color.RED),
	CLOSED("CLOSED", Color.RED),
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
