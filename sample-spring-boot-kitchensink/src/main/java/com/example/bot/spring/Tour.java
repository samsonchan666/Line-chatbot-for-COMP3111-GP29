package com.example.bot.spring;

import java.lang.*;
import java.util.List;

public class Tour {
	private String id;
	
	private String name;
	private String attraction;
	private int duration;
	private int weekDayPrice;
	private int weekEndPrice;
	private String dates;
	public enum Keyword{ DATE, ATTRACTION}
	
	public Tour(
			String id,
			String name,
			String attraction,
			int duration,
			int weekDayPrice,
			int weekEndPrice,
			String dates ) {
		this.id = id;
		this.name = name;
		this.attraction = attraction;
		this.duration = duration;
		this.weekDayPrice = weekDayPrice;
		this.weekEndPrice = weekEndPrice;
		this.dates = dates;
	}
	public void setID(String id) { this.id = id;}
	public String getID() { return this.id;}
	
	public void setName(String name) { this.name = name;}
	public String getName() { return this.name;}
	
	public void setAttraction(String attraction) {
//		assert attraction != null : "_attraction null pointer";
		this.attraction = attraction;
		}
	public String getAttraction() { return this.attraction; }
	
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
	
	public void setDates(String dates) { this.dates = dates;}
	public String getDates() { return this.dates;}

	public StringBuilder getDetailTourInfo(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + " " + this.name + "\n");
		tourBuilder.append("Attractions:\n" + this.attraction + "\n");
		tourBuilder.append("We have confirmed tour on " + this.dates + "\n");
		tourBuilder.append("Fee: Weekday " + this.weekDayPrice + " / Weekend " + this.weekEndPrice + "\n");
		tourBuilder.append("Do you want to book this one?");
		return tourBuilder;
	}

	public StringBuilder getBasicTourInfo(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\n");
		return tourBuilder;
	}

	public static StringBuilder getBasicTourListInfo(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfo());
		}
		return tourBuilder;
	}

	public StringBuilder getBasicTourInfoWithPrice(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\t" + this.weekDayPrice + "\n");
		return tourBuilder;
	}

	public static StringBuilder getBasicTourListInfoWithPrice(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfoWithPrice());
		}
		return tourBuilder;
	}

	public static String getTopicSentence(List<Tour> tourList, Keyword keyword){
		switch (keyword){
			case DATE:
				return "There are " + tourList.size() + " tours available on that day\n";
			case ATTRACTION:
				return "There are " + tourList.size() + " tours related\n";
			default:
				return null;
		}
	}

	public static StringBuilder getBasicTourInfoByKeyword(List<Tour> tourList, Keyword keyword){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(getTopicSentence(tourList, keyword));
		return tourBuilder.append(getBasicTourListInfo(tourList));
	}

	public static StringBuilder getBasicTourInfoSortByPrice(List<Tour> tourList, Keyword keyword){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(getTopicSentence(tourList, keyword));
		//tourBuilder.append("Sorted by Price\n" );

		sortTourListByPrice(tourList);
		return tourBuilder.append(getBasicTourListInfoWithPrice(tourList));
	}

	public static void sortTourListByPrice(List<Tour> tourList){
		Tour temp;
		for (int x=0; x<tourList.size(); x++) // bubble sort outer loop
		{
			for (int i=0; i < tourList.size()-i; i++) {
				if (tourList.get(i).getweekDayPrice() > tourList.get(i+1).getweekDayPrice())
				{
					temp = tourList.get(i);
					tourList.set(i,tourList.get(i+1) );
					tourList.set(i+1, temp);
				}
			}
		}
	}
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