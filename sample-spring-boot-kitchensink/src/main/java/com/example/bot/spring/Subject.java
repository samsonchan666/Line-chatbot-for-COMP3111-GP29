package com.example.bot.spring;

/**
 * Created by Samson on 18/11/2017.
 * 
 * This is the subject interface for 
 * the observer pattern implementation
 * This interface is implemented by the 
 * Booking class while the observer is 
 * implemented by the Customer class
 * 
 * @see Booking.java
 */
public interface Subject {
    int getState();
    void setState(int state) ;
    void attach(Observer observer);
    void notifyAllObservers();
}
