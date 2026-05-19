// 학번: 22212046 이름: 안효원
package Greedy;

import java.util.Arrays;

class Solution2 {
    // 그리디: 진출 지점 기준으로 정렬 후 최소 카메라 설치
    public int solution(int[][] routes) {
        // 진출 지점(routes[i][1]) 기준 오름차순 정렬
        Arrays.sort(routes, (a, b) -> a[1] - b[1]);

        int answer = 0;
        int camera = Integer.MIN_VALUE; // 마지막 카메라 설치 위치

        for (int[] route : routes) {
            // 현재 차량의 진입 지점이 마지막 카메라 위치보다 크면 카메라 추가 설치
            if (route[0] > camera) {
                answer++;
                camera = route[1]; // 진출 지점에 카메라 설치
            }
        }

        return answer;
    }
}

public class HW2 {
    public static void main(String[] args) {
        Solution2 sol = new Solution2();
        int[][] routes = {{-20, -15}, {-14, -5}, {-18, -13}, {-5, -3}};
        System.out.println(sol.solution(routes)); // 2
    }
}
