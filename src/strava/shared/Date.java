package strava.shared;



public class Date {
	
	private int year;
	private int month;
	private int day;
	
	public Date (int yyyy, int mm, int dd) {
		year = yyyy;
		month = mm;
		day = dd;
	}
	
	public boolean isAfter(Date d){
		
		if (d.getYear() > this.year) {
			return false;
		}
		else if (d.getYear() < this.year) {
			return true;
		}
		else {
			if (d.getMonth() > this.month) {
				return false;
			}
			else if(d.getMonth() < this.month) {
				return true;
			}
			else {
				if (d.getDay() > this.day) {
					return false;
				}
				else if (d.getDay() < this.day) {
					return true;
				}
				else {
					return true;
				}
			}
		}
	}
	
	public int getYear(){
		return this.year;
	}
	
	public int getMonth(){
		return this.month;
	}
	
	public int getDay(){
		return this.day;
	}
	
	public String toString(){
		return year + "-" + month + "-" + day;
	}
}
