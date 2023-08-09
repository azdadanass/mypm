package ma.azdad.model;

public class MonthYear {

	private Integer month;
	private Integer year;

	@Override
	public boolean equals(Object o) {
		if (o instanceof MonthYear) {
			MonthYear toCompare = (MonthYear) o;
			return this.month.equals(toCompare.getMonth()) && this.year.equals(toCompare.getYear());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return month.hashCode() * year.hashCode();
	}

	public MonthYear(Integer month, Integer year) {
		this.month = month;
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "MonthYear [month=" + month + ", year=" + year + "]";
	}

}
