// 22212046 안효원

import java.util.ArrayList;
import java.util.Scanner;

class Calc {
    public static int calc(int N) {
        int[] dp = new int[N+1];
        int[] op = new int[N+1];
        dp[1] = 0;

        for (int i = 2; i <= N; i++) {
            dp[i] = dp[i-1]+1;
            op[i] = -1; // 1을 뺀 연산 값을 기록

            if (i % 2 == 0 && dp[i/2] + 1 < dp[i]) {
                dp[i] = dp[i/2] + 1;
                op[i] = 2; // 2로 나눈 연산 값을 기록
            }
            if (i % 3 == 0 && dp[i/3] + 1 < dp[i]) {
                dp[i] = dp[i/3] + 1;
                op[i] = 3; // 3으로 나눈 연산 값을 기록
            }
            if (i % 5 == 0 && dp[i/5] + 1 < dp[i]) {
                dp[i] = dp[i/5] + 1;
                op[i] = 5; // 5로 나눈 연산 값을 기록
            }
        }
        ArrayList<Integer> path = new ArrayList<>();
        int cur = N;
        while (cur > 1) {
            path.add(cur);
            if (op[cur] == -1) {
                cur -= 1;
            } else {
                cur /= op[cur];
            }
        }
        path.add(1);

        // 출력
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) System.out.print(" -> ");
        }
        System.out.println();

        return dp[N];
    }
}

public class HW4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("N =  ");
        int N = sc.nextInt();
        int result = Calc.calc(N);
        System.out.println("연산의 최솟값 = " + result);

    }
}
