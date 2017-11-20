package com.example.bot.spring;

import java.lang.*;
import java.util.List;

/**
 * <h1>Tour details</h1>
 * The Tour class stores the information
 * of Tours 
 *
 */
public class Tour {
	private String id;
	
	private String name;
	private String attraction;
	private int duration;
	private int weekDayPrice;
	private int weekEndPrice;
	private String dates;
	public enum Keyword{ DATE, ATTRACTION, PREFERENCE}
	
	/**
	 * This is the constructor of the
	 * Tour class with all the data 
	 * stored when constructed
	 * @param id Tour's ID
	 * @param name Tour's name
	 * @param attraction Tour's attraction
	 * @param duration Tour's duration
	 * @param weekDayPrice Tour's weekday price
	 * @param weekEndPrice Tour's weekend price
	 * @param dates  Tour's date
	 */
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
	
	/**
	 * This method sets the id of the tour
	 * to the input String
	 * @param id Tour's ID
	 */
	public void setID(String id) { this.id = id;}
	
	/**
	 * This method returns the id of the tour
	 * as String
	 * @return id Tour's ID
	 */
	public String getID() { return this.id;}
	
	/**
	 * This method sets the name of the tour
	 * to the input String 
	 * @param name Tour's name
	 */
	public void setName(String name) { this.name = name;}
	
	/**
	 * This method returns the name of the
	 * tour as String
	 * @return name Tour's name
	 */
	public String getName() { return this.name;}
	
	/**
	 * This method sets the attraction of the 
	 * tour to the input String
	 * @param attraction Tour's attraction
	 */
	public void setAttraction(String attraction) {
		this.attraction = attraction;
		}
	
	/**
	 * This method returns the attraction of the
	 * tour as String
	 * @return attraction Tour's attraction
	 */
	public String getAttraction() { return this.attraction; }
	
	
	/**
	 * This method sets the duration of the
	 * tour to the input integer
	 * @param duration Tour's duration
	 */
	public void setDuration(int duration) { this.duration = duration;}
	
	/**
	 * This method returns the duration of
	 * the tour as integer
	 * @return duration Tour's duration
	 */
	public int getDuration() { return this.duration;}
	
	/**
	 * This method sets the weekday price
	 * of the tour to the input integer
	 * @param price Tour's weekday price
	 */
	public void setweekDayPrice(int price) { this.weekDayPrice = price;}
	
	/**
	 * This method returns the weekday
	 * price of the tour as integer
	 * @return  weekDayPrice Tour's weekday price
	 */
	public int getweekDayPrice() { return this.weekDayPrice;}
	
	/**
	 * This method sets the weekend price
	 * of the tour to the input integer
	 * @param price Tour's weekend price
	 */
	public void setweekEndPrice(int price) { this.weekEndPrice = price;}
	
	/**
	 * This method returns the weekend
	 * price of the tour as integer
	 * @return weekEndPrice Tour's weekend price
	 */
	public int getweekEndPrice() { return this.weekEndPrice;}
	
	/**
	 * This method sets the date of the tour 
	 * to the input String
	 * @param dates Tour's date
	 */
	public void setDates(String dates) { this.dates = dates;}
	
	/**
	 * This method returns the dates
	 * of the tour as String
	 * @return Tour's date
	 */
	public String getDates() { return this.dates;}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * detailed information of the tour
	 * @return detailed information of the tour
	 */
	public StringBuilder getDetailTourInfo(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + " " + this.name + "\n");
		tourBuilder.append("Attractions:\n" + this.attraction + "\n");
		tourBuilder.append("We have confirmed tour on " + this.dates + "\n");
		tourBuilder.append("Fee: Weekday " + this.weekDayPrice + " / Weekend " + this.weekEndPrice);
		//tourBuilder.append("Do you want to book this one?");
		return tourBuilder;
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id and name) of the tour
	 * @return basic information (id and name) of the tour
	 */
	public StringBuilder getBasicTourInfo(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\n");
		return tourBuilder;
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * detailed information of a list of tours
	 * @param tourList List of tours
	 * @return tourBuilder a list of tours
	 */
	public static StringBuilder getBasicTourListInfo(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfo());
		}
		return tourBuilder;
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id and name) and
	 * the weekday price of the tour
	 * @return basic information (id and name) and the weekday price of the tour
	 */
	public StringBuilder getBasicTourInfoWithPrice(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\t$" + this.weekDayPrice + "\n");
		return tourBuilder;
	}
	
	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id, name, date) and
	 * the normal and discounted prices (weekday) of the tour
	 * @param dayFlag True for weekday, False for weekend
	 * @param date The date of the tour
	 * @return basic information, and the normal and discounted prices of the tour
	 */
	public StringBuilder getDiscountTourInfo(Boolean dayFlag, String date){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append("There is a special tour offering at a discount of 50%\n");
		tourBuilder.append(this.getBasicTourInfo() + "on " + date + "\n");
		if (dayFlag) tourBuilder.append(this.getweekDayPrice() + "->" + this.getweekDayPrice()/2);
		else tourBuilder.append(this.getweekEndPrice() + "->" + this.getweekEndPrice()/2);
		tourBuilder.append(" >.<\n");
		tourBuilder.append("Each client can reserve 2 seats at most");
		return tourBuilder;
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id and name) and
	 * the weekday price of a list of tours
	 * @param tourList List of tours
	 * @return basic information (id and name) and the weekday price of a list of tours
	 */
	public static StringBuilder getBasicTourListInfoWithPrice(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfoWithPrice());
		}
		return tourBuilder;
	}

	/**
	 * This method returns a message reporting the
	 * number of related tours regarding the keyword,
	 * which acts as a filter of the tour search
	 * @param tourList A list of tour
	 * @param keyword Attribute keyword
	 * @return a topic sentence
	 */
	public static String getTopicSentence(List<Tour> tourList, Keyword keyword){
		switch (keyword){
			case DATE:
				return "There are " + tourList.size() + " tours available on that day\n";
			case ATTRACTION:
				return "There are " + tourList.size() + " tours related\n";
			case PREFERENCE:
				return "There are " + tourList.size() + " tours available for your preferences\n";
			default:
				return null;
		}
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id and name) of
	 * a list of tours filtered by the input keyword
	 * @param tourList List of tours
	 * @param keyword Attribute keyword
	 * @return basic information (id and name) of a list of tours filtered by the input keyword
	 */
	public static StringBuilder getBasicTourInfoByKeyword(List<Tour> tourList, Keyword keyword){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(getTopicSentence(tourList, keyword));
		return tourBuilder.append(getBasicTourListInfo(tourList));
	}

	/**
	 * This method returns a StringBuilder
	 * which consists of the formatted message of the 
	 * basic information (id and name) and weekday price
	 * of a list of tours filtered by the input keyword
	 * and sorted by the tour price
	 * @param tourList A list of tours
	 * @param keyword Attribute keyword
	 * @return tourBuilder basic information and weekday price of a list of tours filtered by the input keyword
	 */
	public static StringBuilder getBasicTourInfoSortByPrice(List<Tour> tourList, Keyword keyword){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(getTopicSentence(tourList, keyword));
		tourBuilder.append("Sorted by Price\n" );

		sortTourListByPrice(tourList);
		return tourBuilder.append(getBasicTourListInfoWithPrice(tourList));
	}

	/**
	 * This method sort a list of tours
	 * accroding to the tours' weekday prices
	 * in ascending order
	 * @param tourList A list of tours
	 */
	public static void sortTourListByPrice(List<Tour> tourList){
		Tour temp;
		if (tourList.size() == 1) return;
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

	/**
 	 * This is a Date class which stores the information
 	 * about a date, the day of the week it is,
 	 * and a boolean value deciding whether the
 	 * date is at weekend or not
 	 */
	class Date{
	private boolean isWeekend;
	private int date;
	private int day;
	
	/**
	 * This is the constructor of the 
	 * Date class with initial values
	 */
	public Date() {
		isWeekend = false;
		day = 0;
		date = 0;
	}

	/**
	 * This method sets isWeekend of this Date 
	 * to the input boolean value
	 * @param isWeekend is weekend or not
	 */
	public void setIsWeekend(boolean isWeekend) { this.isWeekend = isWeekend;}
	
	/**
	 * This method returns the boolean value
	 * of whether the date is at weekend
	 * @return is weekend or not
	 */
	public boolean getIsWeekend() { return this.isWeekend;}
	
	/**
	 * This method sets the day of a week
	 * this Date belongs to according to
	 * the input integer
	 * @param day day of a week
	 */
	public void setDay(int day) { this.day = day;}
	
	/**
	 * This method returns the day of a
	 * week this Date belongs to as an integer
	 * @return day of a week
	 */
	public int getDay() { return this.day;}
	
	/**
	 * This method sets the date in this
	 * Date object to the input integer
	 * @param date date
	 */
	public void setDate(int date) { this.date = date;}
	
	/**
	 * This method returns the date
	 * in this Date object as an integer
	 * @return date
	 */
	public int getDate() { return this.date;}

}
