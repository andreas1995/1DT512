/*
 * File:	RR.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2017
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class RR{

	// The list of processes to be scheduled
	public ArrayList<Process> processes;
	public ArrayList<Process> waitList ;
	// the quantum time - which indicates the maximum allowable time a process can run once it is scheduled
	int tq;
  
   private Process pro;
   private int ct = 0;
   private StringBuilder printout;
	// keeps track of which process should be executed next
	public Queue<Process> schedulingQueue;
	
	// List to keep all data necessary for an rudimentary Gantt Chart

	

	// Class constructor
	public RR(ArrayList<Process> processes, int tq) {
		schedulingQueue = new LinkedList<Process>();
		this.processes = processes;
		this.tq = tq;
		waitList = new ArrayList<Process>();
		pull();
		printout = new StringBuilder();
		
	}
	

	public void run() {		
		
		boolean loop = true;
		
		//start 
		while (loop)
		{			
			//adding processes if their time has come 
			Iterator<Process> it = waitList.iterator();
			checkForArrival(it);
			
			if (pro != null && pro.getRemainingBurstTime() > 0){
				schedulingQueue.add(pro);
			}
				
			
			pro = schedulingQueue.poll();
			
			
			if (pro == null)
			{
				//if process is null that means we ill work with the same process
				ct++;
				printout.append("  \t| "+ (ct-1)+" - "+ ct+"\t | ");
				
			}
			else
			{
			
			//else we go to the next one
			int bt = pro.getRemainingBurstTime();
			int aTime = bTime(pro.getRemainingBurstTime());
			ct += aTime;
			
			pro.setRemainingBurstTime(bt - aTime);
			printout.append("Pr. ID: "+pro.getProcessId()+"\t | " +(ct - aTime)+" - " + ct+"\t | ");
			
			if (0 >= pro.getRemainingBurstTime())
			{	
				complete(pro,ct);
				pro = null;
			}
			
			}
			
			loop = goAround();
		}
		
		
		printProcesses();
		printGanttChart();
	}
	
	
	public void checkForArrival(Iterator<Process> it){

		while(it.hasNext())
		{
			Process proc = it.next();
			if (ct >= proc.getArrivalTime()) 
			{
				schedulingQueue.add(proc);
				it.remove();
			}
		}
	}
	
	
	public int bTime(int i){
		
		if (i > tq)
			i = tq;
		
		return i;
		
	}
	
	
	
	public boolean goAround(){
		//loop instructions. Check if we have more elements to work with.
		if(pro != null || waitList.isEmpty() == false || schedulingQueue.isEmpty() == false )
			return true;
		else
			return false;
	}
	
	
	
	public void pull(){
		//first we sort the list by arrival time then we add every process to the waitList
		processes.sort((p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());
		for(Process p1 : processes)
			waitList.add(p1);
		
		pro = null;
	}
	
	
	
	public void complete(Process p , int counter){
		//calculate tat wt when a process is completed
		p.setCompletedTime(counter);
		p.setTurnaroundTime(p.getCompletedTime() - p.getArrivalTime());
		p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
	}
	
	

	public void printProcesses() {
		//just sysout everything in the processes list
		System.out.println("Process List");
		System.out.print("Pr. ID\t| AT\t| BT\t| CT\t| TAT\t| WT\n");
		for(Process p : processes){
			System.out.println(
					p.getProcessId()+" \t"+
					p.getArrivalTime()+" \t"+
					p.getBurstTime()+" \t"+
					p.getCompletedTime()+" \t"+
					p.getTurnaroundTime()+" \t"+
					p.getWaitingTime()
					);
		}
	
	}

	public void printGanttChart(){
		//if it is only like | without a number that means it uses the process id that was printed out last.  
		System.out.println("Gantts Chart");
		System.out.println(printout.toString());
		
	}
}