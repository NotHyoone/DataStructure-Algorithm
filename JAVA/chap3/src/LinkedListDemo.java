import java.util.LinkedList;

public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList<String> ll = new LinkedList<>();

        ll.add("F"); ll.add("B"); ll.add("D"); ll.add("E"); ll.add("C");    // Collection
        ll.addLast("Z"); ll.addFirst("A"); // Deque
        ll.add(1, "A2");    // List
        System.out.println("Original contents of ll: " + ll);

        ll.remove("F");     ll.remove(2);   // LinkedList에서 원소 삭제 Collection, List
        System.out.println("Contents of ll after deletion: " + ll);

        ll.removeFirst();   ll.removeLast();    // 처음과 마지막 원소 삭제:Deque
        System.out.println();

        String val = ll.get(2); // 순차검색 List
        ll.set(2, val + "Changed"); // List
        System.out.println("ll after change: "+ll);
    }
}
