import java.util.HashMap;
import java.lang.Double;
import java.util.Map;
import java.util.Set;

public class HashMapDemo {
    public static void main(String[] args) {
        HashMap<String, Double> hm = new HashMap<>();

        hm.put("John Doe", Double.parseDouble("3434.34"));
        hm.put("Tom Smith", Double.parseDouble("123.22"));
        hm.put("Jane Baker", Double.parseDouble("1378.00"));
        hm.put("Tod Hall", Double.parseDouble("99.22"));
        hm.put("Ralph Smith", Double.parseDouble("-19.08"));

        Set<Map.Entry<String,Double>> set = hm.entrySet();
        for(Map.Entry<String,Double> me : set) {
            System.out.println(me.getKey() + ": " + me.getValue());
        }
        double balance = hm.get("John Doe");
        hm.put("John Doe", balance + 1000);
        System.out.println("John Doe의 새로운 잔고: " + hm.get("John Doe"));
    }
}
