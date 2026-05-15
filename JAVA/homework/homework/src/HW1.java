// 22212046 안효원

import java.util.*;

class Solution1 {
    public int[] solution(int [][] arr) {
        int[] answer = new int[2];
        compress(arr, 0, 0, arr.length, answer);
        return answer;
    }

    private void compress(int[][] arr, int row, int col, int size, int[] result) {
        if (isUniform(arr, row, col, size)) {
            result[arr[row][col]]++;
            return;
        }
        int halfSize = size / 2;
        compress(arr, row, col, halfSize, result);                          // Top-left
        compress(arr, row, col + halfSize, halfSize, result);               // Top-right
        compress(arr, row + halfSize, col, halfSize, result);               // Bottom-left
        compress(arr, row + halfSize, col + halfSize, halfSize, result);    // Bottom-right
    }

    private boolean isUniform(int[][] arr, int row, int col, int size) {
        int value = arr[row][col];
        for (int i = row; i < row + size; i++) {
            for (int j = col; j < col + size; j++) {
                if (arr[i][j] != value) {
                    return false;
                }
            }
        }
        return true;
    }
}

public class HW1 {
    public static void main(String[] args) {
        Solution1 sol = new Solution1();
        int[][] arr = {
                {1,1,0,0},
                {1,0,0,0},
                {1,0,0,1},
                {1,1,1,1}
        };
        int[][] arr2 = {
                {1,1,1,1,1,1,1,1},
                {0,1,1,1,1,1,1,1},
                {0,0,0,0,1,1,1,1},
                {0,1,0,0,1,1,1,1},
                {0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,0,1},
                {0,0,0,0,1,0,0,1},
                {0,0,0,0,1,1,1,1}
        };
        int[] result = sol.solution(arr);
        int[] result2 = sol.solution(arr2);
        System.out.println(Arrays.toString(result));
        System.out.println(Arrays.toString(result2));
    }
}