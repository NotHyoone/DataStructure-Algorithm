package Exam;

public class ExamProblem21MatrixLongestPath {
    public int maxPathSum(int[][] a) {
        validate(a);
        int rows = a.length;
        int cols = a[0].length;
        int[][] dp = new int[rows][cols];

        dp[0][0] = a[0][0];
        for (int col = 1; col < cols; col++) {
            dp[0][col] = dp[0][col - 1] + a[0][col];
        }
        for (int row = 1; row < rows; row++) {
            dp[row][0] = dp[row - 1][0] + a[row][0];
        }

        for (int row = 1; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                dp[row][col] = Math.max(dp[row - 1][col], dp[row][col - 1]) + a[row][col];
            }
        }

        return dp[rows - 1][cols - 1];
    }

    private void validate(int[][] a) {
        if (a == null || a.length == 0 || a[0] == null || a[0].length == 0) {
            throw new IllegalArgumentException("Matrix must be non-empty.");
        }
        int cols = a[0].length;
        for (int row = 1; row < a.length; row++) {
            if (a[row] == null || a[row].length != cols) {
                throw new IllegalArgumentException("Matrix must be rectangular.");
            }
        }
    }
}
