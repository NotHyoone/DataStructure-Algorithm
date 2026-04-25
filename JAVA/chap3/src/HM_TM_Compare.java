import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.lang.Integer;

public class HM_TM_Compare {
    public static void main(String[] args) {
        HashMap<Double, Integer> hm = new HashMap<>();
        TreeMap<Double, Integer> tm = new TreeMap<>();

        ArrayList<Double> keys = new ArrayList<>();
        for(int i = 0; i<1000000; i++) {
            keys.add(Math.random());
        }
        long start = System.currentTimeMillis();
        for(Double k : keys)
            hm.put(k, 1);
        long end = System.currentTimeMillis();
        System.out.println("HashMap : 삽입 시간 = " + (end - start));

        start = System.currentTimeMillis();
        for(Double k : keys)
            tm.put(k, 1);
        end = System.currentTimeMillis();
        System.out.println("TreeMap : 삽입 시간 = " + (end - start));

        start = System.currentTimeMillis();
        for(Double k : keys)
            hm.get(k);
        end = System.currentTimeMillis();
        System.out.println("HashMap : 검색 시간 = " + (end - start));

        start = System.currentTimeMillis();
        for(Double k : keys)
            tm.get(k);
        end = System.currentTimeMillis();
        System.out.println("TreeMap : 검색 시간 = " + (end - start));
    }
}
