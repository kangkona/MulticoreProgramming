import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class Resource<T> {
   ArrayList<T> buffer = new ArrayList<T>();
   final int COUNT = 10;
   abstract  void put();
   abstract  void get();
}

//Sync 
class ResourceS<T>  extends Resource{
	public synchronized void put() {
		while (buffer.size() == COUNT) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T  item = (T)new Object(); 
		buffer.add(item);
		System.out.println("Producer an item, buffer size:  " + buffer.size());
		notify();
	}
	
	public synchronized void get() {
		while (buffer.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.remove(0);
		System.out.println("Consumer an item, buffer size:  " + buffer.size());
		notify();
	}
}

//Lock
class ResourceL<T>  extends Resource{
//    private ArrayList<T> buffer = new ArrayList<T>();
//    private final int COUNT = 10;
    private final Lock lock = new ReentrantLock();
    private final Condition full = lock.newCondition();
    private final Condition empty = lock.newCondition();
	public void put() {
		  lock.lock();
          try {
              while (buffer.size() == COUNT) {
                  System.out.println("It's full, can't produce.");
                  full.await();
              }
              T item = (T)new Object();
              if (buffer.add(item)) {
            	  System.out.println("Produce an item, buffer size:  " + buffer.size());
                  empty.signal();
              }
          } catch (InterruptedException ie) {
              System.out.println("producer is interrupted!");
          } finally {
              lock.unlock();
          }
	}

	public void get() {
		  lock.lock();
          try {
              while (buffer.size() == 0) {
                  System.out.println("It's empty, can't consume.");
                  empty.await();
              }
              if (buffer.remove(0) != null ) {
            	  System.out.println("Consume an item, buffer size:  " + buffer.size());
                  full.signal();
              }
          } catch (InterruptedException ie) {
              System.out.println("consumer is interrupted!");
          } finally {
              lock.unlock();
          }
       }
}


class Producer<T> implements Runnable {
	private Resource<T> resource;
	long round;

	public Producer(Resource<T> resource, long round) {
		this.resource = resource;
		this.round = round;
	}
	@Override
	public void run() {
		for(int i=0; i < round; i++) {
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resource.put();
		}
	}
}

class Consumer<T> implements Runnable {
	private Resource<T> resource;
	long round;

	public Consumer(Resource<T> resource, long round) {
		this.resource = resource;
		this.round = round;
	}
	
	@Override
	public void run() {
	    for(int i=0; i < round; i++) {
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resource.get();
		}
	}
}

public class ProducerConsumerCurr {
	
	public static void Test(Resource resource) {
		new Thread(new Producer(resource,100)).start();
		new Thread(new Consumer(resource,100)).start();
		new Thread(new Producer(resource,1000)).start();
		new Thread(new Consumer(resource,1000)).start();
		new Thread(new Producer(resource,1000)).start();
		new Thread(new Consumer(resource,1000)).start();
	}
	public static void main(String[] args) {
//		Resource<Object> rs = new ResourceS<Object>();
//		Test(rs);
		Resource<Object> rl = new ResourceL<Object>();
		Test(rl);
	}
}
