//import java.util.ArrayList;
//
//class Resource<T> {
//    private ArrayList<T> buffer = new ArrayList<T>();
//    private final int COUNT = 10;
//	public synchronized void put() {
//		while (buffer.size() == COUNT) {
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		T  item = (T)new Object(); 
//		buffer.add(item);
//		System.out.println("Producer an item, buffer size:  " + buffer.size());
//		notify();
//	}
//
//	public synchronized void get() {
//		while (buffer.size() == 0) {
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		buffer.remove(0);
//		System.out.println("Consumer an item, buffer size:  " + buffer.size());
//		notify();
//	}
//}
//
//class Producer<T> implements Runnable {
//	private Resource<T> resource;
//
//	public Producer(Resource<T> resource) {
//		this.resource = resource;
//	}
//	
//	@Override
//	public void run() {
//		while(true) {
//			try {
//				Thread.sleep((long) (Math.random() * 1000));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			resource.put();
//		}
//	}
//}
//
//class Consumer<T> implements Runnable {
//	private Resource<T> resource;
//
//	public Consumer(Resource<T> resource) {
//		this.resource = resource;
//	}
//	
//	@Override
//	public void run() {
//		while(true) {
//			try {
//				Thread.sleep((long) (Math.random() * 1000));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			resource.get();
//		}
//	}
//}
//
//public class ProducerConsumerSync {
//	public static void main(String[] args) {
//		Resource<Object> resource = new Resource<Object>();
//		new Thread(new Producer(resource)).start();
//		new Thread(new Consumer(resource)).start();
//		new Thread(new Producer(resource)).start();
//		new Thread(new Consumer(resource)).start();
//		new Thread(new Producer(resource)).start();
//		new Thread(new Consumer(resource)).start();
//	}
//}
