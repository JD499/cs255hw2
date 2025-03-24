import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BuiltInSort {
    public static void main(String[] args) {
        runManualTest();
        int outerLoops = 10;
        int[] exponents = {3, 4, 5, 6, 7, 8};
        Map<Integer, double[]> preallocated = preallocateArrays(exponents);
        double[][] results = runBuiltInSort(outerLoops, exponents, preallocated);
        saveResultsToCSV(results, exponents, "built_in_sort_results.csv");
    }

    private static void runManualTest() {
        System.out.println("Running manual test scenario for Built-In Sort:");
        double[] testArray = new double[10];

        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = Math.random();
        }

        System.out.println("Before sorting: " + Arrays.toString(testArray));
        double[] expectedArray = Arrays.copyOf(testArray, testArray.length);
        Arrays.sort(expectedArray);

        Arrays.sort(testArray);
        System.out.println("After sorting:  " + Arrays.toString(testArray));

        boolean isCorrect = Arrays.equals(testArray, expectedArray);
        System.out.println("Sort correct:   " + isCorrect);
        System.out.println();
    }

    private static Map<Integer, double[]> preallocateArrays(int[] exponents) {
        Map<Integer, double[]> arrays = new HashMap<>();
        for (int exp : exponents) {
            int size = (int) Math.pow(10, exp);
            double[] arr = new double[size];
            for (int i = 0; i < size; i++) {
                arr[i] = Math.random();
            }
            arrays.put(size, arr);
            System.out.println("Preallocated array of size " + size);
        }
        System.out.println();
        return arrays;
    }

    private static double[][] runBuiltInSort(
            int outerLoops, int[] exponents, Map<Integer, double[]> arrays) {
        System.out.println("Running Built-In Sort for Benchmarking:");
        double[][] runtimes = new double[exponents.length][outerLoops];

        for (int loop = 0; loop < outerLoops; loop++) {
            System.out.println("Iteration " + (loop + 1) + ":");
            for (int i = 0; i < exponents.length; i++) {
                int size = (int) Math.pow(10, exponents[i]);
                double[] original = arrays.get(size);
                double[] toSort = Arrays.copyOf(original, original.length);
                long startTime = System.nanoTime();
                Arrays.sort(toSort);
                long elapsed = System.nanoTime() - startTime;
                runtimes[i][loop] = elapsed / 1e9;
                System.out.println("  Array size " + size + " - Time (s): " + runtimes[i][loop]);
            }
            System.out.println();
        }
        return runtimes;
    }

    private static void saveResultsToCSV(double[][] runtimes, int[] exponents, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Array Size,First Run,Average of Remaining 9 Runs\n");

            for (int i = 0; i < exponents.length; i++) {
                int size = (int) Math.pow(10, exponents[i]);
                double firstRun = runtimes[i][0];
                double sum = 0;
                for (int j = 1; j < runtimes[i].length; j++) {
                    sum += runtimes[i][j];
                }
                double average = sum / (runtimes[i].length - 1);

                writer.write(size + "," + firstRun + "," + average + "\n");
            }

            System.out.println("Results saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
