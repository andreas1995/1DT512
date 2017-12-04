package ac222qf;

import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
	private final int id;
    private boolean taken;
	
	public ChopStick(int id) {
		this.id = id;
		this.taken = false;
	}
	
	public int getId(){
		return id;
	}
	
	public synchronized boolean isItLocked(){
		//return true if the chopstick is taken
		return taken;
	}
	//lock and unlock the chopsticks
	public synchronized void Lock(){
		this.taken = true;
	}
	public synchronized void UnLock(){
		this.taken = false;
	}
	
	
	
}
