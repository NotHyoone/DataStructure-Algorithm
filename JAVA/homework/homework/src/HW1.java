// 22212046 안효원

import java.util.*;

class Solution1 {
    private int[][] prefixSum;

    public int[] solution(int[][] arr) {
        int n = arr.length;
        int[] result = new int[2];

        // 전처리: 누적합 계산
        prefixSum = new int[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                prefixSum[i][j] = arr[i-1][j-1] + prefixSum[i-1][j] + prefixSum[i][j-1] - prefixSum[i-1][j-1];
            }
        }

        compress(arr, 0, 0, n, result);

        return result;
    }

    private void compress(int[][] arr, int row, int col, int size, int[] result) {
        if (isUniform(row, col, size)) {
            result[arr[row][col]]++;
            return;
        }
        int halfSize = size/2;
        compress(arr, row, col, halfSize, result);  // Top-left
        compress(arr, row, col+halfSize, halfSize, result);  // Top-right
        compress(arr, row+halfSize, col, halfSize, result);  // Bottom-left
        compress(arr, row+halfSize, col+halfSize, halfSize, result);  // Bottom-right
    }

    private boolean isUniform(int row, int col, int size) {
        int sum = prefixSum[row + size][col + size]
                - prefixSum[row][col + size]
                - prefixSum[row + size][col]
                + prefixSum[row][col];

        return sum == 0 || sum == size * size;
    }
}

public class HW1 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        Solution1 sol = new Solution1();
        int[][] arr = {{1,1,0,0}, {1,0,0,0}, {1,0,0,1}, {1,1,1,1}};
        int[][] arr2 = {{1,1,1,1,1,1,1,1}, {0,1,1,1,1,1,1,1}, {0,0,0,0,1,1,1,1}, {0,1,0,0,1,1,1,1},
                {0,0,0,0,0,0,1,1}, {0,0,0,0,0,0,0,1}, {0,0,0,0,1,0,0,1}, {0,0,0,0,1,1,1,1}};

        int[] result = sol.solution(arr);
        int[] result2 = sol.solution(arr2);

        System.out.println(Arrays.toString(result));
        System.out.println(Arrays.toString(result2));
    }
}
