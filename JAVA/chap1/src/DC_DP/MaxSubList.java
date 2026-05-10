package DC_DP;

import java.util.ArrayList;
import java.util.List;

/**     * 최대 부분 리스트 결과를 저장하는 클래스     */
class MaxSubListResult {
    double maxSum;           // 최대 합
    List<Double> sublist;    // 최대 합을 갖는 부분 리스트의 요소들
    int startIndex;          // 부분 리스트의 시작 인덱스
    int endIndex;            // 부분 리스트의 끝 인덱스

    MaxSubListResult(double maxSum, List<Double> sublist, int startIndex, int endIndex) {
        this.maxSum = maxSum;
        this.sublist = sublist;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}

public class MaxSubList {
    public static MaxSubListResult maxSubList(double[] A, int n) {
        // DP 배열: B[i] = i번째 요소를 포함하는 최대 부분합
        double[] B = new double[n];
        // 각 위치에서 부분 리스트의 시작 인덱스를 추적
        int[] startIndex = new int[n];

        // 최대 합
        double max = B[0] = A[0];
        // 최대 합을 갖는 위치
        int maxIndex = 0;
        // 최대 부분 리스트의 시작 인덱스
        int maxStart = 0;

        startIndex[0] = 0;  // 첫 번째 요소는 인덱스 0에서 시작

        // DP 계산: 강의 자료의 알고리즘
        for (int i = 1; i < n; i++) {
            // B[i-1] <= 0이면 새로 시작, 아니면 이전 누적합에 추가
            if (B[i - 1] <= 0) {
                B[i] = A[i];
                startIndex[i] = i;  // 새로운 시작점
            } else {
                B[i] = B[i - 1] + A[i];
                startIndex[i] = startIndex[i - 1];  // 이전 시작점 유지
            }

            // 현재 누적합이 최댓값보다 크면 업데이트
            if (max < B[i]) {
                max = B[i];
                maxIndex = i;       // 최대값의 끝 인덱스
                maxStart = startIndex[i];  // 최대값의 시작 인덱스
            }
        }

        // 최대 부분 리스트 추출
        List<Double> sublist = new ArrayList<>();
        for (int i = maxStart; i <= maxIndex; i++) {
            sublist.add(A[i]);
        }

        // 결과 객체로 반환
        return new MaxSubListResult(max, sublist, maxStart, maxIndex);
        
    }
    public static void main(String[] args) {
        double[] A = {2.3, 3.2, -4.5, 2.1, -5.3, 3.6, 4.1, -2.3, 3.5, -4.5};
        int n = A.length;

        // 합이 최대인 부분 리스트 구하기
        MaxSubListResult result = maxSubList(A, n);

        // 결과 출력
        System.out.println("=== 합이 최대인 부분 리스트 ===");
        System.out.printf("최대 합: %.1f%n", result.maxSum);
        System.out.println("부분 리스트: " + result.sublist);
        System.out.println("시작 인덱스: " + result.startIndex + ", 끝 인덱스: " + result.endIndex);
    }
}
