// 22212046 안효원

import java.util.Scanner;

class Calc {
    public static int calc(int N) {
        int[] dp = new int[N+1];
        dp[1] = 0;

        for (int i = 2; i <= N; i++) {
            dp[i] = dp[i-1]+1;

            if (i % 2 == 0)
                dp[i] = Math.min(dp[i], dp[i/2] + 1);
            if (i % 3 == 0)
                dp[i] = Math.min(dp[i], dp[i/3] + 1);
            if (i % 5 == 0)
                dp[i] = Math.min(dp[i], dp[i/5] + 1);
        }
        return dp[N];
    }
}

public class HW4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        System.out.print("정수 N을 입력? ");
        int N = sc.nextInt();
        int result = Calc.calc(N);
        System.out.println("최소 연산 횟수: " + result);

    }
}
