package ma.azdad.model;



import java.util.stream.Stream;
import ma.azdad.utils.Color;


public enum UserAppraisalStatus {
	CREATED("Created", Color.GREEN),
	EDITED("Edited", Color.ORANGE),
	SUBMITED("Submitted", Color.BLUE),
	REJECTED("Rejected", Color.L_GREEN),
	APPROVED_LM("Approved L1", Color.GREY),
	APPROVED("Approved", Color.PINK),
	
	//Mid Year 
	MYR_SELF_ASSESSMENT("MYR Self Assessment", Color.ORANGE),
	SUBMITED_MID_YEAR("MYR Submitted", Color.L_BLUE),
	MYR_APPROVED_LM("MYR Approved", Color.PURPLE),
	MYR_REJECTED("MYR Rejected", Color.RED),

	
	//Final year
	FYR_SELF_ASSESSMENT("FY Self Assessment", Color.ORANGE),
	SUBMITED_FINAL_YEAR("FYA Submitted", Color.L_BLUE),
	FYR_APPROVED_LM("FYA Approved", Color.PURPLE),
	FYR_REJECTED("FYA Rejected", Color.RED),
	
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
