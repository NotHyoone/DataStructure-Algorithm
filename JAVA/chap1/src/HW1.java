// 22212046 안효원

// 정수 배열 numbers 주어짐
// numbers에서 서로 다른 인덱스에 있는 두 개의 수를 뽑아
// 더해서 만들 수 있는 모든 수를 배열에 오름차순으로 담아
// return 하도록 solution 함수 완성하기
// 제한사항
// numbers 길이 2이상 100이하
// numbers 모든 수 0이상 100이하
import java.util.Arrays;

import static java.util.Arrays.*;

public class HW1 {
    public static void main(String[] args) {
        Solution1 S = new Solution1();
//        int[] numbers = {2, 1, 3, 4, 1};
        int[] numbers = {5, 0, 2, 7};
        System.out.println("입력 = " + Arrays.toString(numbers));
        System.out.println("출력 = " + Arrays.toString(S.solution(numbers)));
    }
}


class Solution1 {
    public int[] solution(int[] numbers) {
        int[] answer = {};  // 결과를 저장할 배열 초기화
        // 이중 반복 돌면서 모든 경우의 수 합 구하기
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                int sum = numbers[i] + numbers[j];  // 두 수의 합 계산
                // 중복 제거
                boolean exists = false;

                // for-each 문 사용
                for (int k : answer) {
                    if (k == sum) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    // sum이 answer 배열에 존재하지 않으면 추가
                    answer = Arrays.copyOf(answer, answer.length + 1);
                    answer[answer.length - 1] = sum;
                }
            }
        }
        // 결과 배열을 오름차순으로 정렬
        Arrays.sort(answer);    // Java의 내장 정렬 메서드 사용

        return answer;
    }
}