// 학번: 22212046 이름: 안효원
package Greedy;

import java.util.Stack;

class Solution1 {
    // 그리디: 스택을 이용해 앞자리부터 더 큰 수를 만들기 위해 작은 수를 제거
    public String solution(String number, int k) {
        Stack<Character> stack = new Stack<>();
        int removed = 0;

        for (char c : number.toCharArray()) {
            // 스택 최상단보다 현재 수가 크고 아직 제거 가능하면 pop
            while (removed < k && !stack.isEmpty() && stack.peek() < c) {
                stack.pop();
                removed++;
            }
            stack.push(c);
        }

        // k개를 다 제거 못 했으면 뒤에서 제거
        while (removed < k) {
            stack.pop();
            removed++;
        }

        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }
        return sb.toString();
    }
}

public class HW1 {
    public static void main(String[] args) {
        Solution1 sol = new Solution1();
        System.out.println(sol.solution("1924", 2));          // 94
        System.out.println(sol.solution("1231234", 3));       // 3234
        System.out.println(sol.solution("4177252841", 4));    // 775841
        System.out.println(sol.solution("9097654321", 3));    // 9976543
    }
}
