package question5;

import java.util.concurrent.RecursiveTask;

public class RecursiveFindPosition extends RecursiveTask<Integer> {
	    private double[] a;
	    private int start, end;
	    private double number;
	    
	    public RecursiveFindPosition(double[] a, int start, int end,double number) {
	        this.a = a;
	        this.start = start;
	        this.end = end;
	        this.number=number;
	    }
	    
	    @Override
	    protected Integer compute() {
	    	int position1=-1;
			int position2=-1;
			
			if(start==end) {
				if(a[start]<number) {
					return 1; 
				}
				else return 0;
			}else {
	           
	            int mid = start + (end - start) / 2;
	            RecursiveFindPosition leftTask = new RecursiveFindPosition(a, start, mid,number);
	            RecursiveFindPosition rightTask = new RecursiveFindPosition(a, mid+1, end,number);

	            leftTask.fork(); 
	            position1 = rightTask.compute(); 
	            position2 = leftTask.join(); 

	            return position1 + position2; 
	        }
	    }
}
