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



    private static class Tree23<K extends Comparable<K>, V> {
        private static class Node23 {
            int n;  // 2-3 트리에서 노드가 가진 키의 개수
            Object[] keys = new Object[2];      // 2-3 트리에서 노드가 가질 수 있는 최대 키의 개수
            Object[] values = new Object[2];    // 2-3 트리에서 노드가 가질 수 있는 최대 값의 개수
            Node23[] child = new Node23[3];     // 2-3 트리에서 노드가 가질 수 있는 최대 자식의 개수
            // 2-3 트리에서 노드가 leaf인지 아닌지 판단하는 함수입니다.
            boolean isleaf() {return child[0] == null;}
        }

        private class Split {
            K key;
            V value;
            Node23 left, right;
            // 2-3 트리에서 노드를 분할할 때 필요한 정보를 담는 클래스입니다.
            Split(K key, V value, Node23 left, Node23 right) {
                this.key = key; this.value = value; this.left = left; this.right = right;
            }
        }

        private Node23 root;
        private int size;		// 트리에 저장된 키-값 쌍의 개수를 나타내는 변수입니다.
        private boolean inserted;	// put() 메소드에서 키가 새로 추가되었는지 여부를 나타내는 변수입니다.

        public int size() { return size; }
        public boolean contains(K key) { return get(key) != null; }
        public boolean isEmpty() { return size == 0; }

        public V get(K key) {
            Node23 x = root;
            while (x != null) {
                int idx = 0;
                while (idx < x.n && key.compareTo((K) x.keys[idx]) > 0) idx++;
                if (idx < x.n && key.compareTo((K) x.keys[idx]) == 0) return (V) x.values[idx];
                x = x.child[idx];
            }
            return null;
        }

        public void put(K key, V value) {
            if (root == null) {
                root = new Node23();
                root.n = 1;
                root.keys[0] = key;
                root.values[0] = value;
                size = 1;
                inserted = true;
            }
            inserted = false;
            Split split = insert(root, key, value);
            if (split != null) {
                Node23 newRoot = new Node23();
                newRoot.n = 1;
                newRoot.keys[0] = split.key;
                newRoot.values[0] = split.value;
                newRoot.child[0] = split.left;
                newRoot.child[1] = split.right;
                root = newRoot;
            }
            if (inserted) size++;
        }

        public int depth() {
            int d = 0;
            Node23 x = root;
            while (x != null) {
                d++;
                x = x.child[0];
            }
            return d;
        }
    }
}
