import java.util.ArrayList;
import java.lang.Integer;

public class ForEachDemo {
    public static void main(String[] args) {
        ArrayList<Integer> vals = new ArrayList<>();
        vals.add(1);
        vals.add(2);
        vals.add(3);
        vals.add(4);
        vals.add(5);

        System.out.print("Original comtents of vals: ");
        for (int v : vals) {    // for loop을 이용해 요소 출력(순방향 순회만 지원 & 수정 불가)
            System.out.print(v + " ");
        }
        System.out.println();

        int sum = 0;
        for (int v : vals) {
            sum += v;
        }
        System.out.println("Sum of vals: " + sum);
    }
}
