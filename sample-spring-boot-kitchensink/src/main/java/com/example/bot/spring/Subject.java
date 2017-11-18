package com.example.bot.spring;

/**
 * Created by Samson on 18/11/2017.
 */
public interface Subject {
    int getState();
    void setState(int state) ;
    void attach(Observer observer);
    void notifyAllObservers();
}
