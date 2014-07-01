import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

interface MaleFemaleLock {
		Lock male();
		Lock female();
}

public class Bathroom implements MaleFemaleLock {
		volatile int males;
		volatile int females;
	
		Lock lock;
		Condition condition;
		Lock malelock, femalelock;
	
		public Bathroom() {
			males = 0;
			females = 0;
			lock = new ReentrantLock();
			malelock = new MaleLock();
			femalelock = new FemaleLock();
			condition = lock.newCondition();
		}
		class FemaleLock implements Lock {		
			public void enterFemale() {
				lock.lock();
				try {
					while (males > 0) {
						condition.await();
					}
					females++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		
			public void leaveFemale() {
				lock.lock();
				try {
					females--;
				
					if (females == 0) {
						condition.signalAll();
					}
				} finally {
					lock.unlock();
				}
			}
		}
		
		class MaleLock implements Lock {
			public void enterMale() {
				lock.lock();
				try {
					while (females > 0) {
						condition.await();
					}
					males++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		
			public void  leaveMale() {
				lock.lock();
				try {
					males--;
					if (males == 0) {
						condition.signalAll();
					}
				} finally {
					lock.unlock();
				}
			}
		}

                    @Override
		public Lock male() {
			return malelock;
		}

	          @Override
		public Lock female() {
			return femalelock;
		}
}


public class Bathroom2 implements MaleFemaleLock {
		volatile int males;
		volatile int females;

		public Bathroom() {
			males = 0;
			females = 0;
		}
		class FemaleLock implements Lock {		
			public synchronized void enterFemale() {
					while (males > 0) {
						wait();
					}
					females++;
			}
		
			public synchronized void leaveFemale() {
					females--;
					if (females == 0) {
						notifyAll();
					}
			}
		}
		
		class MaleLock implements Lock {
			public synchronized void enterMale() {
					while (females > 0) {
						wait();
					}
					males++;
			}
		
			public synchronized void  leaveMale() {
					males--;
					if (males == 0) {
						notifyAll();
					}
			}
		}
}