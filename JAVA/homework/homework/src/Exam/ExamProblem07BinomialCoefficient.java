package Exam;

public class ExamProblem07BinomialCoefficient {
    public int recursiveBin(int n, int k) {
        validate(n, k);
        if (k == 0 || n == k) {
            return 1;
        }
        return recursiveBin(n - 1, k - 1) + recursiveBin(n - 1, k);
    }

    public int dpBin(int n, int k) {
        validate(n, k);
        int[][] b = new int[n + 1][k + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, k); j++) {
                if (j == 0 || j == i) {
                    b[i][j] = 1;
                } else {
                    b[i][j] = b[i - 1][j - 1] + b[i - 1][j];
                }
            }
        }

        return b[n][k];
    }

    private void validate(int n, int k) {
        if (n < 0 || k < 0 || k > n) {
            throw new IllegalArgumentException("Require 0 <= k <= n.");
        }
    }
}
