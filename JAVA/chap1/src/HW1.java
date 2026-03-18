import java.util.Arrays;

// 22212046 안효원
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
        // numbers배열에서 서로 다른 두 개의 정수 뽑아 더하기
        int[] answer = new int[0];
        int N = numbers.length;
        // selection sort
        for (int i = 0; i < N-1; i++) {
            for (int j = i+1; j < N; j++) {
                if (numbers[j] )
            }
        }


    }
}