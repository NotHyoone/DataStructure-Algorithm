package DC_DP;// 22212046 안효원
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class DC_DP_Homework2 {
    static int n, k;
    static int[] selected;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("n과 k를 입력하세요: ");
        n = sc.nextInt();
        k = sc.nextInt();

        selected = new int[k];
        combination(1, 0);
    }

    static void combination(int start, int depth) {
        if (depth == k) {
            System.out.print(Arrays.toString(selected) + " ");
            return;
        }

        for (int i = start; i <= n; i++) {
            selected[depth] = i;
            combination(i + 1, depth + 1);
        }
    }
}