import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private int balance;
	Lock lock;
	Condition condition;
	
	enum Prior { LOW, HIGH};
	Prior prior = Prior.LOW;;
	
	public Account(int balance) {
		this.balance = balance;
	}
	
	public void withdraw(int amount) throws InterruptedException{
			lock.lock();
			try {
				while(balance<amount) {
					condition.await();
				}
			balance -= amount;
			} finally {
				lock.unlock();
			}
	}
	
	public void withdraw(int amount, Prior prior) throws InterruptedException{
		if ( prior == Prior.LOW && this.prior == Prior.HIGH) {
			return;
		}
		lock.lock();
		try {
			this.prior = prior;
			while(balance<amount) {
				condition.await();
			}
		balance -= amount;
		} finally {
			this.prior = Prior.LOW;
			lock.unlock();
		}
}
	
	public void deposit(int amount) throws InterruptedException{
		    lock.lock();
		    try {
		    	balance +=amount;
		    	condition.notifyAll();// has money, notify
		    } finally {
				lock.unlock();
		    }
	}
	
	public void transfer(int k, Account reserve) throws InterruptedException {
		lock.lock();
		try {
			reserve.withdraw(k);
			deposit(k);
		} finally {
			lock.unlock();
		}
	}
	
}

	
	
	