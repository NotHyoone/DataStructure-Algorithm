import java.lang.Integer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class AL_LL_Compare {
    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<>();
        LinkedList<Integer> ll = new LinkedList<>();

        Random rand = new Random();
        int pos;
        long start, end;

        start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++)
            al.add(0,i);
        end = System.currentTimeMillis();
        System.out.println("Duration = " + (end-start));

        al.clear();
        start = System.currentTimeMillis();
        for(int i = 0 ;i < 100000; i++) {
            pos = rand.nextInt(i+1);
            al.add(pos, i);
        }
        end = System.currentTimeMillis();
        System.out.println("Duration = " +(end-start));

        start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++)
            ll.add(0,i);
        end = System.currentTimeMillis();
        System.out.println("Duration = " + (end-start));

        ll.clear();
        start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++) {
            pos = rand.nextInt(i+1);
            ll.add(pos, i);
        }
        end = System.currentTimeMillis();
        System.out.println("Duration = " + (end-start));
    }
}
