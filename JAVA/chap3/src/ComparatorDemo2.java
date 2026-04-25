import java.util.TreeSet;

public class ComparatorDemo2 {
    public static void main(String[] args) {
        MyComp mc = new MyComp();
        TreeSet<String> ts = new TreeSet<>(mc.reversed());

        ts.add("C"); ts.add("A");
        ts.add("B"); ts.add("E");
        ts.add("F"); ts.add("D");

        System.out.println(ts);
    }
}
