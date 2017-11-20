package com.example.bot.spring;

/**
 * Created by Samson on 18/11/2017.
 * 
 * This is the observer interface for 
 * the observer pattern implementation
 * This interface is implemented by the 
 * Cutomer class while the subject is 
 * implemented by the Booking class
 * 
 * @see Customer.java
 */
public interface Observer {
    void update();
}
