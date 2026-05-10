package DC_DP;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bin {
    public static int bin(int n, int k) {
        if (k == 0 || k == n) return 1;
        else return bin(n-1, k-1) + bin(n-1, k);
    }

    public static int bin2(int n, int k) {
        int[][] dp = new int[n+1][k+1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= Math.min(i, k); j++) {
                if (j == 0 || j == i) dp[i][j] = 1;
                else dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
            }
        }

        return dp[n][k];
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter n and k: ");
        String[] input = br.readLine().split(" ");
        int n = Integer.parseInt(input[0]);
        int k = Integer.parseInt(input[1]);
        long startTime = System.currentTimeMillis();
        System.out.println("C(" + n + ", " + k + ") = " + bin(n, k));
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        System.out.println("C(" + n + ", " + k + ") = " + bin2(n, k));
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }
}
