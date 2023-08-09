package ma.azdad.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RevenueType {
	GOODS_SUPPLY("Goods Supply"),
	SERVICE_SUPPLY("Service Supply"),;

	public static List<RevenueType> MAPPABLE_LIST;
	public static List<Integer> MAPPABLE_LIST_INTEGER;
	static {
		MAPPABLE_LIST = Arrays.asList(RevenueType.GOODS_SUPPLY);
		MAPPABLE_LIST_INTEGER = MAPPABLE_LIST.stream().map(i -> Integer.valueOf(i.ordinal())).collect(Collectors.toList());
	}

	private final String value;

	private RevenueType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public Boolean mappable() {
		return MAPPABLE_LIST.contains(this);
	}

	public Boolean showDates() {
		return Arrays.asList().contains(this);
	}

	public Boolean requireDates() {
		return Arrays.asList().contains(this);
	}

	public Boolean isForDeliveryRequest() {
		return Arrays.asList(RevenueType.GOODS_SUPPLY).contains(this);
	}

	@Override
	public String toString() {
		return this.value;
	}

}
