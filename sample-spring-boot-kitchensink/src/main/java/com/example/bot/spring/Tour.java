package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tour {
	private String ID;
	private String name;
	private String[] attraction;
	private int duration;
	private int weekDayPrice;
	private int weekEndPrice;
	private Date[] dates;
	
	public Tour() {
		ID = null;
		name = null;
		duration = 0;
		weekDayPrice = 0;
		weekEndPrice = 0;
	}
	public void setID(String _ID) { ID = _ID;}
	public String getID() { return ID;}
	
	public void setName(String _name) { name = _name;}
	public String getName() { return name;}
	
	public void setAttraction(String[] _attraction) { 
		assert _attraction != null : "_attraction null pointer";
		attraction = _attraction;
		}
	public String[] getAttraction() { return attraction; }
	
	public void setDuration(int _duration) { duration = _duration;}
	public int getDuration() { return duration;}
	
//	public int getPrice(Date date) {
//		if (date.getIsWeekend()) return weekEndPrice;
//		else return weekDayPrice;
//	}
	public void setweekDayPrice(int price) { weekDayPrice = price;}
	public int getweekDayPrice() { return weekDayPrice;}
	
	public void setweekEndPrice(int price) { weekEndPrice = price;}
	public int getweekEndPrice() { return weekEndPrice;}
	
	public void setDates(Date[] _dates) { dates = _dates;}
	public Date[] getDates() { return dates;}
	
	
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

	public void setIsWeekend(boolean _isWeekend) { isWeekend = _isWeekend;}
	public boolean getIsWeekend() { return isWeekend;}
	
	public void setDay(int _day) { day = _day;}
	public int getDay() { return day;}
	
	public void setDate(int _date) { date = _date;}
	public int getDate() { return date;}
}
