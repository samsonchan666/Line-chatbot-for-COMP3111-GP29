package com.example.bot.spring;

/**
 * Subject interface
 */
public interface Subject {
    int getState();
    void setState(int state) ;
    void attach(Observer observer);
    void notifyAllObservers();
}
