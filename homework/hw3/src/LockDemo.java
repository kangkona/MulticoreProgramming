abstract class Lock extends Thread{
	static int counter  = 0;
	abstract void lock();
	abstract void unlock();
}

/*
 * Peterson Algorithm that give prior to other
 */
class PetersonLock extends Lock{
	private static boolean flag[] = {false, false};
	private static volatile int victim = -1;   //Neither of both
	private final int id;
	private int round = 1;
	
	public PetersonLock(int id, int round) {
		this.id = id;
		this.round = round;
	}
		
	void lock() {
		flag[id] = true;
		victim = id;
		while(flag[(id + 1) % 2] && victim == id) {
			System.out.println("Thread " + id + " is waiting...");
		}
	}
	
	void unlock() {
		flag[id] = false;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < round; i++) {
			lock();
			counter =  ( id == 0 ? counter +1 : counter - 1);
			System.out.println("Thread " + id + " modify counter: " + counter);
			unlock();
		}
	}
}

/*
 * Test-and-Set Spin Lock
 */

class TASLock  extends Lock{
	static class AtomicBoolean {
		boolean value;
		public AtomicBoolean(boolean value) {
			this.value = value;
		}
		public synchronized boolean getAndSet(boolean newValue) {
		   boolean prior = value;
		   value = newValue;
		   return prior;
		 }
		public void set(boolean value) {
			this.value = value;
		}
	 }

	private  static AtomicBoolean state = new TASLock.AtomicBoolean(false);
	private final int id;
	private int round = 1;
	
	public TASLock(int id, int round) {
		this.id = id;
		this.round = round;
	}
	
	 void lock() {
	  while (state.getAndSet(true)) {
		  System.out.println("Thread " + id + " is waiting...");
	  }
	 }
	 
	 void unlock() {
	  state.set(false);
	 }

		@Override
		public void run() {
			for(int i =0; i < round; i++) {
				lock();
				counter +=id;
				System.out.println("Thread " + id + " modify counter: " + counter);
				unlock();
			}
		}
} 



public class LockDemo {
	public static void main(String[] args) throws InterruptedException {
//		Lock  lock0 = new PetersonLock(0, 1);
//	    Lock  lock1 =  new PetersonLock(1, 1);
		Lock lock0 = new PetersonLock(0, 3);
		Lock lock1 = new PetersonLock(1, 3);
	    System.out.println("PeterSon Lock:");
	    lock0.start();
	    lock1.start();
	    lock0.join();
	    lock1.join();
	   
	    System.out.println("\n\nTest-and-Set Lock:");
//	    Lock lock0 = new TASLock(2, 2);
//	    Lock lock1 = new TASLock(3, 2);
//	    Lock lock2 = new TASLock(4,3);
//	    Lock lock3 = new TASLock(5,3);
	    for(int i=0; i < 10; i++) {
	    	new Thread(new TASLock(i, 2), "Thread").start();
	    }
//	    lock0 = new TASLock(2, 3);
//	    lock1 = new TASLock(3, 3);
//	    lock0.start();
//	    lock1.start(); 		
//	    lock2.start();
//	    lock3.start();
	}
}