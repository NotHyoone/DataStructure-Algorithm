import java.util.ArrayList;

class Node_SS<K,V> {
    K key;
    V value;
    Node_SS<K,V> next;

    public Node_SS(K key, V value, Node_SS<K,V> next) {
        this.key = key; this.value = value; this.next = next;
    }
}

public class SequentialSearchSTpractice<K,V> {
    private Node_SS<K,V> first;
    int N = 0;

    public V get(K key) {
        for (Node_SS<K,V> x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        for (Node_SS<K,V> x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                value = x.value;
                return;
            }
        }
        first = new Node_SS<K,V>(key, value, first);
        N++;
    }

    public void delete(K key) {
        // 첫 번째 노드 삭제
        if (key.equals(first.key)) {
            first = first.next; N--;
            return;
        }
        // 삭제할 노드 검색
        for (Node_SS<K,V> x = first; x.next !=null; x = x.next) {
            if (key.equals(x.next.key)) {
                x.next = x.next.next; N--;
                return;
            }
        }
    }

    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (Node_SS<K,V> x = first; x != null; x = x.next) {
            keyList.add(x.key);
        }
        return keyList;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }
}