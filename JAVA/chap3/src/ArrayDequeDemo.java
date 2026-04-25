import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class ArrayDequeDemo {
    public static void main(String[] args) {
        ArrayDeque<String> adq = new ArrayDeque<>();

        adq.push("A");
        adq.push("B");
        adq.push("D");
        adq.push("E");
        adq.push("F");

        System.out.print("Popping the stack: ");


//        while(adq.peek() != null) {
//            System.out.print(adq.pop() + " ");
//        }
        try {
            while(true) {
                adq.element();
                System.out.print(adq.pop() + " ");
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Stack is empty");
        }

        System.out.println();
    }
}
