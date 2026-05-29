/*
Name: Christopher Albear
Course: CNT 4714 Summer 2026
Assignment title: Project 1 – Multi-threaded programming in Java
Date: June 7, 2026
Class: Conveyor
*/
package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conveyor {
    public int number;
    private Lock lock = new ReentrantLock();
    // lock

    public Conveyor(int number){
        this.number = number;
    }

    public boolean lockConveyor(){
        return lock.tryLock();
    }
    public void unlockConveyor(){
        lock.unlock();
    }
}
