package ac222qf;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Philosopher implements Runnable {
	
	private int id;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	DiningPhilosopher dt = new DiningPhilosopher();
	private Random randomGenerator = new Random();
	 AtomicBoolean isPhilospherDone = new AtomicBoolean(false);
	private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;
	
	
	
  
	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;
	
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
		
		
		
		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution 
		 */
	
		randomGenerator.setSeed(id+seed);
		
		
	
	}
	public int getId() {
		return id;
	}

	public double getAverageThinkingTime() {
		//just divides the total time with the loops and finds the average
		return thinkingTime / numberOfThinkingTurns;
	}

	public double getAverageEatingTime() {
	
		return eatingTime / numberOfEatingTurns;
	}

	public double getAverageHungryTime() {
		
		return hungryTime / numberOfHungryTurns;
	}
	
	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}
	
	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}
	
	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}

	public double getTotalThinkingTime() {
		return thinkingTime;
	}

	public double getTotalEatingTime() {
		return eatingTime;
	}

	public double getTotalHungryTime() {
		return hungryTime;
	}
	
	
	
   public boolean think(){
	try {
		//put the thread to sleep. Represent the thinking time
		if(dt.DEBUG)
		System.out.println("Philosopher "+id+" is thinking.");
		
		int thinkslp = randomGenerator.nextInt(1000)  ;
		Thread.sleep(thinkslp);
		
		numberOfThinkingTurns ++;
		
		thinkingTime += thinkslp;
		
		
		
	} catch (InterruptedException e) {
		
	}
	return true;
	}
   
   
   
   
     public boolean hungry(){
    	 
		long start = System.currentTimeMillis();
		if(dt.DEBUG)
			System.out.println("Philosopher "+id+" is hungry.");
	
		
			boolean pass = false;
			/*this loop takes a boolean pass = false to continue. 
			 * What this loop does is finding the philosopher the left chopstick 
			 * if it is able to find it then it searches for the right one. If he is not able t
			 *to find the right one. he releases the one and repeats the loop. If he finds it the loop breaks
			 *and the philosopher goes to eat*/
		
			while(!pass){
				//check if left is locked
					if(!leftChopStick.isItLocked()){
				
						leftChopStick.Lock();
						
						
						// if left is not locked check if right is locked  
						if(!rightChopStick.isItLocked()){
				
							 //if yes lock it and go to eat
					rightChopStick.Lock();
					if(dt.DEBUG){
					System.out.println("Philosopher "+id+" picked up the right chopstick "+rightChopStick.getId()); 
					System.out.println("Philosopher "+id+" picked up the left chopstick "+leftChopStick.getId());
					}
							 pass = true;
							 continue;
						 }
						else
						{
							//if no unlock the left one and continue searching
							 leftChopStick.UnLock();
						
					}
				}
				
				

				 //it maybe take some time to find the chopsticks so check the philosophers are done
				   if(isPhilospherDone.get())
					   return true;	
	          
	          
	         	     

			}		
			 //caclulate how much time it took for the philosopher find the chopsticks
			//basically hungry time
		long end = System.currentTimeMillis();
		
		long time = end - start;

		numberOfHungryTurns ++;
		hungryTime = hungryTime + time;
		//return eat so it goes to the eat method
		 return eat();
	
		}
     
   
        public boolean eat(){
		try {
			
			//put the thread to sleep for some random time. The sleeping time represents the eating time
			if(dt.DEBUG)
			System.out.println("Philosopher "+id+" is eating.");
			
			int eatslp = randomGenerator.nextInt(1000) ;
			
			Thread.sleep(eatslp);
			
			//unlockes the chopsticks when is done eating 
			leftChopStick.UnLock();
			rightChopStick.UnLock();
			
			if(dt.DEBUG){
			System.out.println("Philosopher "+id+" released the left chopstick "+leftChopStick.getId());	
			System.out.println("Philosopher "+id+" released the right chopstick "+rightChopStick.getId());	
			}
			
			numberOfEatingTurns ++;
			eatingTime += eatslp;
			
		
			
		} catch (InterruptedException e) {
			
		}
		return true;
		}
   

	@Override
	public void run() {
		
		
		while(!isPhilospherDone.get()){
		  //if the philosophers are done it would break the loop
			// the loop starts with the thinking stage and then it goes to the hungry stage
			//the hungry method is responsible for making the philosopher eat
			think();
			hungry();
			
		
		}
		
		
	}
}