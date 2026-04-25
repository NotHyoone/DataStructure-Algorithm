import java.lang.Integer;
import java.util.ArrayList;

public class ArrayListToArray {
    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<>();

        al.add(1); al.add(2); al.add(3); al.add(4);

        System.out.println("Contents of al: " + al);

        Integer[] ia = new Integer[al.size()];
        ia = al.toArray(ia);

        int sum = 0;
        for(Integer i: ia)
            sum += i;
        System.out.println("Sum of elements: " + sum);
        System.out.println("Sum of elements in ia: " + sum);
    }
}
