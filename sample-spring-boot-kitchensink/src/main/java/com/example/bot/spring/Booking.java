package com.example.bot.spring;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;


/**
 * <h1>Booking details</h1>
 * The Booking class stores the details of the booking made 
 * during the conversation of the client and the bot
 */
public class Booking implements Subject {
	private String ID;
	private Tour tour;
	private Calendar date;
	private TourGuide tourGuide;
	private String hotel;
	private int capacity;
	private int miniCustomer;
	private int currentCustomer;
	private List<Observer> observers = new ArrayList<Observer>();
	private int state = 0;

	/**
	 * This is the constructor of the Booking class
	 * with all the information needed for a booking 
	 * stored when constructed
	 * 
		 * @param ID 				Booking's ID
		 * @param tour 				Booking's tour
		 * @param date 				Booking's date
		 * @param hotel 				Booking's hotel
		 * @param capacity 			Booking's capcaity
		 * @param miniCustomer 		Booking's minimum customer
		 * @param currentCustomer 	Booking's current customer
	 */
	public Booking(
			String ID,
			Tour tour,
			Calendar date,
			String hotel,
			int capacity,
			int miniCustomer,
			int currentCustomer) {
		this.ID = ID;
		this.tour = tour;
		this.date = date;
		this.tourGuide = new TourGuide("-1", "-1");
		this.hotel = hotel;
		this.capacity = capacity;
		this.miniCustomer = miniCustomer;
		this.currentCustomer = currentCustomer;
	}
	
	/**
	 * This method sets the ID of the Booking to the input String
	 * @param ID Booking's ID
	 */
	public void setID(String ID) {
		this.ID = ID;
	}
	
	/**
	 * This method returns the ID of the Booking, which is a String
	 * @return ID of the Booking
	 */
	public String getID() {
		return this.ID;
	}

	/**
	 * This method sets the Tour of the Booking to the input Tour
	 * @param tour Booking's tour
	 */
	public void setTour(Tour tour) {
		this.tour = tour;
	}

	/**
	 * This method returns the Tour of the Booking, which is a Tour class
	 * @return Tour of the Booking
	 */
	public Tour getTour() {
		return this.tour;
	}

	/**
	 * This method sets the date of the Booking to the input Calendar class
	 * @param date Booking's date
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * This method sets the date of the Booking to the 
	 * date specified in the input String
	 * @param dateString date of booking
	 */
	public void setDateString(String dateString) {
		this.date = StringToDate(dateString);
	}

	/**
	 * This method returns the date of teh Booking, which is a Calendar class
	 * @return Booking date
	 */
	public Calendar getDate() {
		return this.date;
	}

	/**
	 * This method converts the date stored in the object into a day of week
	 * @return day of week
	 */
	public int dateToDay() {
		//Calendar date = StringToDate(dateString);
		return date.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * This method converts a date specified in a String 
	 * into an actual date stored in a object, 
	 * which is a Calandar class
	 * @param dateString date String
	 * @return date as a Calander class
	 */
	public Calendar StringToDate(String dateString) {
		String[] dateArr = dateString.split("/");
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[1]) - 1, Integer.parseInt(dateArr[0]));
		return date;
	}

	/**
	 * This method converts a date which is 
	 * of Calander class into a String
	 * @return dataString
	 */
	public String dateToString() {
		SimpleDateFormat format1 = new SimpleDateFormat("d/MM/yyyy");
		String dateString = format1.format(date.getTime());
		return dateString;
	}

	/**
	 * This method sets the tourGuide stored in the 
	 * Booking object to the input TourGuide object
	 * @param tourGuide tour guide
	 */
	public void setTourGuide(TourGuide tourGuide) {
		this.tourGuide = tourGuide;
	}

	/**
	 * This method returns the Tourguide object 
	 * stored in the Booking object
	 * @return tourGuide
	 */
	public TourGuide getTourGuide() {
		return this.tourGuide;
	}

	/**
	 * This method sets the hotel in the 
	 * Booking object to the input String
	 * @param hotel Booking's hotel
	 */
	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	/**
	 * This method returns the hotel object as a String 
	 * @return hotel
	 */
	public String getHotel() {
		return this.hotel;
	}

	/**
	 * This method sets the maximum capacity of
	 * the Booking to the input integer
	 * @param capacity Booking's capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * This method returns the the maximum capacity
	 * of the Booking as a integer
	 * @return Booking's capacity
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * This method sets the minimum number
	 * of customer required for the tour
	 * to the input integer 
	 * @param miniCustomer Booking's minimum customer
	 */
	public void setMiniCustomer(int miniCustomer) {
		this.miniCustomer = miniCustomer;
	}

	/**
	 * This method returns the minimun number
	 * of customer required for the tour 
	 * as a integer
	 * @return Booking's minimum customer
	 */
	public int getMiniCustomer() {
		return this.miniCustomer;
	}

	/**
	 * This method sets the number of 
	 * current customer to the input integer
	 * @param currentCustomer  Booking's current customer
	 */
	public void setCurrentCustomer(int currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	/**
	 * This method returns the number of
	 * current customer as a integer
	 * @return  Booking's current customer
	 */
	public int getCurrentCustomer() {
		return this.currentCustomer;
	}

	/**
	 * This method increments the current number
	 * of customer stored in the Booking object by 
	 * the value of the input integer
	 * @param number the amount of increment
	 */
	public void addCurrentCustomer(int number) { this.currentCustomer+=number;}

	/**
	 * This method returns the state of the
	 * Booking, which is indicated by a integer
	 * @return state of booking
	 */
	public int getState() {
		return this.state;
	}

	/**
	 * This method sets the state of the Booking,
	 * which is indicated by a integer value,
	 * to the input integer. After that, 
	 * notify the observers who have subscribed
	 * the Booking
	 * @param state state of booking
	 */
	public void setState(int state) {
		this.state = state;
		notifyAllObservers();
	}

	/**
	 * This method attaches the input observer
	 * to the Booking object
	 * @param observer a customer observer
	 */
	public void attach(Observer observer) {
		observers.add(observer);
	}

	/**
	 * This method notifies the observers
	 * which are attached to the Booking object
	 * of the changes (of state)
	 */
	public void notifyAllObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}
}

/**
 * This is the TourGuide class which can be 
 * found in the Booking class. It stores the information 
 * of the tour guides, including their name and
 * their Line Account.
 *
 */
class TourGuide {
	private String name;
	private String lineAcc;
	
	/**
	 * This is the constructor of the TourGuide
	 * with both the name and Line Account stored
	 * when constructed
	 * @param name name of tour guide
	 * @param lineAcc line account of tour guide
	 */
	public TourGuide(
			String name,
			String lineAcc) {
		this.name = name;
		this.lineAcc = lineAcc;
	}
	
	/**
	 * This method sets the name of the
	 * tour guide to the input String
	 * @param name name of the tour guide
	 */
	public void setName(String name) { this.name = name;}
	
	/**
	 * This method returns the name of the 
	 * tour guide as a String
	 * @return name of a TourGuide object
	 */
	public String getName() { return this.name;}
	
	/**
	 * This method set the Line Account
	 * of the tour guide to the input String
	 * @param lineAcc line account of the tour guide
	 */
	public void setLineAcc(String lineAcc) { this.lineAcc = lineAcc;}
}