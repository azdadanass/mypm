package ma.azdad.model;

public enum RestrictionType {
	EXP_NOT_ACK("Expense(s) not acknownledged",
			"iexpense",
			"Kindly proceed with the paid expense acknowledgement  under Iexpense to restore your system access",
			"You WTR access has been suspended due to paid expense but missing acknowledgement form your side. The system tolerance for this task is 5 days which has been exceeded. To enable the access, please proceed immediately with the expense acknowledgement",
			5,
			false,
			true),
	NO_CONTRACT_ATTACHMENT("No contract attachement", "myhr", "Please contact your HR Manager and provide the signed contract to HR team to restore your system access", "Your WTR & Iexpense access has been temporarily suspended due to missing signed labour Contract attachment in the system. The system tolerance for this task completion is 21 days which has been exceeded", 21, true, true),
	NO_CONTRACT("No Contract", "myhr", "Please contact your HR Manager / Line Manager to activate your labour contract in the system to restore your system access", "You WTR & Iexpense access has been suspended due to missing Labour Contract. The system tolerance for this is 15 days which has been exceeded", 15, true, true),
	EXPIRED_CIN("Expired CIN", "myhr", "Please contact your HR Manager and provide the scan copy of the renewed CIN document to restore your system access", "Your WTR & Iexpense access has been temporarily suspended due to missing renewed CIN document. The system tolerance for this task is 30 days which has been exceeded", 30, true, true),
	EXPIRED_DL("Expired DL", "myhr", "Please contact your HR Manager and provide the scan copy of the renewed driving license document to restore your system access", "Your WTR & Iexpense access has been temporarily suspended due to missing renewed driving license document. The system tolerance for this task is 30 days which has been exceeded", 30, true, true),
	TOOLS_DOUBLE_ALLOCATION("Double Allocation", "mytools", "Kindly proceed with the system & Physical return of the tool doubly allocated to restore your system access", "Your WTR & Iexpense access has been temporarily suspended due to hold of double tools while double allocation is not allowed. The system tolerance for this task is 5 days which has been exceeded", 5, true, true),
	TOOLS_NOT_ACK("Tool(s) not acknownledged", "mytools", "Kindly proceed with the acknowledgement of the allocated tool under Mytools to restore your system access", "Your WTR & Iexpense access has been temporarily suspended due non acknowledgement of allocated tool. The system tolerance for this task is 5 days which has been exceeded", 5, true, true),;

	private final String value;
	private final String system;
	private final String action;
	private final String reason;
	private final Integer tolerance;
	private final Boolean iexpense;
	private final Boolean wtr;

	private RestrictionType(String value, String system, String action, String reason, Integer tolerance, Boolean iexpense, Boolean wtr) {
		this.value = value;
		this.system = system;
		this.action = action;
		this.reason = reason;
		this.tolerance = tolerance;
		this.iexpense = iexpense;
		this.wtr = wtr;
	}

	public String getSystem() {
		return system;
	}

	public String getValue() {
		return value;
	}

	public String getAction() {
		return action;
	}

	public String getReason() {
		return reason;
	}

	public Boolean getIexpense() {
		return iexpense;
	}

	public Boolean getWtr() {
		return wtr;
	}

	public Integer getTolerance() {
		return tolerance;
	}

	@Override
	public String toString() {
		return this.value;
	}
}