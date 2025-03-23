package question5;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class RecursiveDivideArray extends RecursiveTask<double[]> {
    private double[] a,b;
    private int start, end;
    
    public RecursiveDivideArray(double[] a, int start, int end,double[] b) {
        this.a = a;
        this.start = start;
        this.end = end;
        this.b=b;
    }
    
    @Override
    protected double[] compute() {
    	int position=-1;
		
    	if(start==end) {
    		
    		RecursiveFindPosition positionTask = new RecursiveFindPosition(a, 0, a.length-1,a[start]);
    		positionTask.fork();
			position=positionTask.join();
		    
			b[position]=a[start];
		
    	}else {
          
    		int mid=(start+end)/2;
    		RecursiveDivideArray leftTask = new RecursiveDivideArray(a, start, mid,b);
    		RecursiveDivideArray rightTask = new RecursiveDivideArray(a, mid+1, end,b);
    		
            leftTask.fork(); 
            b = rightTask.compute(); 
            b = leftTask.join(); 

        }
		return b;
    }
    
}
