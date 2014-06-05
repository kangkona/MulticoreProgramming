
import java.util.Arrays;  
import java.util.Random;  
import java.util.concurrent.TimeUnit;  
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


class SortTask extends RecursiveAction {
	private static final long serialVersionUID = 1L;
	final long[] array;
  	final int lo;
 	final int hi;
    private int THRESHOLD = 200; 

   public SortTask(long[] array) {
  	     this.array = array;
  	     this.lo = 0;
  	     this.hi = array.length - 1;
 	  }

 	public SortTask(long[] array, int lo, int hi) {
 	      this.array = array;
  	     this.lo = lo;
  	     this.hi = hi;
   	}
	
 	protected void compute() {  
 	      if (lo - hi < THRESHOLD)  
 	            sequentiallySort(array, lo, hi);  
 	      else {  
 	            int pivot = partition(array, lo,  hi);  
 	            new SortTask(array, lo, pivot - 1).fork();  
 	            new SortTask(array, pivot + 1, hi).fork();  
 	        }  
 	    }  

   	private int partition(long[] array, int lo, int hi) {
     	  long x = array[hi];
      	 int i = lo - 1;
     	  for (int j = lo; j < hi; j++) {
       	    if (array[j] <= x) {
               i++;
               swap(array, i, j);
         	  }
         }
         swap(array, i + 1, hi);
      	 return i + 1;
   	 }
   	
  	 private void swap(long[] array, int i, int j) {
       	if (i != j) {
          	 long temp = array[i];
           	array[i] = array[j];
           	array[j] = temp;
       	}
   	}

   	private void sequentiallySort(long[] array, int lo, int hi) {
       	Arrays.sort(array, lo, hi + 1);
 	  }
 }

public class ForkJoinDemo {
	public static void main(String[] args) throws InterruptedException {  
		 ForkJoinPool forkJoinPool = new ForkJoinPool();  
		    Random rnd = new Random();  
		    final int SIZE = 15000;
		    long[] array = new long[SIZE];  
		    for (int i = 0; i < SIZE; i++) {  
		        array[i] = rnd.nextInt();  
		    }  
		    forkJoinPool.submit(new SortTask(array));  
		    forkJoinPool.shutdown();  
		    forkJoinPool.awaitTermination(50, TimeUnit.SECONDS);  
		  
		    for (int i = 1; i < SIZE; i++) {  
		        assert(array[i - 1] < array[i]);   
		    } 
   }  
	
}

