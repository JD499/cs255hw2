import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MergeSortCoarsened {
  public static void main(String[] args) {
    runManualTest();

    int outerLoops = 10;
    int[] exponents = {3, 4, 5, 6, 7, 8};
    int threshold = 10;
    Map<Integer, double[]> preallocated = preallocateArrays(exponents);
    runMergeSortCoarsened(outerLoops, exponents, preallocated, threshold);
  }

  private static void runManualTest() {
    System.out.println("Running manual test scenario for Optimized MergeSort:");
    double[] testArray = new double[10];

    for (int i = 0; i < testArray.length; i++) {
      testArray[i] = Math.random();
    }

    System.out.println("Before sorting: " + Arrays.toString(testArray));
    double[] expectedArray = Arrays.copyOf(testArray, testArray.length);
    Arrays.sort(expectedArray);

    mergesortCoarsened(testArray, 0, testArray.length - 1, 10);
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

  private static void runMergeSortCoarsened(
      int outerLoops, int[] exponents, Map<Integer, double[]> arrays, int threshold) {
    System.out.println(
        "Running MergeSort with coarsened base case (threshold = "
            + threshold
            + ") for Benchmarking:");
    for (int loop = 1; loop <= outerLoops; loop++) {
      System.out.println("Iteration " + loop + ":");
      for (int exp : exponents) {
        int size = (int) Math.pow(10, exp);
        double[] original = arrays.get(size);
        double[] toSort = Arrays.copyOf(original, original.length);
        long startTime = System.nanoTime();
        mergesortCoarsened(toSort, 0, toSort.length - 1, threshold);
        long elapsed = System.nanoTime() - startTime;
        System.out.println("  Array size " + size + " - Time (ns): " + elapsed);
      }
      System.out.println();
    }
  }

  public static void mergesortCoarsened(double[] A, int p, int r, int k) {
    if (r - p + 1 <= k) {
        insertionSort(A, p, r);
        return;
    }
    int q = (p + r) / 2;
    mergesortCoarsened(A, p, q, k);
    mergesortCoarsened(A, q + 1, r, k);
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

    public static void insertionSort(double[] A, int p, int r) {
        for (int i = p + 1; i <= r; i++) {
            double key = A[i];
            int j = i - 1;
            while (j >= p && A[j] > key) {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = key;
        }
    }

}
