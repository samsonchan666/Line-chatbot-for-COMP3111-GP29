package com.example.bot.spring;

import java.util.Calendar;

public class Booking {
	private String ID;
	private Tour tour;
	private Calendar date;
	private TourGuide tourGuide;
	private String hotel;
	private int capacity;
	private int miniCustomer;

	public Booking(
			String ID,
			Tour tour,
			Calendar date,
			TourGuide tourGuide,
			String hotel,
			int capacity,
			int miniCustomer) {
		this.ID = ID;
		this.tour = tour;
		this.date = date;
		this.tourGuide = tourGuide;
		this.hotel = hotel;
		this.capacity = capacity;
		this.miniCustomer = miniCustomer;
	}
	
	public void setID(String ID) { this.ID = ID;}
	public String getID() { return this.ID;}
	
	public void setTour(Tour tour) { this.tour = tour;}
	public Tour getTour() { return this.tour;}

	public void setDate(Calendar date) { this.date = date;}
	public Calendar getDate() {return this.date;}
	public static int dateToday(String dateString){
		String[] dateArr = dateString.split("/");
		Calendar date = Calendar.getInstance();
		date.set(Integer.parseInt(dateArr[2]),Integer.parseInt(dateArr[1])-1,Integer.parseInt(dateArr[0]));
		return date.get(Calendar.DAY_OF_WEEK);
	}

	public void setTourGuide(TourGuide tourGuide) { this.tourGuide = tourGuide;}
	public TourGuide getTourGuide() { return this.tourGuide;}
	
	public void setHotel(String hotel) { this.hotel = hotel;}
	public String getHotel() { return this.hotel;}
	
	public void setCapacity(int capacity) { this.capacity = capacity;}
	public int getCapacity() { return this.capacity;}
	
	public void setMiniCustomer(int miniCustomer) { this.miniCustomer = miniCustomer;}
	public int getMiniCustomer() { return this.miniCustomer;}
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
