// 학번: 22212046 이름: 안효원
package Greedy;

// 큰 수 만들기
class GreedyHomework1 {

    public String solution(String number, int k) {
        StringBuilder stack = new StringBuilder();
        int removeCount = k;    // 제거할 수 있는 숫자의 개수

        for (int i = 0; i < number.length(); i++) {
            char current = number.charAt(i);

            // 스택이 비어있지 않고, 현재 숫자가 스택의 마지막 숫자보다 크고, 제거할 수 있는 숫자가 남아있다면 제거
            while (stack.length() > 0 && removeCount > 0 && stack.charAt(stack.length() - 1) < current) {
                stack.deleteCharAt(stack.length() - 1);
                removeCount--;
            }
            stack.append(current);
        }

        return stack.substring(0, stack.length() - removeCount); // 남은 제거할 숫자만큼 뒤에서 제거
    }
}