package Exam;

public class ExamProblem05EditDistance {
    public int recursiveDistance(String x, String y, int insertCost, int deleteCost, int updateCost) {
        validate(x, y, insertCost, deleteCost, updateCost);
        return recursive(x, y, x.length(), y.length(), insertCost, deleteCost, updateCost);
    }

    private int recursive(String x, String y, int i, int j, int insertCost, int deleteCost, int updateCost) {
        if (i == 0) {
            return j * insertCost;
        }
        if (j == 0) {
            return i * deleteCost;
        }

        int delete = recursive(x, y, i - 1, j, insertCost, deleteCost, updateCost) + deleteCost;
        int insert = recursive(x, y, i, j - 1, insertCost, deleteCost, updateCost) + insertCost;
        int replaceCost = x.charAt(i - 1) == y.charAt(j - 1) ? 0 : updateCost;
        int replace = recursive(x, y, i - 1, j - 1, insertCost, deleteCost, updateCost) + replaceCost;

        return Math.min(Math.min(delete, insert), replace);
    }

    public int dpDistance(String x, String y, int insertCost, int deleteCost, int updateCost) {
        validate(x, y, insertCost, deleteCost, updateCost);
        int n = x.length();
        int m = y.length();
        int[][] distance = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            distance[i][0] = i * deleteCost;
        }
        for (int j = 1; j <= m; j++) {
            distance[0][j] = j * insertCost;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int delete = distance[i - 1][j] + deleteCost;
                int insert = distance[i][j - 1] + insertCost;
                int replaceCost = x.charAt(i - 1) == y.charAt(j - 1) ? 0 : updateCost;
                int replace = distance[i - 1][j - 1] + replaceCost;
                distance[i][j] = Math.min(Math.min(delete, insert), replace);
            }
        }

        return distance[n][m];
    }

    private void validate(String x, String y, int insertCost, int deleteCost, int updateCost) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("Strings must be non-null.");
        }
        if (insertCost < 0 || deleteCost < 0 || updateCost < 0) {
            throw new IllegalArgumentException("Costs must be non-negative.");
        }
    }
}
