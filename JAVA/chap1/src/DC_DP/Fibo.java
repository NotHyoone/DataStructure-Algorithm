package DC_DP;

public class Fibo {
    // recursive
    public static int fibo(int n) {
        if (n <= 1)
            return n;
        else
            return fibo(n-1) + fibo(n-2);
    }
    // dynamic programming
    public static int fibo2(int n) {
        if (n <= 1) return n;

        int[] dp = new int[n+1];

        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }

    public static void main(String[] args) {
        int n = 10;
        System.out.println("Fibonacci(" + n + ") = " + fibo(n));
        System.out.println("Fibonacci(" + n + ") = " + fibo2(n));
    }
}

