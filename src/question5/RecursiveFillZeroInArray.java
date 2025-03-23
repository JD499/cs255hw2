package question5;

import java.util.Random;
import java.util.concurrent.RecursiveTask;


public class RecursiveFillZeroInArray extends RecursiveTask<double[][]> {
    private double[][] a;
    private int start, end;
    
    public RecursiveFillZeroInArray(double[][] a, int start, int end) {
        this.a = a;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected double[][] compute() {
    	if(start==end) {
    		Random r = new Random();
    		a[0][start]=r.nextDouble();
    		a[1][start]=0;
    	}else {
          
    		int mid=(start+end)/2;
    		RecursiveFillZeroInArray leftTask = new RecursiveFillZeroInArray(a, start, mid);
    		RecursiveFillZeroInArray rightTask = new RecursiveFillZeroInArray(a, mid+1, end);
    		
            leftTask.fork(); 
            a = rightTask.compute(); 
            a = leftTask.join(); 

        }
		return a;
    }
    
}
