import java.util.ArrayList;
import java.util.List;

public class AVLTreeTestClient {

    private static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T item : iterable) {
            list.add(item);
        }
        return list;
    }

    private static void printState(AVLTree<Integer, String> avl, String label) {
        System.out.println("\n[" + label + "]");
        System.out.println("size     : " + avl.size());
        System.out.println("keys     : " + toList(avl.keys()));
        System.out.println("contains7: " + avl.contains(7));
        System.out.println("get(7)   : " + avl.get(7));
        System.out.println("contains9: " + avl.contains(9));
        System.out.println("get(9)   : " + avl.get(9));
    }

    public static void main(String[] args) {
        AVLTree<Integer, String> avl = new AVLTree<>();

        int[] insertKeys = {10, 20, 30, 40, 50, 25, 5, 7, 3};
        for (int key : insertKeys) {
            avl.put(key, "v" + key);
        }

        printState(avl, "After inserts");

        avl.delete(3);   // leaf delete
        avl.delete(5);   // one-child delete (after deleting 3)
        avl.delete(20);  // two-children delete

        printState(avl, "After deletes (3, 5, 20)");

        // Update existing key and print check
        avl.put(25, "updated");
        System.out.println("\n[Update existing key]");
        System.out.println("get(25)  : " + avl.get(25));

        // Delete missing key should be no-op
        avl.delete(999);
        System.out.println("\n[Delete missing key]");
        System.out.println("keys     : " + toList(avl.keys()));
    }
}

