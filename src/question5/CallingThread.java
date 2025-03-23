package question5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;


public class CallingThread {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CallingThread m = new CallingThread();
		/*
		int[] a = {0,9,-7,5,7,4,12,-3};
		int[] b = {0,0,0,0,0,0,0,0};
		*/
		double n=0;
		double[][] a;
		Random r = new Random();

		List<String[]> csvData=new ArrayList<String[]>();
		String[] csvarr;
		//PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\Desktop\\Question5.txt"));
		//output to the file a line

		try{
			for(double i=3;i<=8;i++) {
				if(i==4)break;
				n=Math.pow(10, i);

				a=new double[2][(int) n];

				for(int k=0;k<10;k++) {
					if(k==1)break;

					ForkJoinPool pool1 = new ForkJoinPool(10000);
					RecursiveFillZeroInArray task2 = new RecursiveFillZeroInArray(a, 0, a.length-1);
					a=pool1.invoke(task2);
					pool1.shutdown();


			/*
			 out.println("-------Before Sorting------- N= "+n);
			 out.println();
			 out.println();
			 out.println("Array A ");

			 for(int j=0;j<n;j++) {
				 out.print(" "+a[j]);
			}
			 out.println();
			 out.println();
			 out.println("Array B ");

			for(int j=0;j<n;j++) {
				out.print(" "+b[j]);
			}
			*/

					long startTime = System.nanoTime();
					ForkJoinPool pool = new ForkJoinPool(10000);
					RecursiveDivideArray task1 = new RecursiveDivideArray(a[0], 0, a.length-1,a[1]);
					a[1]=pool.invoke(task1);
					pool.shutdown();

					long endTime = System.nanoTime();

					long elapsedTime = endTime - startTime;

					//		out.println();
					//		out.println();
					//		out.println("Loop "+k+" Time taken for N= "+n+" = "+elapsedTime);
					//		out.println("---------------------------------------");
					csvarr = new String[2];
					csvarr[0]=" "+n;
					csvarr[1]=" "+elapsedTime;
					csvData.add(csvarr);
			 /*
			 out.println();
			 out.println();
			 out.println("-------After Sorting------- N= "+n);
			 out.println();
			 out.println();
			 out.println("Array A ");
			for(int j=0;j<n;j++) {
				   out.print(" "+a[j]);
			}
			 out.println();
			 out.println();
			 out.println("Array B ");

			for(int j=0;j<n;j++) {
				out.print(" "+b[j]);
			}
			*/
				}
			}

			try (ICSVWriter writer = new CSVWriterBuilder(
					new FileWriter("question5.csv"))
					.build()) {
				writer.writeAll(csvData);

			}
			//	out.close();
		}catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}


