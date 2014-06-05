import java.util.Random;
class MyThreadA extends Thread {
	public  void run() { // entry point for thread
		try {
			Random rd = new Random(7);
			for(int i = 5; i > 0; i--) {
            		System.out.println("MyThreadA");
            		Thread.sleep(rd.nextInt(100));
         		}
         	}
         	catch(InterruptedException e) {
         		System.out.println("Interrupted.");    
        	}
	}
}

class MyThreadB extends Thread {
	public  void run() { // entry point for thread
		try {
			Random rd = new Random(10);
			for(int i = 5; i > 0; i--) {
            		System.out.println("MyThreadB");
            		Thread.sleep(rd.nextInt(100));
         		}
         	}
        catch(InterruptedException e) {
         	System.out.println("Interrupted.");    
        }
     }
}

public class RunvsStart {
	public enum ORDER { R2, S2, FRLS, FSLR }
	public  static void order(ORDER order) {
		MyThreadA t1 = new MyThreadA();
		MyThreadB t2 = new MyThreadB();
		switch (order) {
			case R2:
			    t1.run();
			    t2.run();
			    break;
			case S2:
			    t1.start();
			    t2.start();
			    break;
			case FRLS:
			    t1.run();
			    t2.start();
			    break;
			case FSLR:
			    t1.start();
			    t2.run();
			    break;
			default: break;
		}
	}
	public static void main(String [] args) {
		System.out.println("Both run:");
		order(ORDER.R2);


		System.out.println("First run, then start:");
		order(ORDER.FRLS);

		try {
			Thread.sleep(1000);
		}
        	catch(InterruptedException e) {
         	System.out.println("Interrupted.");    
        	}

		System.out.println("First start, then run:");
		order(ORDER.FSLR);

		System.out.println("Both start:");
		order(ORDER.FSLR);
	}
}
