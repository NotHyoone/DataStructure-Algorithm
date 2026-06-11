// 학번: 22212046 이름: 안효원
package Greedy;

import java.util.Arrays;

class GreedyHomework2 {
    // 그리디: 진출 지점 기준으로 정렬 후 최소 카메라 설치
    public int solution(int[][] routes) {
        int answer = 0;

        Arrays.sort(routes, (a, b) -> Integer.compare(a[1], b[1])); // 진출 지점 기준으로 정렬

        int camera = -30001; // 초기 카메라 위치 (최소 진출 지점보다 작은 값)

        // 각 차량의 진입 지점과 진출 지점을 확인하면서 카메라 설치
        for (int i = 0; i < routes.length; i++) {
            int start = routes[i][0];
            int end = routes[i][1];

            // 현재 카메라 위치가 진입 지점보다 작으면 새로운 카메라 설치
            // 이 차량의 진출 시점에 새 카메라를 설치한다.
            if (camera < start) {
                camera = end;
                answer++;
            }
        }

        return answer;
    }
}
