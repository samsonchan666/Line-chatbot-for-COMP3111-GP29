package com.example.bot.spring;

import java.lang.*;
import java.util.List;

/**
 * <h>Tour details<h>
 * The Tour class store the information
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
	 * @param id
	 * @param name
	 * @param attraction
	 * @param duration
	 * @param weekDayPrice
	 * @param weekEndPrice
	 * @param dates
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
	 * This method set the id of the tour
	 * to the input String
	 * @param id
	 */
	public void setID(String id) { this.id = id;}
	
	/**
	 * This method return the id of the tour
	 * as String
	 * @return tour id
	 */
	public String getID() { return this.id;}
	
	/**
	 * This method set the name of the tour
	 * to the input String 
	 * @param name
	 */
	public void setName(String name) { this.name = name;}
	
	/**
	 * This method return the name of the
	 * tour as String
	 * @return tour name
	 */
	public String getName() { return this.name;}
	
	/**
	 * This method set the attraction of the 
	 * tour as the input String
	 * @param attraction
	 */
	public void setAttraction(String attraction) {
		this.attraction = attraction;
		}
	
	/**
	 * This method return the attraction of the
	 * tour as String
	 * @return tour attraction
	 */
	public String getAttraction() { return this.attraction; }
	
	
	/**
	 * This method set the duration of the
	 * tour as the input integer
	 * @param duration
	 */
	public void setDuration(int duration) { this.duration = duration;}
	
	/**
	 * This method return the duration of
	 * the tour as integer
	 * @return
	 */
	public int getDuration() { return this.duration;}
	
//	public int getPrice(Date date) {
//		if (date.getIsWeekend()) return weekEndPrice;
//		else return weekDayPrice;
//	}
	
	/**
	 * This method set the week day price
	 * of the tour to the input integer
	 * @param weekDayPrice
	 */
	public void setweekDayPrice(int price) { this.weekDayPrice = price;}
	
	/**
	 * This method return the week day
	 * price of the tour as a integer
	 * @return weekEndPrice
	 */
	public int getweekDayPrice() { return this.weekDayPrice;}
	
	/**
	 * This method set the week end price
	 * of the tour to the inout integer
	 * @param weekEndPrice
	 */
	public void setweekEndPrice(int price) { this.weekEndPrice = price;}
	
	/**
	 * This method return the week end
	 * price of the tour as a integer
	 * @return
	 */
	public int getweekEndPrice() { return this.weekEndPrice;}
	
	/**
	 * This method set the date of the tour 
	 * to the input String
	 * @param dates
	 */
	public void setDates(String dates) { this.dates = dates;}
	
	/**
	 * This method return the dates
	 * of the tour as String
	 * @return tour dates
	 */
	public String getDates() { return this.dates;}

	/**
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * detail informations of the tour
	 * @return tourBuilder
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
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * basic informations (id & name) of the tour
	 * @return tourBuilder
	 */
	public StringBuilder getBasicTourInfo(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\n");
		return tourBuilder;
	}

	/**
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * detail informations of a list of tours
	 * @param tourList
	 * @return tourBuilder
	 */
	public static StringBuilder getBasicTourListInfo(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfo());
		}
		return tourBuilder;
	}

	/**
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * basic informations (id & name) and 
	 * the week day price of the tour
	 * @return tourBuilder
	 */
	public StringBuilder getBasicTourInfoWithPrice(){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(this.id + "\t" + this.name + "\t$" + this.weekDayPrice + "\n");
		return tourBuilder;
	}

	/**
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * basic informations (id & name) and 
	 * the week day price of a list of tours
	 * @param tourList
	 * @return tourBuilder
	 */
	public static StringBuilder getBasicTourListInfoWithPrice(List<Tour> tourList){
		StringBuilder tourBuilder = new StringBuilder();
		for (Tour tour : tourList){
			tourBuilder.append(tour.getBasicTourInfoWithPrice());
		}
		return tourBuilder;
	}

	/**
	 * This method return a message reporting the
	 * number of related tours regarding the keywork,
	 * which act as a filter of the tour search
	 * @param tourList
	 * @param keyword
	 * @return Nothing
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
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * basic informations (id & name) of
	 * a list of tour filtered by the input keyword
	 * @param tourList
	 * @param keyword
	 * @return tourBuilder
	 */
	public static StringBuilder getBasicTourInfoByKeyword(List<Tour> tourList, Keyword keyword){
		StringBuilder tourBuilder = new StringBuilder();
		tourBuilder.append(getTopicSentence(tourList, keyword));
		return tourBuilder.append(getBasicTourListInfo(tourList));
	}

	/**
	 * This method  return a StringBuilder
	 * which consist of the formatted message of the 
	 * basic informations (id & name) and week day price
	 * of a list of tour filtered by the input keyword
	 * and sorted by the tour price
	 * @param tourList
	 * @param keyword
	 * @return tourBuilder
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
	 * accroding to the tours' week day prices
	 * in ascending order
	 * @param tourList
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
 * This is a Date class which store information
 * about a date, the day of week the date is,
 * and a boolean value deciding whether the
 * date is at weekend or not
 */
	class Date{
	private boolean isWeekend;
	private int date;
	private int day;
	
	/**
	 * This is the constructor the 
	 * Date class with defaul initial values
	 */
	public Date() {
		isWeekend = false;
		day = 0;
		date = 0;
	}

	/**
	 * This method change isWeekend of the date 
	 * to the input boolean value
	 * @param isWeekend
	 */
	public void setIsWeekend(boolean isWeekend) { this.isWeekend = isWeekend;}
	
	/**
	 * This method return the boolean value
	 * of whether the date is at weekend
	 * @return isWeekEnd
	 */
	public boolean getIsWeekend() { return this.isWeekend;}
	
	/**
	 * This method set the day of a week
	 * the date belongs to according to
	 * the inout integer
	 * @param day
	 */
	public void setDay(int day) { this.day = day;}
	
	/**
	 * This method return the day of a
	 * week the date belongsto as a integer
	 * @return day
	 */
	public int getDay() { return this.day;}
	
	/**
	 * This method set the date in the
	 * Date class to the input integer
	 * @param date
	 */
	public void setDate(int date) { this.date = date;}
	
	/**
	 * This method return the date
	 * in the Date class as a integer
	 * @return date
	 */
	public int getDate() { return this.date;}

}