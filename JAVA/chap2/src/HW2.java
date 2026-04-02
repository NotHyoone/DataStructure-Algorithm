import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;


public class HW2 {

    private static class Tree23<K extends Comparable<K>, V> {

        @SuppressWarnings("unchecked")
        private static class Node23<K, V> {
            int n;
            private final Object[] keys   = new Object[2];
            private final Object[] values = new Object[2];
            private final Object[] child  = new Object[3];

            boolean leaf() { return child[0] == null; }

            K           key(int i) { return (K)           keys[i];  }
            V           val(int i) { return (V)           values[i]; }
            Node23<K,V>  ch(int i) { return (Node23<K,V>) child[i]; }

            void key(int i, K k)           { keys[i]  = k; }
            void val(int i, V v)           { values[i] = v; }
            void ch (int i, Node23<K,V> c) { child[i]  = c; }
        }

        private class Split {
            K key; V value;
            Node23<K,V> left, right;
            Split(K key, V value, Node23<K,V> left, Node23<K,V> right) {
                this.key = key; this.value = value; this.left = left; this.right = right;
            }
        }

        private Node23<K,V> root;
        private int  size = 0;
        private boolean inserted;

        public boolean isEmpty()       { return size == 0; }
        public int     size()          { return size; }
        public boolean contains(K key) { return get(key) != null; }

        public V get(K key) {
            Node23<K,V> x = root;
            while (x != null) {
                int idx = 0;
                while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;
                if (idx < x.n && key.compareTo(x.key(idx)) == 0) return x.val(idx);
                x = x.ch(idx);
            }
            return null;
        }

        public void put(K key, V value) {
            if (root == null) {
                root = new Node23<>();
                root.n = 1; root.key(0, key); root.val(0, value);
                size++;
                return;
            }
            inserted = false;
            Split sp = insert(root, key, value);
            if (sp != null) {
                Node23<K,V> nr = new Node23<>();
                nr.n = 1;
                nr.key(0, sp.key); nr.val(0, sp.value);
                nr.ch(0, sp.left); nr.ch(1, sp.right);
                root = nr;
            }
            if (inserted) size++;
        }

        public ArrayList<K> keys() {
            ArrayList<K> list = new ArrayList<>();
            inorder(root, list);
            return list;
        }

        public int depth() {
            int d = 0;
            Node23<K,V> x = root;
            while (x != null) { d++; x = x.ch(0); }
            return d;
        }

        public void delete(K key) {
            if (key == null) throw new IllegalArgumentException();
            if (root == null) return;
            if (!deleteRec(root, key)) return;
            size--;
            if (root.n == 0) root = root.leaf() ? null : root.ch(0);
        }

        private Split insert(Node23<K,V> x, K key, V value) {
            int idx = 0;
            while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;

            if (idx < x.n && key.compareTo(x.key(idx)) == 0) {
                x.val(idx, value);
                return null;
            }

            if (x.leaf()) {
                if (x.n == 2) return splitLeaf(x, key, value);
                for (int i = x.n; i > idx; i--) {
                    x.key(i, x.key(i-1));
                    x.val(i, x.val(i-1));
                }
                x.key(idx, key); x.val(idx, value);
                x.n++;
                inserted = true;
                return null;
            }

            Split sp = insert(x.ch(idx), key, value);
            if (sp == null) return null;
            if (x.n == 2) return splitInternal(x, sp, idx);

            for (int i = x.n; i > idx; i--) {
                x.key(i,  x.key(i-1));
                x.val(i,  x.val(i-1));
                x.ch(i+1, x.ch(i));
            }
            x.key(idx, sp.key); x.val(idx, sp.value);
            x.ch(idx, sp.left); x.ch(idx+1, sp.right);
            x.n++;
            return null;
        }

        private Split splitLeaf(Node23<K,V> x, K key, V value) {
            int idx = 0;
            while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;

            K k0, k1, k2;  V v0, v1, v2;
            if      (idx == 0) { k0=key;      k1=x.key(0); k2=x.key(1); v0=value;    v1=x.val(0); v2=x.val(1); }
            else if (idx == 1) { k0=x.key(0); k1=key;      k2=x.key(1); v0=x.val(0); v1=value;    v2=x.val(1); }
            else               { k0=x.key(0); k1=x.key(1); k2=key;      v0=x.val(0); v1=x.val(1); v2=value;    }

            Node23<K,V> L = new Node23<>(), R = new Node23<>();
            L.n=1; L.key(0,k0); L.val(0,v0);
            R.n=1; R.key(0,k2); R.val(0,v2);
            inserted = true;
            return new Split(k1, v1, L, R);
        }

        private Split splitInternal(Node23<K,V> x, Split csp, int idx) {
            K k0, k1, k2;  V v0, v1, v2;
            Node23<K,V> c0, c1, c2, c3;

            if (idx == 0) {
                k0=csp.key;   k1=x.key(0); k2=x.key(1);
                v0=csp.value; v1=x.val(0); v2=x.val(1);
                c0=csp.left; c1=csp.right; c2=x.ch(1); c3=x.ch(2);
            } else if (idx == 1) {
                k0=x.key(0); k1=csp.key;   k2=x.key(1);
                v0=x.val(0); v1=csp.value; v2=x.val(1);
                c0=x.ch(0); c1=csp.left; c2=csp.right; c3=x.ch(2);
            } else {
                k0=x.key(0); k1=x.key(1); k2=csp.key;
                v0=x.val(0); v1=x.val(1); v2=csp.value;
                c0=x.ch(0); c1=x.ch(1); c2=csp.left; c3=csp.right;
            }

            Node23<K,V> L = new Node23<>(), R = new Node23<>();
            L.n=1; L.key(0,k0); L.val(0,v0); L.ch(0,c0); L.ch(1,c1);
            R.n=1; R.key(0,k2); R.val(0,v2); R.ch(0,c2); R.ch(1,c3);
            return new Split(k1, v1, L, R);
        }

        private void inorder(Node23<K,V> x, ArrayList<K> list) {
            if (x == null) return;
            if (x.n == 1) {
                inorder(x.ch(0), list); list.add(x.key(0)); inorder(x.ch(1), list);
            } else {
                inorder(x.ch(0), list); list.add(x.key(0));
                inorder(x.ch(1), list); list.add(x.key(1));
                inorder(x.ch(2), list);
            }
        }

        private boolean deleteRec(Node23<K,V> x, K key) {
            int idx = 0;
            while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;

            if (idx < x.n && key.compareTo(x.key(idx)) == 0) {
                if (x.leaf()) { removeKeyAt(x, idx); return true; }
                Node23<K,V> succ = findMinNode(x.ch(idx+1));
                K sk = succ.key(0);
                x.key(idx, sk); x.val(idx, succ.val(0));
                boolean removed = deleteRec(x.ch(idx+1), sk);
                if (removed) fixUnderflow(x, idx+1);
                return removed;
            }

            Node23<K,V> next = x.ch(idx);
            if (next == null) return false;
            boolean removed = deleteRec(next, key);
            if (removed) fixUnderflow(x, idx);
            return removed;
        }

        private Node23<K,V> findMinNode(Node23<K,V> x) {
            while (!x.leaf()) x = x.ch(0);
            return x;
        }

        private void removeKeyAt(Node23<K,V> x, int idx) {
            if (x.n == 2) {
                if (idx == 0) { x.key(0, x.key(1)); x.val(0, x.val(1)); }
                x.key(1, null); x.val(1, null);
                x.n = 1;
            } else {
                x.key(0, null); x.val(0, null);
                x.n = 0;
            }
        }

        private void fixUnderflow(Node23<K,V> parent, int idx) {
            Node23<K,V> child = parent.ch(idx);
            if (child == null || child.n > 0) return;

            Node23<K,V> left  = idx > 0        ? parent.ch(idx-1) : null;
            Node23<K,V> right = idx < parent.n ? parent.ch(idx+1) : null;

            if (left  != null && left.n  == 2) { rotateFromLeft(parent, idx);  return; }
            if (right != null && right.n == 2) { rotateFromRight(parent, idx); return; }
            if (left  != null) combineWithLeft(parent, idx);
            else if (right != null) combineWithRight(parent, idx);
        }

        private void rotateFromLeft(Node23<K,V> parent, int idx) {
            Node23<K,V> child = parent.ch(idx);
            Node23<K,V> left  = parent.ch(idx-1);

            child.key(0, parent.key(idx-1));
            child.val(0, parent.val(idx-1));
            if (!left.leaf()) {
                child.ch(1, child.ch(0));
                child.ch(0, left.ch(2));
                left.ch(2, null);
            }
            child.n = 1;

            parent.key(idx-1, left.key(1));
            parent.val(idx-1, left.val(1));
            left.key(1, null); left.val(1, null);
            left.n = 1;
        }

        private void rotateFromRight(Node23<K,V> parent, int idx) {
            Node23<K,V> child = parent.ch(idx);
            Node23<K,V> right = parent.ch(idx+1);

            child.key(0, parent.key(idx));
            child.val(0, parent.val(idx));
            if (!right.leaf()) {
                child.ch(1, right.ch(0));
                right.ch(0, right.ch(1));
                right.ch(1, right.ch(2));
                right.ch(2, null);
            }
            child.n = 1;

            parent.key(idx, right.key(0));
            parent.val(idx, right.val(0));
            right.key(0, right.key(1));
            right.val(0, right.val(1));
            right.key(1, null); right.val(1, null);
            right.n = 1;
        }

        private void combineWithLeft(Node23<K,V> parent, int idx) {
            Node23<K,V> left  = parent.ch(idx-1);
            Node23<K,V> child = parent.ch(idx);

            left.key(1, parent.key(idx-1));
            left.val(1, parent.val(idx-1));
            left.n = 2;
            if (!left.leaf()) left.ch(2, child.ch(0));

            if (parent.n == 2 && idx-1 == 0) {
                parent.key(0, parent.key(1));
                parent.val(0, parent.val(1));
            }
            parent.key(parent.n-1, null);
            parent.val(parent.n-1, null);
            for (int i = idx; i < parent.n; i++) parent.ch(i, parent.ch(i+1));
            parent.ch(parent.n, null);
            parent.n--;
        }

        private void combineWithRight(Node23<K,V> parent, int idx) {
            Node23<K,V> child = parent.ch(idx);
            Node23<K,V> right = parent.ch(idx+1);

            child.key(0, parent.key(idx));
            child.val(0, parent.val(idx));
            child.key(1, right.key(0));
            child.val(1, right.val(0));
            child.n = 2;
            if (!right.leaf()) {
                child.ch(1, right.ch(0));
                child.ch(2, right.ch(1));
            }

            if (parent.n == 2 && idx == 0) {
                parent.key(0, parent.key(1));
                parent.val(0, parent.val(1));
            }
            parent.key(parent.n-1, null);
            parent.val(parent.n-1, null);
            for (int i = idx+1; i < parent.n; i++) parent.ch(i, parent.ch(i+1));
            parent.ch(parent.n, null);
            parent.n--;
        }
    }

    public static void main(String[] args) {
        Tree23<String, Integer> st = new Tree23<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("입력 파일 이름? ");
        String fname = sc.nextLine();
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
            print_tree(st);

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
