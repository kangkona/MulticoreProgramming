class MyThread extends Thread {
	public void run() {
		for (int i=0; i < 1000; i++) {
			System.out.println("hello");
		}
	}
}

public class Main4 {
	public static void main(String[] args) {
		MyThread t1 = new MyThread();
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			System.out.println("Error");
		}

		System.out.println("Thread is done.");
	}
}