package ma.azdad.model;

public enum ProjectCatalogDetailType {
	DELIVERY("Delivery"),
	ACCEPTANCE("Acceptance"),;

	private final String value;

	private ProjectCatalogDetailType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
