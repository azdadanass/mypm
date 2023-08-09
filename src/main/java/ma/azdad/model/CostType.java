package ma.azdad.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CostType {
	CAR_RENTAL("Car Rental"),
	PETROL_HIGHWAY("Petrol"),
	CAR_OPEX("Car OPEX"),
	TELECOM("Telecom"),
	TOOLS_PURCHASE("Tools Purchase"),

	HOUSING_ALLOWANCE("Housing allowance & Accomodations"),
	PROJECT_GOODS_PURCHASE("Project Goods Purchase"),
	PROJECT_TRANSPORTATION("Project Transportation"),
	SUBCONTRACTING("Subcontracting"),
	WAREHOUSING("Warehousing"),

	FACILITY_MANAGEMENT("Facility Management"),
	ADMIN_PURCHASES("Admin / Facility Purchases"),
	ADMIN_TRANSPORTATION("Admin Transportation"),

	SALARY("Salary"),
	BONUS_AND_AWARDS("Bonus & Awards"),

	LEGAL_FEE("Legal Fee"),
	FINANCIAL_SERVICES("Financial Services"),
	INSURANCE_SERVICES("Insurance Services"),
	LOGISTICS("Logistics"),
	PROJECT_PURCHASE("Project Purchase"),
	TRANSPORTATOIN_ALLOWANCE("Transportation Allowance"),
	OTHER("Other"),

	FREIGHT_AND_TRANSIT("Customs, Freight & Transit"),

	TOOLS_OPEX("Tools OPEX"),
	TRAVEL("Travel & Travel Accommodations"),
	HIGHWAY("Highway"),
	SOFTWARE_SUPPORT("Software / Support"),
	TPSR("TPSR");

	public static List<CostType> MAPPABLE_LIST;
	public static List<Integer> MAPPABLE_LIST_INTEGER;
	static {
		MAPPABLE_LIST = Arrays.asList(CostType.CAR_RENTAL, CostType.PETROL_HIGHWAY, CostType.CAR_OPEX, CostType.TELECOM, CostType.TOOLS_PURCHASE, CostType.TOOLS_OPEX, CostType.PROJECT_GOODS_PURCHASE, CostType.FREIGHT_AND_TRANSIT, CostType.PROJECT_PURCHASE, CostType.PROJECT_TRANSPORTATION, CostType.WAREHOUSING, CostType.FACILITY_MANAGEMENT, CostType.ADMIN_PURCHASES, CostType.HIGHWAY);

		MAPPABLE_LIST_INTEGER = MAPPABLE_LIST.stream().map(i -> Integer.valueOf(i.ordinal())).collect(Collectors.toList());
	}

	private final String value;

	private CostType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Boolean mappable() {
		return MAPPABLE_LIST.contains(this);
	}

	public Boolean showDates() {
		return Arrays.asList(CAR_RENTAL, PETROL_HIGHWAY, TELECOM, HOUSING_ALLOWANCE, WAREHOUSING, FACILITY_MANAGEMENT, SALARY, BONUS_AND_AWARDS, CostType.HIGHWAY).contains(this);
	}

	public Boolean requireDates() {
		return Arrays.asList(CAR_RENTAL, PETROL_HIGHWAY, TELECOM, HIGHWAY).contains(this);
	}

	public Boolean isForTool() {
		return Arrays.asList(CostType.CAR_RENTAL, CostType.CAR_OPEX, CostType.PETROL_HIGHWAY, CostType.HIGHWAY, CostType.TELECOM, CostType.TOOLS_PURCHASE, CostType.TOOLS_OPEX).contains(this);
	}

	public Boolean isForDeliveryRequest() {
		return Arrays.asList(CostType.PROJECT_GOODS_PURCHASE, CostType.FREIGHT_AND_TRANSIT, CostType.PROJECT_PURCHASE).contains(this);
	}

	public Boolean isForTransportationRequest() {
		return Arrays.asList(CostType.PROJECT_TRANSPORTATION).contains(this);
	}

	public Boolean isForWarehouse() {
		return Arrays.asList(CostType.WAREHOUSING).contains(this);
	}

	public Boolean isForOffice() {
		return Arrays.asList(CostType.FACILITY_MANAGEMENT, CostType.ADMIN_PURCHASES).contains(this);
	}

	public Boolean isForUser() {
		return Arrays.asList(CostType.SALARY, CostType.BONUS_AND_AWARDS).contains(this);
	}

	@Override
	public String toString() {
		return this.value;
	}

}
