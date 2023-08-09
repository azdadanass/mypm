package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum RevenueCategory {
	GOODS_SUPPLY("Goods Supply", "blue", Arrays.asList(RevenueType.GOODS_SUPPLY)),
	SERVICE_SUPPLY("Service Supply", "green", Arrays.asList(RevenueType.SERVICE_SUPPLY)),
	GOODS_AND_SERVICE_SUPPLY("Goods And Service Supply", "pink", Arrays.asList(RevenueType.GOODS_SUPPLY, RevenueType.SERVICE_SUPPLY)),;

	private final String value;
	private final String color;
	private final List<RevenueType> revenueTypeList;

	private RevenueCategory(String value, String color, List<RevenueType> revenueTypeList) {
		this.value = value;
		this.color = color;
		this.revenueTypeList = revenueTypeList;

	}

	public String getValue() {
		return value;
	}

	public String getColor() {
		return color;
	}

	public List<RevenueType> getRevenueTypeList() {
		return revenueTypeList;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
