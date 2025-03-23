import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MergeSort {
  public static void main(String[] args) {
    int outerLoops = 10;
    int[] exponents = {3, 4, 5, 6, 7, 8};
    Map<Integer, double[]> preallocated = preallocateArrays(exponents);
    double[][] results = runMergeSort(outerLoops, exponents, preallocated);
    saveResultsToCSV(results, exponents, "merge_sort_results.csv");
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

  private static double[][] runMergeSort(
      int outerLoops, int[] exponents, Map<Integer, double[]> arrays) {
    System.out.println("Running Plain MergeSort for Benchmarking:");
    double[][] runtimes = new double[exponents.length][outerLoops];

    for (int loop = 0; loop < outerLoops; loop++) {
      System.out.println("Iteration " + (loop + 1) + ":");
      for (int i = 0; i < exponents.length; i++) {
        int size = (int) Math.pow(10, exponents[i]);
        double[] original = arrays.get(size);
        double[] toSort = Arrays.copyOf(original, original.length);
        long startTime = System.nanoTime();
        mergeSort(toSort, 0, toSort.length - 1);
        long elapsed = System.nanoTime() - startTime;
        runtimes[i][loop] = elapsed / 1e9; // Convert to seconds
        System.out.println("  Array size " + size + " - Time (s): " + runtimes[i][loop]);
      }
      System.out.println();
    }
    return runtimes;
  }

  private static void saveResultsToCSV(double[][] runtimes, int[] exponents, String fileName) {
    try (FileWriter writer = new FileWriter(fileName)) {
      // Write header
      writer.write("Array Size,First Run,Average of Remaining 9 Runs\n");

      for (int i = 0; i < exponents.length; i++) {
        int size = (int) Math.pow(10, exponents[i]);
        double firstRun = runtimes[i][0];
        double sum = 0;
        for (int j = 1; j < runtimes[i].length; j++) {
          sum += runtimes[i][j];
        }
        double average = sum / (runtimes[i].length - 1);

        // Write data row
        writer.write(size + "," + firstRun + "," + average + "\n");
      }

      System.out.println("Results saved to " + fileName);
    } catch (IOException e) {
      System.err.println("Error writing to CSV file: " + e.getMessage());
    }
  }

  public static void mergeSort(double[] A, int p, int r) {
    if (p >= r) return;
    int q = (p + r) / 2;
    mergeSort(A, p, q);
    mergeSort(A, q + 1, r);
    merge(A, p, q, r);
  }

  public static void merge(double[] A, int p, int q, int r) {
    int nL = q - p + 1;
    int nR = r - q;
    double[] L = new double[nL];
    double[] R = new double[nR];

      System.arraycopy(A, p + 0, L, 0, nL);
    for (int j = 0; j < nR; j++) {
      R[j] = A[q + 1 + j];
    }

    int i = 0;
    int j = 0;
    int k = p;

    while (i < nL && j < nR) {
      if (L[i] <= R[j]) {
        A[k] = L[i];
        i++;
      } else {
        A[k] = R[j];
        j++;
      }
      k++;
    }

    while (i < nL) {
      A[k] = L[i];
      i++;
      k++;
    }

    while (j < nR) {
      A[k] = R[j];
      j++;
      k++;
    }
  }
}
