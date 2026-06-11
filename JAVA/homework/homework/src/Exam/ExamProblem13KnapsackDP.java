package Exam;

public class ExamProblem13KnapsackDP {
    public int knapsack(int n, int capacity, int[] profit, int[] weight) {
        validate(n, capacity, profit, weight);
        int[][] p = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weight[i] > w) {
                    p[i][w] = p[i - 1][w];
                } else {
                    p[i][w] = Math.max(p[i - 1][w], profit[i] + p[i - 1][w - weight[i]]);
                }
            }
        }

        return p[n][capacity];
    }

    /*
     * 시간 복잡도는 O(nW), 공간 복잡도도 O(nW)이다.
     * 입력 크기를 숫자의 자릿수로 보면 W가 클 때 매우 커질 수 있으므로
     * 이 알고리즘은 pseudo-polynomial time algorithm이다.
     */
    private void validate(int n, int capacity, int[] profit, int[] weight) {
        if (profit == null || weight == null || profit.length != weight.length) {
            throw new IllegalArgumentException("profit and weight arrays must be non-null and same length.");
        }
        if (n < 0 || n >= profit.length || capacity < 0) {
            throw new IllegalArgumentException("Invalid n or capacity.");
        }
    }
}
