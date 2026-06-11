package Exam;

public class ExamProblem20MaxSubList {
    public double maxSubList(double[] a, int n) {
        if (a == null || n <= 0 || n > a.length) {
            throw new IllegalArgumentException("Require 0 < n <= a.length.");
        }

        double[] bestEndingAt = new double[n];
        double max = bestEndingAt[0] = a[0];

        for (int i = 1; i < n; i++) {
            bestEndingAt[i] = Math.max(a[i], bestEndingAt[i - 1] + a[i]);
            max = Math.max(max, bestEndingAt[i]);
        }

        return max;
    }
}
