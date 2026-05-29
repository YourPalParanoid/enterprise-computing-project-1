/*
Name: Christopher Albear
Course: CNT 4714 Summer 2026
Assignment title: Project 1 – Multi-threaded programming in Java
Date: June 7, 2026
Class: Station
*/
package org.example;

import java.util.*;

// Thread
public class Station implements Runnable{
    // station attributes
    public int workload;
    public int number;

    public int rightConveyer;
    public int leftConveyer;

    public Station(int workload, int number, int numStations){
        this.workload = workload;
        this.number = number;
        this.rightConveyer = number;
        this.leftConveyer = number % numStations;
    }

    private void sleep(){

    }

    public void work(){

    }

    public void run(){

    }
}
