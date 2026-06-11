package Exam;

import java.util.Arrays;

public class ExamProblem01JobScheduling {
    private final int[] t;
    private final int[] p;
    private final int[] d;
    private int[][] memo;

    public ExamProblem01JobScheduling(int[] t, int[] p, int[] d) {
        if (t == null || p == null || d == null || t.length != p.length || p.length != d.length) {
            throw new IllegalArgumentException("t, p, d arrays must be non-null and have the same length.");
        }
        this.t = t;
        this.p = p;
        this.d = d;
    }

    public int dcMax(int n, int dn) {
        validateInput(n, dn);
        memo = new int[n + 1][dn + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1); //
        }
        return dc(n, dn);
    }

    private int dc(int i, int time) {
        if (i == 0 || time <= 0) {
            return 0;
        }
        if (memo[i][time] != -1) {
            return memo[i][time];
        }

        int skip = dc(i - 1, time);
        int finishLimit = Math.min(time, d[i]);
        if (finishLimit < t[i]) {
            memo[i][time] = skip;
            return skip;
        }

        int take = dc(i - 1, finishLimit - t[i]) + p[i];
        memo[i][time] = Math.max(skip, take);
        return memo[i][time];
    }

    public int dpMax(int n, int dn) {
        validateInput(n, dn);
        int[][] profit = new int[n + 1][dn + 1];

        for (int i = 1; i <= n; i++) {
            for (int time = 0; time <= dn; time++) {
                int skip = profit[i - 1][time];
                int finishLimit = Math.min(time, d[i]);
                if (finishLimit < t[i]) {
                    profit[i][time] = skip;
                    continue;
                }

                int take = profit[i - 1][finishLimit - t[i]] + p[i];
                profit[i][time] = Math.max(skip, take);
            }
        }
        return profit[n][dn];
    }

    private void validateInput(int n, int dn) {
        if (n < 0 || n >= t.length) {
            throw new IllegalArgumentException("n must be between 0 and t.length - 1.");
        }
        if (dn < 0) {
            throw new IllegalArgumentException("dn must be non-negative.");
        }
    }
}
