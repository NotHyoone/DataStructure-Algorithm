import java.util.LinkedList;
import java.util.Queue;

public class TrieSt<Value> {
    private static final int R = 256;        // extended ASCII
    private Node root;
    private int N;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    public TrieSt() {}

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    public Value get(String key) {
        validateKey(key);
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    public void put(String key, Value val) {
        validateKey(key);
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) N++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        validateKey(prefix);
        Queue<String> queue = new LinkedList<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        if (x.val != null) queue.add(prefix.toString());
        for (char c = 0; c < R; c++) {
            if (x.next[c] == null) continue;
            prefix.append(c);
            collect(x.next[c], prefix, queue);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        validatePattern(pattern);
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), pattern, 0, queue);
        return queue;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, int d, Queue<String> queue) {
        if (x == null) return;
        if (d == pattern.length()) {
            if (x.val != null) queue.add(prefix.toString());
            return;
        }

        char p = pattern.charAt(d);
        if (p == '.') {
            for (char c = 0; c < R; c++) {
                if (x.next[c] == null) continue;
                prefix.append(c);
                collect(x.next[c], prefix, pattern, d + 1, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(p);
            collect(x.next[p], prefix, pattern, d + 1, queue);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public String longestPrefixOf(String s) {
        validateKey(s);
        int length = search(root, s, 0, 0);
        return s.substring(0, length);
    }

    private int search(Node x, String s, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == s.length()) return length;
        char c = s.charAt(d);
        return search(x.next[c], s, d+1, length);
    }

    public void delete(String key) {
        validateKey(key);
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) N--;
            x.val = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }
        if (x.val != null) return x;
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) return x;
        }
        return null;
    }

    private void validateKey(String key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) >= R) {
                throw new IllegalArgumentException("key contains a non-extended ASCII character");
            }
        }
    }

    private void validatePattern(String pattern) {
        if (pattern == null) throw new IllegalArgumentException("pattern is null");
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c != '.' && c >= R) {
                throw new IllegalArgumentException("pattern contains a non-extended ASCII character");
            }
        }
    }
}
