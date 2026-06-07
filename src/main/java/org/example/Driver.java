/*
Name: Christopher Albear
Course: CNT 4714 Summer 2026
Assignment title: Project 1 – Multi-threaded programming in Java
Date: June 7, 2026
Class: Driver
*/
package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Driver {
    static int MAX = 10;

    static void main(String[] args) {
        try {
            System.out.print("Summer 2026 - Project 1 - Package Management Facility Simulator\n\n");
            System.out.print("**********Package Management Facility Simulation Begins!**********\n\n");

            // create MAX size threadpool
            ExecutorService pool = Executors.newFixedThreadPool(MAX);

            // initialize stations and Conveyors
            Scanner in = new Scanner(new File("org/example/" + args[0]));

            // read first line of config for # of stations
            int numStations = in.nextInt();

            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(numStations);

            // create an array of stations (threads)
            Station[] stationArray = new Station[numStations];
            // create an array of Conveyors
            Conveyor[] ConveyorArray = new Conveyor[numStations];

            // populate array with new stations with their assigned workloads
            for(int i = 0; i < numStations; i++) {
                ConveyorArray[i] = new Conveyor(i);
            }

            for(int i = 0; i < numStations; i++) {
                stationArray[i] = new Station(in.nextInt(), i, ConveyorArray, numStations, startSignal, doneSignal);
            }

            //close file
            in.close();

            // print initial arrangement
            System.out.print("\tThe parameters for this run are:\n\n");

            System.out.print("\tThere are " + numStations + " routing stations in this simulation run\n\n");
            for(int i = 0; i < numStations; i++) {
                System.out.print("\tRouting station S" + i + " has total workload of " + stationArray[i].workload + " package groups\n");
            }

            System.out.print("\n");

            for(int i = 0; i < numStations; i++)
            {
                System.out.print("\t% % % % ROUTING STATION S" + i + " Initializing Conveyors" + " % % % %\n");
                System.out.print("\t    Routing Station S" + i + ": Input conveyor assigned to conveyor number C" + stationArray[i].rightConveyor.number + "\n");
                System.out.print("\t    Routing Station S" + i + ": Output conveyor assigned to conveyor number C" + stationArray[i].leftConveyor.number + "\n");
                System.out.print("\t    Routing Station S" + i + ": Workload set. Station S" + i + ": has a total of " + stationArray[i].workload + " package groups to move\n");
                System.out.print("\t% % % % ROUTING STATION S" + i + ": Awaiting Signal From Control To Begin Operations" + " % % % %\n\n");

            }

            // START THE THREADS
            for(int i = 0; i < numStations; i++)
            {
                pool.execute(stationArray[i]);
            }

            try {
                startSignal.countDown();
                doneSignal.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            // shutdown sequence

            //TODO: print "signal recieved from control, station online
            pool.shutdown();


        } catch(FileNotFoundException e) {
                System.out.print("\tFile " + "\"" + args[0] + "\"" + " not found!\n");
        }
        // System.out.print("**********Package Management Facility Simulation Ends!**********\n");
    }
}
