package question5;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

public class CallingThread {

	public static void main(String[] args) throws IOException {
		runManualTest();
		double[][] runtimes;
		int outerLoops = 10;
		int[] exponents = {3, 4, 5, 6, 7, 8};
		Random r = new Random();

		runtimes = new double[exponents.length][outerLoops];

		try {
			for (int i = 0; i < exponents.length; i++) {
				double n = Math.pow(10, exponents[i]);
				System.out.println("Processing array size: " + (int) n);

				for (int k = 0; k < outerLoops; k++) {
					double[][] a = new double[2][(int) n];
					ForkJoinPool pool1 = new ForkJoinPool(10000);
					RecursiveFillZeroInArray task2 = new RecursiveFillZeroInArray(a, 0, a.length - 1);
					a = pool1.invoke(task2);
					pool1.shutdown();

					long startTime = System.nanoTime();
					ForkJoinPool pool = new ForkJoinPool(10000);
					RecursiveDivideArray task1 = new RecursiveDivideArray(a[0], 0, a.length - 1, a[1]);
					a[1] = pool.invoke(task1);
					pool.shutdown();
					long endTime = System.nanoTime();

					double elapsedTime = (endTime - startTime) / 1e9;
					runtimes[i][k] = elapsedTime;

					System.out.println("  Iteration " + (k + 1) + " - Time (s): " + elapsedTime);
				}
				System.out.println();
			}

			saveResultsToCSV(runtimes, exponents, "question5.csv");

		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private static void saveResultsToCSV(double[][] runtimes, int[] exponents, String fileName) {
		try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(fileName)).build()) {
			writer.writeNext(new String[] {"Array Size", "First Run", "Average of Remaining 9 Runs"});

			for (int i = 0; i < exponents.length; i++) {
				int size = (int) Math.pow(10, exponents[i]);
				double firstRun = runtimes[i][0];
				double sum = 0;
				for (int j = 1; j < runtimes[i].length; j++) {
					sum += runtimes[i][j];
				}
				double average = sum / (runtimes[i].length - 1);

				writer.writeNext(new String[] {String.valueOf(size), String.valueOf(firstRun), String.valueOf(average)});
			}

			System.out.println("Results saved to " + fileName);
		} catch (IOException e) {
			System.err.println("Error writing to CSV file: " + e.getMessage());
		}
	}

	private static void runManualTest() {
		System.out.println("Running manual test scenario for RecursiveDivideArray:");
		int n = 10;
		double[][] a = new double[2][n];
		Random random = new Random();

		for (int i = 0; i < n; i++) {
			a[0][i] = random.nextDouble();
		}

		System.out.println("Before sorting: " + Arrays.toString(a[0]));

		double[] expectedArray = Arrays.copyOf(a[0], n);
		Arrays.sort(expectedArray);

		long startTime = System.nanoTime();
		ForkJoinPool pool = new ForkJoinPool(10000);
		RecursiveDivideArray task = new RecursiveDivideArray(a[0], 0, n - 1, a[1]);
		a[1] = pool.invoke(task);
		pool.shutdown();
		long endTime = System.nanoTime();

		double elapsedTime = (endTime - startTime) / 1e9;

		System.out.println("After sorting:  " + Arrays.toString(a[1]));
		System.out.println("Sort correct:   " + Arrays.equals(a[1], expectedArray));
		System.out.println("Time taken (s): " + elapsedTime);
		System.out.println();
	}
}
