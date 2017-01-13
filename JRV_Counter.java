package jrv_counter;

/*
 * JRV_Counter by James R. Vaughan
 * Exercise in multi-threading.
 * This program uses 10 threads to increment two ints from 1 to 5000
 * As you can see from the sample output, all the threads get allowed into the action.
 * However, this is usually not the case with most runs.
 * Which threads get to increment n1 and n2 varies randomly from run to run.
 * The previous time I ran this, threads 7, 8, and 9 didn't get to increment
 * n1 and n2.
 */

import java.lang.*;
import java.io.*;
import java.util.*;



/**
 *
 * @author James
 */
public class JRV_Counter {

    private int count1 = 0, count2 = 0;
    private int max_count = 5000;
    private int number_of_threads = 10;
    private int threads_quit;

	public JRV_Counter(){
		threads_quit = 0;
		startThreads();
	}
	
	public synchronized int getNextNumber1(int _number){
		int temp;
		if(count1 < max_count) 
		{  	temp = count1;
			count1 = count1 + 1;
                        System.out.println("Thread ID " + _number + " N1 " + temp);
		        return(temp);
		}
		else return(-1);
	}
	
	public synchronized int getNextNumber2(int _number){
		int temp;
		if(count2 < max_count) 
		{  	temp = count2;
			count2 = count2 + 1;
                        System.out.println("Thread ID " + _number + " N2 " + temp);
		        return(temp);
		}
		else return(-1);
	}

	public synchronized int threads_quit_increase() {
		threads_quit = threads_quit + 1;
		if (threads_quit < number_of_threads) return (0);
		else return (-1); 
	}
	
	public void startThreads(){
		int i = 0;
		for(i = 0; i < number_of_threads; i++){
			CounterThread ct = new CounterThread(this, i);
			ct.start();
		}
	}
	
	public static void main(String args[]){
		JRV_Counter a = new JRV_Counter();
	}
	
	class CounterThread extends Thread
	{
		JRV_Counter parent;
		int number;
		
		public CounterThread(JRV_Counter parent, int number){
			this.parent = parent;
			this.number = number;
		}
		
		public void run(){
			int n1;
			int n2;
			
			synchronized(parent)
			{
				n1 = parent.getNextNumber1(number); 
				n2 = parent.getNextNumber2(number);
			}
			
			while (n1 != -1 && n2 != -1 && n1 == n2) {

				synchronized(parent)
				{
					n1=parent.getNextNumber1(number);
					n2=parent.getNextNumber2(number);
				}
			}
			if (n1 != n2)
				System.out.println("Two counters are discrepant!");

			System.out.println("Thread ID " + number + " is quitting!");
			if (threads_quit_increase() == -1)
				System.out.println("All threads have quitted. Done!");
		}
	}
    
}




