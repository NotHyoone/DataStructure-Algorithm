import java.util.Comparator;

public class MyComp implements Comparator<String> {
    public int compare(String aStr, String bStr) {
        return bStr.compareTo(aStr);    // 역순으로 정렬
    }
}
