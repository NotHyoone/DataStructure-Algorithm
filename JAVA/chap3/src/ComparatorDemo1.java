import java.util.TreeSet;

public class ComparatorDemo1 {
    public static void main(String[] args) {
        TreeSet<String> ts = new TreeSet<>(new MyComp());    // MyComp 객체를 이용해 TreeSet 생성

        ts.add("C"); ts.add("A");
        ts.add("B"); ts.add("E");
        ts.add("F"); ts.add("D");

        System.out.println(ts);
    }
}

