package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum CostCategory {
	TOOLS("Tools", "blue", Arrays.asList(CostType.CAR_RENTAL, CostType.PETROL_HIGHWAY, CostType.CAR_OPEX, CostType.TELECOM, CostType.TOOLS_PURCHASE, CostType.TOOLS_OPEX, CostType.HIGHWAY, CostType.OTHER)),
	DELIVERY("Delivery", "green", Arrays.asList(CostType.HOUSING_ALLOWANCE, CostType.PROJECT_GOODS_PURCHASE, CostType.PROJECT_TRANSPORTATION, CostType.SUBCONTRACTING, CostType.FREIGHT_AND_TRANSIT, CostType.WAREHOUSING, CostType.SOFTWARE_SUPPORT, CostType.OTHER)),
	ADMIN_AND_LOGISTICS("Admin & Logistics", "orange", Arrays.asList(CostType.FACILITY_MANAGEMENT, CostType.ADMIN_PURCHASES, CostType.ADMIN_TRANSPORTATION, CostType.TRAVEL, CostType.OTHER)),
	HR("HR", "pink", Arrays.asList(CostType.SALARY, CostType.BONUS_AND_AWARDS, CostType.OTHER)),
	TPSR("TPSR", "red", Arrays.asList(CostType.TPSR)),
	OTHER("Other", "purple", Arrays.asList(CostType.LEGAL_FEE, CostType.FINANCIAL_SERVICES, CostType.INSURANCE_SERVICES, CostType.LOGISTICS, CostType.PROJECT_PURCHASE, CostType.TRANSPORTATOIN_ALLOWANCE, CostType.OTHER));

	private final String value;
	private final String color;
	private final List<CostType> costTypeList;

	private CostCategory(String value, String color, List<CostType> costTypeList) {
		this.value = value;
		this.color = color;
		this.costTypeList = costTypeList;

	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	public List<CostType> getCostTypeList() {
		return costTypeList;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
