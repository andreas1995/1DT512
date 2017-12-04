package ac222qf;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DiningPhilosopher {
	
	/*
	 * Controls whether logs should be shown on the console or not. 
	 * Logs should print events such as: state of the philosopher, and state of the chopstick
	 * 		for example: philosopher # is eating; 
	 * 		philosopher # picked up his left chopstick (chopstick #) 
	 */
	public static boolean DEBUG = false;
	private final int NUMBER_OF_PHILOSOPHERS = 5;
	private int SIMULATION_TIME = 10000;
	private int SEED = 0;
	
	ExecutorService executorService = null;
	ArrayList<Philosopher> philosophers = null;
	ArrayList<ChopStick> chopSticks = null;

	public void start() throws InterruptedException {
		try {
			
			for (int i = 1; i < NUMBER_OF_PHILOSOPHERS; i+=2) {
				executorService.execute(philosophers.get(i));
				Thread.sleep(50); //makes sure that this thread kicks in before the next one
			}
		
			for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i+=2) {
				executorService.execute(philosophers.get(i));
				Thread.sleep(50); //makes sure that this thread kicks in before the next one
			}
			
		

			// Main thread sleeps till time of simulation
			Thread.sleep(SIMULATION_TIME);
			
			//stop the philosophers
			for(Philosopher p : philosophers){
				p.isPhilospherDone.set(true);
			}
			
			

		} finally {
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.MILLISECONDS);
		}
	}

	public void initialize(int simulationTime, int randomSeed) {
		SIMULATION_TIME = simulationTime;
		SEED = randomSeed;
		
		philosophers = new ArrayList<Philosopher>(NUMBER_OF_PHILOSOPHERS);
		chopSticks = new ArrayList<ChopStick>(NUMBER_OF_PHILOSOPHERS);
		
		//create the executor service
		executorService = Executors.newFixedThreadPool(NUMBER_OF_PHILOSOPHERS);
		
		//creating 5 chop sticks with id number from 0 - 4
		for(int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++){
			chopSticks.add(new ChopStick(i));
		}
		
		
		//adding the chopsticks to the wise men every one finds his right and 
		//left chopstick but did not pick it up yet
	     int cs = 4;
		for(int i = 0; i < NUMBER_OF_PHILOSOPHERS ; i ++){
			philosophers.add(new Philosopher(i,chopSticks.get(cs),chopSticks.get(i),SEED));
			if(cs == 4)
				cs = 0;
			else
			    cs ++;
			}
		
		
	}
	
	public ArrayList<Philosopher> getPhilosophers() {
		return philosophers;
	}
	
	/*
	 * The following code prints a table where each row corresponds to one of the Philosophers, 
	 * Columns correspond to the Philosopher ID (PID), average think time (ATT), average eating time (AET), average hungry time (AHT), number of thinking turns(#TT), number of eating turns(#ET), and number of hungry turns(#HT).
	 * This table should be printed regardless of the DEBUG value
	 */
	public void printTable() {
		DecimalFormat df2 = new DecimalFormat(".##");
		System.out.println("\n---------------------------------------------------");
		System.out.println("PID \tATT \tAET \tAHT \t#TT \t#ET \t#HT");
		
		for (Philosopher p : philosophers) {
			System.out.println(p.getId() + "\t"
					+ df2.format(p.getAverageThinkingTime()) + "\t"
					+ df2.format(p.getAverageEatingTime()) + "\t"
					+ df2.format(p.getAverageHungryTime()) + "\t"
					+ p.getNumberOfThinkingTurns() + "\t"
					+ p.getNumberOfEatingTurns() + "\t"
					+ p.getNumberOfHungryTurns() + "\t");
		}
		
		System.out.println("---------------------------------------------------\n");
	}

}