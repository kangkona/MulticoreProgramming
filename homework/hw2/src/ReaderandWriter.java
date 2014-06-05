import java.util.concurrent.CountDownLatch;

public class ReaderandWriter {
	
	//读锁
	static Object w = new Object();
	//写锁
	static Object r = new Object();
	
	//内容 
	static int data = 0 ;

	static CountDownLatch latch = new CountDownLatch(150);

	class Reader extends Thread {
		int quantity;

		Reader(int quantity) {
			this.quantity = quantity;
		}

		@Override
		public void run() {
			synchronized (w) {
					while (quantity > 0) {
						System.out.println(getName()
								+ " is reading ...【data=" + data + "】");
						quantity--;
					}
					latch.countDown();
			}
		}
	}

class Writer extends Thread {
		int quantity;
		Writer(int quantity) {
			this.quantity = quantity;
		}
		
		@Override
		public void run() {
			synchronized (w) {
				synchronized (r) {
					while (quantity > 0) {
						data++;
						System.out.println(getName() + " is writing...【data=" + data + "】");
						quantity--;
					}
					latch.countDown();
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		long startTime = System.nanoTime();
		ReaderandWriter demo = new ReaderandWriter();
		for (int i = 0; i < 100; ++i) {
			demo.new Reader(10).start();
		}
		for (int i = 0; i < 50; ++i) {
			demo.new Writer(1).start();
		}

		latch.await();
		long endTime = System.nanoTime();
		System.out.println(endTime - startTime + "ns");
	}

}