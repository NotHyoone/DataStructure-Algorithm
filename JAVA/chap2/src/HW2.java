import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;


public class HW2 {

    private static class Tree23<K extends Comparable<K>, V> {
        private static class Node23 {
            int n;
            Object[] keys = new Object[2];
            Object[] values = new Object[2];
            Node23[] child = new Node23[3];

            boolean leaf() {
                return child[0] == null;
            }
        }

        private class Split {
            K key;
            V value;
            Node23 left;
            Node23 right;
        }

        private Node23 root;
        private int size;
        private boolean insertedNew;

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public boolean contains(K key) {
            return get(key) != null;
        }

        @SuppressWarnings("unchecked")
        public V get(K key) {
            if (key == null) throw new IllegalArgumentException();
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
            if (key == null) throw new IllegalArgumentException();
            if (root == null) {
                root = new Node23();
                root.n = 1;
                root.keys[0] = key;
                root.values[0] = value;
                size = 1;
                return;
            }
            insertedNew = false;
            Split sp = insert(root, key, value);
            if (sp != null) {
                Node23 newRoot = new Node23();
                newRoot.n = 1;
                newRoot.keys[0] = sp.key;
                newRoot.values[0] = sp.value;
                newRoot.child[0] = sp.left;
                newRoot.child[1] = sp.right;
                root = newRoot;
            }
            if (insertedNew) size++;
        }

        public Iterable<K> keys() {
            ArrayList<K> list = new ArrayList<>();
            inorder(root, list);
            return list;
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

        public void delete(K key) {
            if (key == null) throw new IllegalArgumentException();
            if (root == null) return;
            boolean removed = deleteRec(root, key);
            if (!removed) return;
            size--;
            if (root.n == 0) {
                if (root.leaf()) root = null;
                else root = root.child[0];
            }
        }

        @SuppressWarnings("unchecked")
        private Split insert(Node23 x, K key, V value) {
            int idx = 0;
            while (idx < x.n && key.compareTo((K) x.keys[idx]) > 0) idx++;
            if (idx < x.n && key.compareTo((K) x.keys[idx]) == 0) {
                x.values[idx] = value;
                return null;
            }

            if (x.leaf()) {
                if (x.n == 1) {
                    if (idx == 0) {
                        x.keys[1] = x.keys[0];
                        x.values[1] = x.values[0];
                        x.keys[0] = key;
                        x.values[0] = value;
                    } else {
                        x.keys[1] = key;
                        x.values[1] = value;
                    }
                    x.n = 2;
                    insertedNew = true;
                    return null;
                }

                Object[] tk = new Object[3];
                Object[] tv = new Object[3];
                if (idx == 0) {
                    tk[0] = key; tv[0] = value;
                    tk[1] = x.keys[0]; tv[1] = x.values[0];
                    tk[2] = x.keys[1]; tv[2] = x.values[1];
                } else if (idx == 1) {
                    tk[0] = x.keys[0]; tv[0] = x.values[0];
                    tk[1] = key; tv[1] = value;
                    tk[2] = x.keys[1]; tv[2] = x.values[1];
                } else {
                    tk[0] = x.keys[0]; tv[0] = x.values[0];
                    tk[1] = x.keys[1]; tv[1] = x.values[1];
                    tk[2] = key; tv[2] = value;
                }

                Node23 left = new Node23();
                Node23 right = new Node23();
                left.n = 1;
                right.n = 1;
                left.keys[0] = tk[0]; left.values[0] = tv[0];
                right.keys[0] = tk[2]; right.values[0] = tv[2];

                Split sp = new Split();
                sp.key = (K) tk[1];
                sp.value = (V) tv[1];
                sp.left = left;
                sp.right = right;
                insertedNew = true;
                return sp;
            }

            Split csp = insert(x.child[idx], key, value);
            if (csp == null) return null;

            if (x.n == 1) {
                if (idx == 0) {
                    x.keys[1] = x.keys[0];
                    x.values[1] = x.values[0];
                    x.keys[0] = csp.key;
                    x.values[0] = csp.value;
                    x.child[2] = x.child[1];
                    x.child[0] = csp.left;
                    x.child[1] = csp.right;
                } else {
                    x.keys[1] = csp.key;
                    x.values[1] = csp.value;
                    x.child[1] = csp.left;
                    x.child[2] = csp.right;
                }
                x.n = 2;
                return null;
            }

            Object[] tk = new Object[3];
            Object[] tv = new Object[3];
            Node23[] tc = new Node23[4];
            if (idx == 0) {
                tk[0] = csp.key; tv[0] = csp.value;
                tk[1] = x.keys[0]; tv[1] = x.values[0];
                tk[2] = x.keys[1]; tv[2] = x.values[1];
                tc[0] = csp.left; tc[1] = csp.right; tc[2] = x.child[1]; tc[3] = x.child[2];
            } else if (idx == 1) {
                tk[0] = x.keys[0]; tv[0] = x.values[0];
                tk[1] = csp.key; tv[1] = csp.value;
                tk[2] = x.keys[1]; tv[2] = x.values[1];
                tc[0] = x.child[0]; tc[1] = csp.left; tc[2] = csp.right; tc[3] = x.child[2];
            } else {
                tk[0] = x.keys[0]; tv[0] = x.values[0];
                tk[1] = x.keys[1]; tv[1] = x.values[1];
                tk[2] = csp.key; tv[2] = csp.value;
                tc[0] = x.child[0]; tc[1] = x.child[1]; tc[2] = csp.left; tc[3] = csp.right;
            }

            Node23 left = new Node23();
            Node23 right = new Node23();
            left.n = 1;
            right.n = 1;
            left.keys[0] = tk[0]; left.values[0] = tv[0];
            right.keys[0] = tk[2]; right.values[0] = tv[2];
            left.child[0] = tc[0]; left.child[1] = tc[1];
            right.child[0] = tc[2]; right.child[1] = tc[3];

            Split sp = new Split();
            sp.key = (K) tk[1];
            sp.value = (V) tv[1];
            sp.left = left;
            sp.right = right;
            return sp;
        }

        @SuppressWarnings("unchecked")
        private void inorder(Node23 x, ArrayList<K> list) {
            if (x == null) return;
            if (x.n == 1) {
                inorder(x.child[0], list);
                list.add((K) x.keys[0]);
                inorder(x.child[1], list);
            } else {
                inorder(x.child[0], list);
                list.add((K) x.keys[0]);
                inorder(x.child[1], list);
                list.add((K) x.keys[1]);
                inorder(x.child[2], list);
            }
        }

        @SuppressWarnings("unchecked")
        private boolean deleteRec(Node23 x, K key) {
            int idx = 0;
            while (idx < x.n && key.compareTo((K) x.keys[idx]) > 0) idx++;

            if (idx < x.n && key.compareTo((K) x.keys[idx]) == 0) {
                if (x.leaf()) {
                    removeKeyAt(x, idx);
                    return true;
                }
                Node23 s = x.child[idx + 1];
                while (!s.leaf()) s = s.child[0];
                x.keys[idx] = s.keys[0];
                x.values[idx] = s.values[0];
                boolean removed = deleteRec(x.child[idx + 1], (K) s.keys[0]);
                if (removed) fixUnderflow(x, idx + 1);
                return removed;
            }

            Node23 next = x.child[idx];
            if (next == null) return false;
            boolean removed = deleteRec(next, key);
            if (removed) fixUnderflow(x, idx);
            return removed;
        }

        private void removeKeyAt(Node23 x, int idx) {
            if (x.n == 2) {
                if (idx == 0) {
                    x.keys[0] = x.keys[1];
                    x.values[0] = x.values[1];
                }
                x.keys[1] = null;
                x.values[1] = null;
                x.n = 1;
            } else {
                x.keys[0] = null;
                x.values[0] = null;
                x.n = 0;
            }
        }

        private void fixUnderflow(Node23 parent, int idx) {
            Node23 child = parent.child[idx];
            if (child == null || child.n > 0) return;

            Node23 left = idx > 0 ? parent.child[idx - 1] : null;
            Node23 right = idx < parent.n ? parent.child[idx + 1] : null;

            if (left != null && left.n == 2) {
                rotateFromLeft(parent, idx);
                return;
            }
            if (right != null && right.n == 2) {
                rotateFromRight(parent, idx);
                return;
            }
            if (left != null) combineWithLeft(parent, idx);
            else if (right != null) combineWithRight(parent, idx);
        }

        private void rotateFromLeft(Node23 parent, int idx) {
            Node23 child = parent.child[idx];
            Node23 left = parent.child[idx - 1];

            child.keys[0] = parent.keys[idx - 1];
            child.values[0] = parent.values[idx - 1];
            if (!left.leaf()) {
                child.child[1] = child.child[0];
                child.child[0] = left.child[2];
                left.child[2] = null;
            }
            child.n = 1;

            parent.keys[idx - 1] = left.keys[1];
            parent.values[idx - 1] = left.values[1];
            left.keys[1] = null;
            left.values[1] = null;
            left.n = 1;
        }

        private void rotateFromRight(Node23 parent, int idx) {
            Node23 child = parent.child[idx];
            Node23 right = parent.child[idx + 1];

            child.keys[0] = parent.keys[idx];
            child.values[0] = parent.values[idx];
            if (!right.leaf()) {
                child.child[1] = right.child[0];
                right.child[0] = right.child[1];
                right.child[1] = right.child[2];
                right.child[2] = null;
            }
            child.n = 1;

            parent.keys[idx] = right.keys[0];
            parent.values[idx] = right.values[0];
            right.keys[0] = right.keys[1];
            right.values[0] = right.values[1];
            right.keys[1] = null;
            right.values[1] = null;
            right.n = 1;
        }

        private void combineWithLeft(Node23 parent, int idx) {
            Node23 left = parent.child[idx - 1];
            Node23 child = parent.child[idx];

            left.keys[1] = parent.keys[idx - 1];
            left.values[1] = parent.values[idx - 1];
            left.n = 2;

            if (!left.leaf()) {
                left.child[2] = child.child[0];
            }

            if (parent.n == 2 && idx - 1 == 0) {
                parent.keys[0] = parent.keys[1];
                parent.values[0] = parent.values[1];
            }
            parent.keys[parent.n - 1] = null;
            parent.values[parent.n - 1] = null;

            for (int i = idx; i < parent.n; i++) {
                parent.child[i] = parent.child[i + 1];
            }
            parent.child[parent.n] = null;
            parent.n--;
        }

        private void combineWithRight(Node23 parent, int idx) {
            Node23 child = parent.child[idx];
            Node23 right = parent.child[idx + 1];

            child.keys[0] = parent.keys[idx];
            child.values[0] = parent.values[idx];
            child.keys[1] = right.keys[0];
            child.values[1] = right.values[0];
            child.n = 2;

            if (!right.leaf()) {
                child.child[1] = right.child[0];
                child.child[2] = right.child[1];
            }

            if (parent.n == 2 && idx == 0) {
                parent.keys[0] = parent.keys[1];
                parent.values[0] = parent.values[1];
            }
            parent.keys[parent.n - 1] = null;
            parent.values[parent.n - 1] = null;

            for (int i = idx + 1; i < parent.n; i++) {
                parent.child[i] = parent.child[i + 1];
            }
            parent.child[parent.n] = null;
            parent.n--;
        }
    }

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
                st.delete(keyList.get(i));
            }
            System.out.println("\n### 키 삭제 후 트리 정보");
            print_tree(st);
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
