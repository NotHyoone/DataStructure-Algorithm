// 22212046 안효원

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

// 수직선 위에 N개의 좌표 X1, X2, ..., XN이 있다. 이 좌표에 좌표 압축을 적용하
// 려고 한다. Xp를 좌표 압축한 결과 X'p의 값은 Xp > Xq를 만족하는 서로 다른
// 좌표 Xq의 개수와 같아야 한다. X1, X2, ..., XN에 좌표 압축을 적용한 결과 X'1,
// X'2, ..., X'N를 출력해보자.
// 제한사항
// N은 1 이상 1,000,000 이하이다.
// -10^9 <= Xp <= 10^9
public class HW3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++)
            A[i] = sc.nextInt();
        sc.close();

        // A의 압축 결과 계산해서 출력

        int[] B = new int[n];
        System.arraycopy(A, 0, B, 0, n);   // 배열 복사
        Arrays.sort(B);  // B를 오름차순으로 정렬
        HashMap<Integer, Integer> rank = new HashMap<>();  // 원소와 그 원소보다 작은 원소의 개수 매핑
        int rankCount = 0;
        for (int i = 0; i < n; i++) {
            if (!rank.containsKey(B[i])) {
                rank.put(B[i], rankCount++);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(rank.get(A[i]));
            if (i < n - 1) sb.append(" ");
        }
        System.out.println(sb);
    }
}
