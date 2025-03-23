import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MergeSort {
  public static void main(String[] args) {
    // Manual test using a small array of 10 values.
    runManualTest();

    int outerLoops = 10;
    int[] exponents = {3, 4, 5, 6, 7, 8};
    Map<Integer, double[]> preallocated = preallocateArrays(exponents);
    runMergeSort(outerLoops, exponents, preallocated);
  }

  // A simple manual test scenario on a small array.
  private static void runManualTest() {
    System.out.println("Running manual test scenario for Plain MergeSort:");
    double[] testArray = new double[10];
    for (int i = 0; i < testArray.length; i++) {
      testArray[i] = Math.random();
    }
    System.out.println("Before sorting: " + Arrays.toString(testArray));

    double[] expectedArray = Arrays.copyOf(testArray, testArray.length);
    Arrays.sort(expectedArray);

    mergeSort(testArray, 0, testArray.length - 1);
    System.out.println("After sorting:  " + Arrays.toString(testArray));

    boolean isCorrect = Arrays.equals(testArray, expectedArray);
    System.out.println("Sort correct:   " + isCorrect);
    System.out.println();
  }


  // Preallocate random arrays for various sizes.
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

  // Benchmarking loop for plain MergeSort.
  private static void runMergeSort(
          int outerLoops, int[] exponents, Map<Integer, double[]> arrays) {
    System.out.println("Running Plain MergeSort for Benchmarking:");
    for (int loop = 1; loop <= outerLoops; loop++) {
      System.out.println("Iteration " + loop + ":");
      for (int exp : exponents) {
        int size = (int) Math.pow(10, exp);
        double[] original = arrays.get(size);
        double[] toSort = Arrays.copyOf(original, original.length);
        long startTime = System.nanoTime();
        mergeSort(toSort, 0, toSort.length - 1);
        long elapsed = System.nanoTime() - startTime;
        System.out.println("  Array size " + size + " - Time (ns): " + elapsed);
      }
      System.out.println();
    }
  }

  // Standard recursive MergeSort.
  public static void mergeSort(double[] A, int p, int r) {
    if (p >= r)
      return;
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

    for (int i = 0; i < nL; i++) {
      L[i] = A[p + i];
    }
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
