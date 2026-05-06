package DC_DP;

import java.util.Random;
import java.util.Arrays;

public class MatrixMultBenchmark {
    private static final int DEFAULT_MIN_EXP = 1;
    private static final int DEFAULT_MAX_EXP = 12;
    private static final int DEFAULT_WARMUP_ITERS = 2;
    private static final int DEFAULT_MEASURE_ITERS = 5;
    private static final int DEFAULT_THRESHOLD = 32;
    private static final int DEFAULT_VALUE_BOUND = 100;
    private static final long DEFAULT_SEED = 42L;

    public static void main(String[] args) {
        int minExp = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_MIN_EXP;
        int maxExp = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_MAX_EXP;
        int warmupIters = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_WARMUP_ITERS;
        int measureIters = args.length > 3 ? Integer.parseInt(args[3]) : DEFAULT_MEASURE_ITERS;
        int threshold = args.length > 4 ? Integer.parseInt(args[4]) : DEFAULT_THRESHOLD;
        int valueBound = args.length > 5 ? Integer.parseInt(args[5]) : DEFAULT_VALUE_BOUND;

        if (minExp < 0 || maxExp < minExp || warmupIters < 0 || measureIters <= 0 || threshold <= 0 || valueBound <= 0) {
            System.out.println("Usage: java DC_DP.MatrixMultBenchmark [minExp maxExp warmupIters measureIters threshold valueBound]");
            System.out.println("Example: java DC_DP.MatrixMultBenchmark 1 12 2 5 64 100");
            return;
        }

        MatrixMult mm = new MatrixMult(threshold);

        System.out.println("=== Matrix Multiplication Benchmark ===");
        System.out.printf("n = 2^k, k in [%d, %d], warmup=%d, measure=%d, threshold=%d, valueBound=%d\n",
            minExp, maxExp, warmupIters, measureIters, threshold, valueBound);
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%8s %14s %14s %10s %10s\n", "n", "matrix(ms)", "strassen(ms)", "speedup", "verified");
        System.out.println("--------------------------------------------------------------------------");

        for (int exp = minExp; exp <= maxExp; exp++) {
            int n = 1 << exp;

            int[][] A = randomMatrix(n, valueBound, DEFAULT_SEED + n * 31L);
            int[][] B = randomMatrix(n, valueBound, DEFAULT_SEED + n * 67L);

            int[][] naiveResult = new int[n][n];
            int[][] strassenResult = new int[n][n];

            for (int i = 0; i < warmupIters; i++) {
                mm.matrixmult(n, A, B, naiveResult);
                mm.strassen(n, A, B, strassenResult);
            }

            long[] naiveTimes = new long[measureIters];
            long[] strassenTimes = new long[measureIters];

            for (int i = 0; i < measureIters; i++) {
                clearMatrix(naiveResult);
                long start = System.nanoTime();
                mm.matrixmult(n, A, B, naiveResult);
                long end = System.nanoTime();
                naiveTimes[i] = end - start;

                clearMatrix(strassenResult);
                start = System.nanoTime();
                mm.strassen(n, A, B, strassenResult);
                end = System.nanoTime();
                strassenTimes[i] = end - start;
            }

            boolean verified = equalsMatrix(naiveResult, strassenResult);
            double naiveAvgMs = avgMs(naiveTimes);
            double strassenAvgMs = avgMs(strassenTimes);
            double speedup = strassenAvgMs > 0.0 ? naiveAvgMs / strassenAvgMs : Double.NaN;

            System.out.printf("%8d %14.3f %14.3f %10.2f %10s\n",
                n,
                naiveAvgMs,
                strassenAvgMs,
                speedup,
                verified ? "OK" : "FAIL");
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Tip: increase measureIters for stabler results.");
    }

    private static int[][] randomMatrix(int n, int valueBound, long seed) {
        Random random = new Random(seed);
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = random.nextInt(valueBound);
            }
        }
        return m;
    }

    private static void clearMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            Arrays.fill(m[i], 0);
        }
    }

    private static boolean equalsMatrix(int[][] a, int[][] b) {
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (a[i].length != b[i].length) {
                return false;
            }
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static double avgMs(long[] timesNs) {
        long sum = 0L;
        for (long timeNs : timesNs) {
            sum += timeNs;
        }
        return (sum / (double) timesNs.length) / 1_000_000.0;
    }
}

