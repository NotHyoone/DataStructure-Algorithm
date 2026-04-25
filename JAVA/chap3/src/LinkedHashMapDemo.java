import java.util.LinkedHashMap;
import java.util.Map;


public class LinkedHashMapDemo {
    public static void main(String[] args) {
        Map<String, Double> lhm = new LinkedHashMap<>();
        lhm.put("Apple", 91.98);
        lhm.put("Sony", 16.76);
        lhm.put("Dell", 30.47);
        lhm.put("HP", 33.91);
        System.out.println("Map contents: " + lhm);
        for(String key : lhm.keySet())
            System.out.println(key + ":\t" + lhm.get(key));

        System.out.println("\nThe size of Map is " + lhm.size());
        System.out.println("Is Map empty? : " + lhm.isEmpty());
        System.out.println("Map contains Sony as key? : " + lhm.containsKey("Sony"));

        System.out.println("\nRemove entry for Dell: " + lhm.remove("Dell"));
        System.out.println("Content of Map removing Dell: " + lhm);

        lhm.clear();
        System.out.println("Content of Map after clear: " + lhm);
    }
}
