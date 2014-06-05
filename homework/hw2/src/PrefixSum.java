import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class PrefixTree{
	int sum;
	int lo,hi;
	PrefixTree left, right;
	int fromleft;
	
	PrefixTree(int lo, int hi){
		sum = 0;
		this.lo = lo;
		this.hi = hi;
		fromleft = 0;
	}
}


public class PrefixSum {
	static class PrefixUp extends RecursiveTask<PrefixTree>{       //Up
		private static final long serialVersionUID = 1L;
		PrefixTree tree ;
		int[] array;
		
		PrefixUp(int[] array, int lo, int hi){
			tree = new PrefixTree(lo, hi);
			this.array = array;
		}
		
		protected PrefixTree compute() {
			if (tree.hi - tree.lo == 1){
				tree.sum = array[tree.lo];
				return tree;
			} 
			else {
				PrefixUp left = new PrefixUp(array, tree.lo, (tree.hi+tree.lo)/2);
				PrefixUp right = new PrefixUp(array, (tree.hi + tree.lo)/2, tree.hi);
				left.fork();
				tree.right = right.compute();
				tree.left = left.join();
				tree.sum = tree.left.sum + tree.right.sum;
				return tree;
			}
		}

	}

  static class PrefixDown extends RecursiveTask<Integer>{     //Down 
		private static final long serialVersionUID = 1L;
		int[] output;
		PrefixTree tree;
		PrefixDown(PrefixTree tr, int fl, int[] arr){	
		tree = tr;
		tree.fromleft = fl;
		output = arr;
	}
		@Override
		protected Integer compute() {
			if (tree.hi - tree.lo == 1){
				output[tree.lo] = tree.fromleft + tree.sum;
				return null;
			} 
			else {
				PrefixDown left = new PrefixDown(tree.left, tree.fromleft, output);
				PrefixDown right = new PrefixDown(tree.right, tree.fromleft + tree.left.sum, output);
				left.fork();
				right.compute();
				left.join();
				return null;
			}
		}
	}


	public static void main(String[] args) {
		int[] array = {1,2,3,4,5,6,7,8,9,10};
		int[] prefixSum = new int[array.length];
		
		final ForkJoinPool fjPool1 = new ForkJoinPool();
		PrefixTree tree = fjPool1.invoke(new PrefixUp(array, 0, array.length));
		
		final ForkJoinPool fjPool2 = new ForkJoinPool();
		fjPool2.invoke(new PrefixDown(tree, 0, prefixSum));
		
		System.out.print("Input array: ");
		for (int i : array){
			System.out.print(i+" ");
		}
		System.out.println(" ");
		System.out.print("Prefix sum  : ");
		for (int i : prefixSum){
			System.out.print(i+" ");
		}
	}
}
