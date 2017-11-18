package com.example.bot.spring;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class Booking implements Subject{
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
	
	public void setID(String ID) { this.ID = ID;}
	public String getID() { return this.ID;}
	
	public void setTour(Tour tour) { this.tour = tour;}
	public Tour getTour() { return this.tour;}

	public void setDate(Calendar date) { this.date = date;}
	public void setDateString(String dateString) { this.date = StringToDate(dateString);}
	public Calendar getDate() {return this.date;}
	public int dateToDay(String dateString){
		Calendar date = StringToDate(dateString);
		return date.get(Calendar.DAY_OF_WEEK);
	}
	public Calendar StringToDate(String dateString) {
		String[] dateArr = dateString.split("/");
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[0]));
		return date;
	}
	public String dateToString() {
		SimpleDateFormat format1 = new SimpleDateFormat("d/MM/yyyy");
		String dateString = format1.format(date.getTime());
		return dateString;
	}

	public void setTourGuide(TourGuide tourGuide) { this.tourGuide = tourGuide;}
	public TourGuide getTourGuide() { return this.tourGuide;}
	
	public void setHotel(String hotel) { this.hotel = hotel;}
	public String getHotel() { return this.hotel;}
	
	public void setCapacity(int capacity) { this.capacity = capacity;}
	public int getCapacity() { return this.capacity;}
	
	public void setMiniCustomer(int miniCustomer) { this.miniCustomer = miniCustomer;}
	public int getMiniCustomer() { return this.miniCustomer;}
	
	public void setCurrentCustomer(int currentCustomer) { this.currentCustomer = currentCustomer;}
	public int getCurrentCustomer() { return this.currentCustomer;}

	public int getState(){
		return this.state;
	}
	public void setState(int state) {
		this.state = state;
		notifyAllObservers();
	}
	public void attach(Observer observer){
		observers.add(observer);
	}
	public void notifyAllObservers(){
		for (Observer observer : observers) {
			observer.update();
	}
} 


class TourGuide {
	private String name;
	private String lineAcc;
	
	public TourGuide(
			String name,
			String lineAcc) {
		this.name = name;
		this.lineAcc = lineAcc;
	}
	
	public void setName(String name) { this.name = name;}
	public String getName() { return this.name;}
	
	public void setLineAcc(String lineAcc) { this.lineAcc = lineAcc;}
}