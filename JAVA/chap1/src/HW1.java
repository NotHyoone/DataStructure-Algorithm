// 22212046 안효원
import java.util.Arrays;

public class HW1 {
    public static void main(String[] args) {
        Solution1 S = new Solution1();
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

                for (int k = 0; k < answer.length; k++) {
                    if (answer[k] == sum) {
                        exists = true;  // 이미 존재하면 exists를 true로 설정
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

        // 선택 정렬 구현
//        for (int i = 0; i < answer.length - 1; i++) {
//            int minIndex = i;
//            for (int j = i + 1; j < answer.length; j++) {
//                if (answer[j] < answer[minIndex]) {
//                    minIndex = j;
//                }
//            }
//            int temp = answer[i];
//            answer[i] = answer[minIndex];
//            answer[minIndex] = temp;
//        }

        // 삽입 정렬 구현
        for (int i = 1; i < answer.length; i++) {
            for (int j = i; j > 0 && answer[j] < answer[j-1]; j--) {
                int temp = answer[j];
                answer[j] = answer[j-1];
                answer[j-1] = temp;
            }
        }

        return answer;
    }
}