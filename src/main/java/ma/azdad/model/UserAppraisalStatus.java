package ma.azdad.model;



import java.util.stream.Stream;
import ma.azdad.utils.Color;


public enum UserAppraisalStatus {
	CREATED("CREATED", Color.GREEN),
	EDITED("EDITED", Color.ORANGE),
	SUBMITED("SUBMITED", Color.BLUE),
	REJECTED("REJECTED", Color.L_GREEN),
	APPROVED_LM("APPROVED_LM", Color.GREY),
	APPROVED("APPROVED", Color.PINK),
	
	//Mid Year 
	MYR_SELF_ASSESSMENT("MYR_SELF_ASSESSMENT", Color.ORANGE),
	SUBMITED_MID_YEAR("SUBMITED_MID_YEAR", Color.L_BLUE),
	MYR_APPROVED_LM("MYR_APPROVED_LM", Color.PURPLE),
	MYR_REJECTED("MYR_REJECTED", Color.RED),

	
	//Final year
	FYR_SELF_ASSESSMENT("FYT_SELF_ASSESSMENT", Color.ORANGE),
	SUBMITED_FINAL_YEAR("SUBMITED_FINAL_YEAR", Color.L_BLUE),
	FYR_APPROVED_LM("FYR_APPROVED_LM", Color.PURPLE),
	FYR_REJECTED("FYR_REJECTED", Color.RED),
	
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
