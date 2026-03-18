// 22212046 안효원

// 단일 연결 리스트의 시작 노드가 주어질 때, 삽입 정렬 알고리즘을 이용하여
// 연결 리스트를 정렬한 후, 정렬된 연결 리스트의 시작 노드를 반환하는 함수를 작성하라.
// 제한사항
// The number of nodes in the list is in the range[1, 5000]
// -5000 <= Node.val <= 5000
class ListNode {
    int val;    // 값
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class HW2 {
    public static void main(String[] args) {
        ListNode head = null;
        // head를 시작 노드로 하는 연결 리스트 작성
        head = new ListNode(-1);
        head.next = new ListNode(5);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(0);

        Solution2 sol = new Solution2();
        System.out.println("정렬 전");
        printList(head);
        head = sol.insertionSortList(head);
        System.out.println("정렬 후");
        printList(head);
    }

    private static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
        System.out.println();
    }
}

class Solution2 {
    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode dummy = new ListNode(0);   // 정렬 리스트의 가짜 머리
        ListNode curr = head;   // 원본 리스트 순회 포인터

        // curr 값 존재시 진입
        while(curr != null) {
            ListNode next = curr.next;  // 원본 리스트 다음 노드 보관

            // curr가 들어갈 위치 탐색
            ListNode prev = dummy;
            while(prev.next != null && prev.next.val <= curr.val) {
                prev = prev.next;
            }

            // prev와 prev.next 사이에 curr 삽입
            curr.next = prev.next;
            prev.next = curr;

            // 원본 리스트의 다음 노드로 이동
            curr = next;
        }
        return dummy.next;
    }
}