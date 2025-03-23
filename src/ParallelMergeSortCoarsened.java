import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ParallelMergeSortCoarsened {

    private static final ForkJoinPool pool =
            new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        runManualTest();

        int outerLoops = 10;
        int[] exponents = {3, 4, 5, 6, 7, 8};
        int threshold = 10;
        Map<Integer, double[]> preallocated = preallocateArrays(exponents);
        runParallelMergeSort(outerLoops, exponents, preallocated, threshold);
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

    private static void runParallelMergeSort(
            int outerLoops, int[] exponents, Map<Integer, double[]> arrays, int threshold) {
        System.out.println("Running Parallel MergeSort for Benchmarking:");
        for (int loop = 1; loop <= outerLoops; loop++) {
            System.out.println("Iteration " + loop + ":");
            for (int exp : exponents) {
                int size = (int) Math.pow(10, exp);
                double[] original = arrays.get(size);
                double[] toSort = Arrays.copyOf(original, original.length);
                long startTime = System.nanoTime();
                pool.invoke(new parallelMergeSort(toSort, 0, toSort.length - 1, threshold));
                long elapsed = System.nanoTime() - startTime;
                System.out.println("  Array size " + size + " - Time (ns): " + elapsed);
            }
            System.out.println();
        }
    }

    private static void runManualTest() {
        System.out.println("Running manual test scenario for Parallel MergeSort:");
        double[] testArray = new double[10];
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = Math.random();
        }
        System.out.println("Before sorting: " + Arrays.toString(testArray));

        double[] expectedArray = Arrays.copyOf(testArray, testArray.length);
        Arrays.sort(expectedArray);

        pool.invoke(new parallelMergeSort(testArray, 0, testArray.length - 1, 10));
        System.out.println("After sorting:  " + Arrays.toString(testArray));

        boolean isCorrect = Arrays.equals(testArray, expectedArray);
        System.out.println("Sort correct:   " + isCorrect);
        System.out.println();
    }

    public static void pMerge(double[] A, int p, int q, int r) {
        double[] B = new double[r - p + 1];

        pool.invoke(new PMergeAux(A, p, q, q + 1, r, B, 0));

        IntStream.rangeClosed(p, r).parallel().forEach(i -> A[i] = B[i - p]);
    }

    public static int findSplitPoint(double[] A, int p, int r, double x) {
        int low = p;
        int high = r + 1;

        while (low < high) {
            int mid = (low + high) / 2;
            if (x <= A[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        return low;
    }

    static class parallelMergeSort extends RecursiveAction {
        private final double[] A;
        private final int p;
        private final int r;
        private final int k;

        public parallelMergeSort(double[] A, int p, int r, int k) {
            this.A = A;
            this.p = p;
            this.r = r;
            this.k = k;
        }

        @Override
        protected void compute() {
            if (r - p + 1 <= k) {
                insertionSort(A, A.length);
                return;
            }
            int q = (p + r) / 2;

            parallelMergeSort child1 = new parallelMergeSort(A, p, q, k);
            parallelMergeSort child2 = new parallelMergeSort(A, q + 1, r, k);

            invokeAll(child1, child2);

            pMerge(A, p, q, r);
        }
    }

    static class PMergeAux extends RecursiveAction {
        private final double[] A;
        private final double[] B;
        private final int p3;
        private int p1;
        private int r1;
        private int p2;
        private int r2;

        public PMergeAux(double[] A, int p1, int r1, int p2, int r2, double[] B, int p3) {
            this.A = A;
            this.p1 = p1;
            this.r1 = r1;
            this.p2 = p2;
            this.r2 = r2;
            this.B = B;
            this.p3 = p3;
        }

        @Override
        protected void compute() {
            if (p1 > r1 && p2 > r2) {
                return;
            }

            if (r1 - p1 < r2 - p2) {
                int temp = p1;
                p1 = p2;
                p2 = temp;

                temp = r1;
                r1 = r2;
                r2 = temp;
            }

            int q1 = (p1 + r1) / 2;
            double x = A[q1];
            int q2 = findSplitPoint(A, p2, r2, x);
            int q3 = p3 + (q1 - p1) + (q2 - p2);
            B[q3] = x;

            PMergeAux child1 = new PMergeAux(A, p1, q1 - 1, p2, q2 - 1, B, p3);
            PMergeAux child2 = new PMergeAux(A, q1 + 1, r1, q2, r2, B, q3 + 1);

            invokeAll(child1, child2);
        }
    }

    public static void insertionSort(double[] A, int n) {
        for (int i = 1; i < n; i++) {
            double key = A[i];
            int j = i - 1;
            while (j >= 0 && A[j] > key) {
                A[j + 1] = A[j];
                j = j - 1;
            }
            A[j + 1] = key;
        }
    }
}
