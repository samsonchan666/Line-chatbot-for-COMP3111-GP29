package com.example.bot.spring;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.*;
//
//
//@Entity // This tells Hibernate to make a table out of this class
//@Table(name = "tour")
public class Tour {
//	@Id
	private String id;
	
	private String name;
	private String[] attraction;
	private int duration;
	private int weekDayPrice;
	private int weekEndPrice;
//	private Date[] dates;
	
	public Tour(
			String id,
			String name,
			String[] attraction,
			int duration,
			int weekDayPrice,
			int weekEndPrice
//			Date[] dates
			) {
		this.id = id;
		this.name = name;
		this.attraction = attraction;
		this.duration = duration;
		this.weekDayPrice = weekDayPrice;
		this.weekEndPrice = weekEndPrice;
//		this.dates = dates;
	}
	public void setID(String id) { this.id = id;}
	public String getID() { return this.id;}
	
	public void setName(String name) { this.name = name;}
	public String getName() { return this.name;}
	
	public void setAttraction(String[] attraction) { 
		assert attraction != null : "_attraction null pointer";
		this.attraction = attraction;
		}
	public String[] getAttraction() { return this.attraction; }
	
	public void setDuration(int duration) { this.duration = duration;}
	public int getDuration() { return this.duration;}
	
//	public int getPrice(Date date) {
//		if (date.getIsWeekend()) return weekEndPrice;
//		else return weekDayPrice;
//	}
	public void setweekDayPrice(int price) { this.weekDayPrice = price;}
	public int getweekDayPrice() { return this.weekDayPrice;}
	
	public void setweekEndPrice(int price) { this.weekEndPrice = price;}
	public int getweekEndPrice() { return this.weekEndPrice;}
	
//	public void setDates(Date[] dates) { this.dates = dates;}
//	public Date[] getDates() { return this.dates;}
//	
	
}

	class Date{
	private boolean isWeekend;
	private int date;
	private int day;
	
	public Date() {
		isWeekend = false;
		day = 0;
		date = 0;
	}

	public void setIsWeekend(boolean isWeekend) { this.isWeekend = isWeekend;}
	public boolean getIsWeekend() { return this.isWeekend;}
	
	public void setDay(int day) { this.day = day;}
	public int getDay() { return this.day;}
	
	public void setDate(int date) { this.date = date;}
	public int getDate() { return this.date;}
}
