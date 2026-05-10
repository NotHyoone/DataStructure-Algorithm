// 22212046 안효원
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

class FindCombination {
    public static void findCombination(int start, int n, int k, List<Integer> current) {
        if (current.size() == k) {
            System.out.print(current + " ");
            return;
        }
        for (int i = start; i <= n; i++) {
            current.add(i);
            findCombination(i+1, n,k, current);
            current.remove(current.size() -1);
        }
    }
}


public class HW2 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("정수 n과 k를 입력? ");
        String[] input = br.readLine().split(" ");
        FindCombination fc = new FindCombination();
        int n = Integer.parseInt(input[0]);
        int k = Integer.parseInt(input[1]);
        fc.findCombination(1, n, k, new ArrayList<>());
    }
}
