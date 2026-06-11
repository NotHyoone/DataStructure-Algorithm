package Exam;

public class ExamProblem06KnapsackRecursive {
    public int knapsack(int[] weight, int[] profit, int n, int capacity) {
        validate(weight, profit, n, capacity);
        return solve(weight, profit, n, capacity);
    }

    private int solve(int[] weight, int[] profit, int i, int capacity) {
        if (i == 0 || capacity == 0) {
            return 0;
        }
        if (weight[i] > capacity) {
            return solve(weight, profit, i - 1, capacity);
        }

        int skip = solve(weight, profit, i - 1, capacity);
        int take = profit[i] + solve(weight, profit, i - 1, capacity - weight[i]);
        return Math.max(skip, take);
    }

    /*
     * DP 관계식:
     * P[i][w] = P[i - 1][w]                                  if weight[i] > w
     * P[i][w] = max(P[i - 1][w], profit[i] + P[i - 1][w - weight[i]])
     *                                                     if weight[i] <= w
     */
    private void validate(int[] weight, int[] profit, int n, int capacity) {
        if (weight == null || profit == null || weight.length != profit.length) {
            throw new IllegalArgumentException("weight and profit arrays must be non-null and same length.");
        }
        if (n < 0 || n >= weight.length || capacity < 0) {
            throw new IllegalArgumentException("Invalid n or capacity.");
        }
    }
}
