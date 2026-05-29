/*
Name: Christopher Albear
Course: CNT 4714 Summer 2026
Assignment title: Project 1 – Multi-threaded programming in Java
Date: June 7, 2026
Class: Conveyer
*/
package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conveyer {
    public int number;
    private Lock lock = new ReentrantLock();
    // lock

    public Conveyer(int number){
        this.number = number;
    }

    public boolean lockConveyer(){
        return lock.tryLock();
    }
    public void unlockConveyer(){
        lock.unlock();
    }
}
