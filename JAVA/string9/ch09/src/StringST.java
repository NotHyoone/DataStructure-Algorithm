import com.sun.jdi.Value;

import java.util.ArrayList;
import java.util.List;

public class StringST <Value> {
    private static final int R = 256;  // extended ASCII

    private static class Node {
        Object val;
        Node[] next = new Node[R];
    }

    private Node root;
    private int n; // number of key-value pairs

    public StringST() {}

    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("key is null");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val, 0);
    }
    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) n++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    @SuppressWarnings("unchecked")
    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;

        if (d == key.length()) {
            if (x.val != null) n--;
            x.val = null;
        } else {
            int c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        if (x.val != null) return x;
        for (int c = 0; c < R; c++) {
            if (x.next[c] != null) return x;
        }
        return null;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public String longestPrefixOf(String s) {
        if (s == null) throw new IllegalArgumentException("argument is null");
        int length = longestPrefixOf(root, s, 0, 0);
        return s.substring(0, length);
    }

    private int longestPrefixOf(Node x, String s, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == s.length()) return length;
        int c = s.charAt(d);
        return longestPrefixOf(x.next[c], s, d+1, length);
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("prefix is null");
        List<String> result = new ArrayList<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), result);
        return result;
    }

    public Iterable<String> keysThatMatch(String pattern) {
        if (pattern == null) throw new IllegalArgumentException("pattern is null");
        List<String> result = new ArrayList<>();
        collect(root, new StringBuilder(), pattern, 0, result);
        return result;
    }

    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    private void collect(Node x, StringBuilder prefix, List<String> result) {
        if (x == null) return;
        if (x.val != null) result.add(prefix.toString());
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) {
                prefix.append((char) c);
                collect(x.next[c], prefix, result);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    // '.' is wildcard : matches any single character
    private void collect(Node x, StringBuilder prefix, String pattern, int d, List<String> result) {
        if (x == null) return;
        if (d == pattern.length()) {
            if (x.val != null) result.add(prefix.toString());
            return;
        }

        char p = pattern.charAt(d);
        if (p == '.') {
            for (int c = 0; c < R; c++) {
                if (x.next[c] != null) {
                    prefix.append((char) c);
                    collect(x.next[c], prefix, pattern, d + 1, result);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
        } else {
            int c = p;
            prefix.append(p);
            collect(x.next[c], prefix, pattern, d + 1, result);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
