
import java.util.Scanner;

public class  Pi {
	public static class NaivePi {
		static double MontePI(long n) {
			double PI;
			double x, y;
			long  sum = 0;
			for (long i = 1; i < n; i++) {
				x = Math.random();
				y = Math.random();
				if ((x * x + y * y) <= 1) {
					sum++;
				}
			}
			PI = 4.0 * sum / n;
			return PI;
		}
	 }
	 
	 class MultiPi extends Thread {
		 long n;
	     int sum = 0;
	     public MultiPi(long n) {
	    	 this.n = n;
	     }
		public void run() {  
	        double x, y;                
	        for (int i = 1; i < n; i++) {  
	        	x = Math.random();  
	            y = Math.random();  
	            if ((x * x + y * y) <= 1) {  
	            	sum++;  
	            }  	  
	        }  
	    } 
	 }
	
	 void  Compare(long n, int chunk) throws InterruptedException {
		double PI;
		long start = System.currentTimeMillis();
		PI = NaivePi.MontePI(n);
		long end = System.currentTimeMillis();
		System.out.println("NaivePi: "+PI  + "\t Time: " + (end - start));
		
		start = System.currentTimeMillis();
		
		long size = n/chunk;
		MultiPi[] mp = new MultiPi[chunk];
		start = System.currentTimeMillis();
		for (int i =0; i < chunk; i++) {
			mp[i] = new MultiPi(size); 
			mp[i].start();
		}
		int sum = 0;
	    for(int i=0; i < chunk; i++) {
	    	   mp[i].join();
	    	   sum += mp[i].sum;
	        }
	    PI = 4.0*sum/n;
	    end = System.currentTimeMillis();
	    System.out.println("MultiPi: "+PI  + "\t Time: " + (end - start));
	    
	}
	
  public static void main(String [] args) {
		System.out.println("蒙特卡洛概率算法计算圆周率:");
		Scanner input = new Scanner(System.in);
		System.out.println("输入点的数量：");
		long n = input.nextLong();
		input.close();
		Pi p = new Pi();
		try {
			p.Compare(n,  4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
  }
}