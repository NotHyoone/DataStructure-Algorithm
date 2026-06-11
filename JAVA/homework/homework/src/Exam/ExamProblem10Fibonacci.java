package Exam;

public class ExamProblem10Fibonacci {
    public int recursiveFib(int n) {
        validate(n);
        if (n <= 1) {
            return n;
        }
        return recursiveFib(n - 1) + recursiveFib(n - 2);
    }

    public int dpFib(int n) {
        validate(n);
        if (n <= 1) {
            return n;
        }

        int[] f = new int[n + 1];
        f[0] = 0;
        f[1] = 1;

        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }
        return f[n];
    }

    /*
     * recursiveFib(10)은 recursiveFib(8), recursiveFib(7) 같은 같은 부분 문제를
     * 반복 계산한다. dpFib는 f[0]부터 f[n]까지 한 번씩만 계산하므로 O(n)에 끝난다.
     */
    private void validate(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative.");
        }
    }
}
