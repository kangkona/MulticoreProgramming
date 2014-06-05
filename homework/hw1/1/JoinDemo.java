class SharedData {
	public int a = 0;
	public String s = null;

	public SharedData() {
		a = 10;
		s = "Test";
	}
}

class AddThread  extends Thread {
	private SharedData m_data = null;
	public AddThread(SharedData data) {
		m_data = data;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			m_data.a++;
			System.out.println(m_data.a);
		}
	}
}

class SubThread  extends Thread {
	private SharedData m_data = null;
	public SubThread(SharedData data) {
		m_data = data;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			m_data.a--;
			System.out.println(m_data.a);
		}
	}
}

public class JoinDemo {
	public static void main(String[] args) {
		SharedData data = new SharedData();
		AddThread add1 = new AddThread(data);
		SubThread sub1 = new SubThread(data);

		System.out.println("No Join:");
		add1.start();
		sub1.start();
		// for (int i = 0; i < 10; i++) {
		// 	System.out.println(data.a);
			
		// }

		try {
			Thread.sleep(1000);
		}
        catch(InterruptedException e) {
         	System.out.println("Interrupted.");    
        }


		AddThread add2 = new AddThread(data);
		SubThread sub2 = new SubThread(data);

		System.out.println("With Join:");
		add2.start();
	    try {
			add2.join();
		}
            catch(InterruptedException e) {
         	System.out.println("Interrupted.");    
            }
		sub2.start();
	}
}
