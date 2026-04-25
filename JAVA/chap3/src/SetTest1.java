import java.util.HashSet;
import java.util.Set;

public class SetTest1 {
    public static void main(String[] args) {
        Set<String> s1 = new HashSet<>();
        Set<String> s2 = new HashSet<>();

        // Collection 메소드
        s1.add("A");    s1.add("B"); s1.add("C");
        s2.add("A");    s2.add("D");

        Set<String> union = new HashSet<>(s1);
        union.addAll(s2);

        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);

        System.out.println("합집합 "+ union);
        System.out.println("교집합 "+intersection);
    }
}
