/*
Name: Christopher Albear
Course: CNT 4714 Summer 2026
Assignment title: Project 1 – Multi-threaded programming in Java
Date: June 7, 2026
Class: Station
*/
package org.example;

import java.util.*;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

// Thread
public class Station implements Runnable{
    // station attributes
    public int workload;
    public int number;

    public Conveyor rightConveyor;
    public Conveyor leftConveyor;

    // create cyclic barrier with number of threads (stations)
    public static CyclicBarrier cyclicBarrier;

    public Random gen = new Random();

    private final CountDownLatch doneSignal;
    private final CountDownLatch startSignal;

    public Station(int workload, int number, Conveyor[] conveyorArr, int numStations, CountDownLatch startSignal, CountDownLatch doneSignal){
        this.workload = workload;
        this.number = number;
        this.rightConveyor = conveyorArr[number];

        this.startSignal = startSignal;
        this.doneSignal = doneSignal;

        if(number == 0)
            this.leftConveyor = conveyorArr[numStations - 1];
        else
            this.leftConveyor = conveyorArr[number - 1];

        this.cyclicBarrier = new CyclicBarrier(numStations);
    }

    private void sleep(){
        try {
            Thread.sleep(gen.nextInt(500));
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void work(){
        System.out.print(" \n ** Routing station S" + number + ": **CURRENTLY HARD AT WORK MOVING PACKAGES **\n");
        sleep();
    }

    public void run(){
        // each thread will wait for the other threads to wait at the barrier before entering critical phase

        try{
            startSignal.await();
            System.out.print("Routing station S" + number + ": Signal received from control - S" + number + " Online\n");


            cyclicBarrier.await();
            doneSignal.countDown();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(" \nRouting station S" + number + " entering lock acquisition phase\n");

        // loop for entire workload
        for(int i = 1; i <= workload; i++)
        {
            boolean hasBothLocks = false;
            while(!hasBothLocks)
            {
                if(rightConveyor.lockConveyor())
                {
                    System.out.print(" Routing station S" + number + ": currently holds lock on input conveyor C" + rightConveyor.number + "\n");
                    if(leftConveyor.lockConveyor())
                    {
                        System.out.print(" Routing station S" + number + ": currently holds lock on output conveyor C" + leftConveyor.number + "\n");
                        hasBothLocks = true;
                    }
                    else
                    {
                        System.out.print(" Routing station S" + number + ": UNABLE TO ACQUIRE LOCK FOR OUTPUT CONVEYOR C" + leftConveyor.number + "\n");
                        System.out.print(" \nSYNCHRONIZATION ISSUE: Station S" + leftConveyor.number + " currently holds the lock on output conveyor C" + leftConveyor.number +
                                ". Station S" + number + " releasing lock on input conveyor C" + rightConveyor.number + "\n");
                        rightConveyor.unlockConveyor();
                        sleep();
                    }
                }
            }

            System.out.print("\n******Routing station S" + number + ": currently holds locks on both input conveyor C" + rightConveyor.number + " and output conveyor C" + leftConveyor.number + "\n");
            work();

            System.out.print(" \nRouting station S" + number + ": Package group completed - " + (workload - i) + " package groups remaining to move\n");
            System.out.print(" Routing station S" + number + ": Unlocks/releases input conveyor C" + rightConveyor.number + "\n");
            rightConveyor.unlockConveyor();
            System.out.print(" Routing station S" + number + ": Unlocks/releases output conveyor C" + leftConveyor.number + "\n");
            leftConveyor.unlockConveyor();
            // acquire input lock

            // acquire output lock
                // if cant, release input lock and sleep
                // if can, do work
                    // unlock both
        }
        System.out.print("\n\n ## Routing station S" + number + ": going offline - work completed! BYE! ##\n\n");
        Driver.coutndown -= 1;

    }
}
