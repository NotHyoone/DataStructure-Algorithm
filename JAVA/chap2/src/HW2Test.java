import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class HW2Test {

    public static void main(String[] args) {
        Tree23<String, Integer> st = new Tree23<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("입력 파일 이름? ");
        String fname = sc.nextLine();	// 파일 이름을 입력
        System.out.print("난수 생성을 위한 seed 값? ");
        Random rand = new Random(sc.nextLong());
        sc.close();
        try {
            sc = new Scanner(new File(fname));
            long start = System.currentTimeMillis();
            while (sc.hasNext()) {
                String word = sc.next();
                if (!st.contains(word))
                    st.put(word, 1);
                else	st.put(word, st.get(word) + 1);
            }
            long end = System.currentTimeMillis();
            System.out.println("입력 완료: 소요 시간 = " + (end-start) + "ms");

            System.out.println("### 생성 시점의 트리 정보");
            print_tree(st);		// 정상적으로 출력되면 50점

            ArrayList<String> keyList = (ArrayList<String>) st.keys();
            Collections.shuffle(keyList, rand);
            int loopCount = (int)(keyList.size() * 0.95);
            for (int i = 0; i < loopCount; i++) {
                st.delete(keyList.get(i));						// 주석 처리 가능
            }
            System.out.println("\n### 키 삭제 후 트리 정보");		// 주석 처리 가능
            print_tree(st);										// 주석 처리 가능. 여기까지 정상적으로 출력되면 100점
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        if (sc != null)
            sc.close();
    }

    private static void print_tree(Tree23<String, Integer> st) {
        System.out.println("등록된 단어 수 = " + st.size());
        System.out.println("트리의 깊이 = " + st.depth());

        String maxKey = "";
        int maxValue = 0;
        for (String word : st.keys())
            if (st.get(word) > maxValue) {
                maxValue = st.get(word);
                maxKey = word;
            }
        System.out.println("가장 빈번히 나타난 단어와 빈도수: " + maxKey + " " + maxValue);
    }
}
