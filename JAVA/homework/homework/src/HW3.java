// 22212046 안효원

class Solution3 {
    public int solution(int[][] triangle) {
        if (triangle == null || triangle.length == 0)
            return 0;

        int n = triangle.length;
        int[] dp = new int[n];

        // 수동으로 마지막 행을 dp 배열에 복사
        System.arraycopy(triangle[n - 1], 0, dp, 0, triangle[n - 1].length);
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < triangle[i].length; j++) {
                dp[j] = triangle[i][j] + Math.max(dp[j], dp[j+1]);
            }
        }
        return dp[0];
    }
}

public class HW3 {
    public static void main(String[] args) {
        Solution3 sol = new Solution3();
        // 테스트 케이스
        int[][] triangle1 = {
                {7},
                {3, 8},
                {8, 1, 0},
                {2, 7, 4, 4},
                {4, 5, 2, 6, 5}
        };
        System.out.println("Test 1: " + sol.solution(triangle1));

        int[][] triangle2 = {
                {1},
                {2, 3},
                {4, 5, 6}
        };
        System.out.println("Test 2: " + sol.solution(triangle2));

        int[][] triangle3 = {
                {5},
                {5, 5},
                {5, 5, 5}
        };
        System.out.println("Test 3: " + sol.solution(triangle3));
    }
}
