import java.util.ArrayList;

class Node_<K, V> {
    K key;
    V value;
    Node_<K, V> next;

    public Node_(K key, V value, Node_<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}

public class SequentialSearchST<K, V> {
    private Node_<K, V> first;
    private int N = 0; // 연결 리스트 노드 수

    public V get(K key) {
        for (Node_<K, V> x = first; x != null; x = x.next)
            if (key.equals(x.key)) return x.value;
        return null;
    }
    public void put(K key, V value) {
        for (Node_<K, V> x = first; x != null; x=x.next)
            if (key.equals(x.key)) {
                x.value = value; return;
            }
        first = new Node_<K, V>(key, value, first);
        N++;
    }
    public void delete(K key) {
        // 첫 번째 노드 삭제하는 경우
        if (key.equals(first.key)) {
            first = first.next; N--; return;
        }

        for (Node_<K, V> x = first; x.next != null; x = x.next) {
            if (key.equals(x.next.key)) {
                x.next = x.next.next;
                N--;
                return;
            }
        }
    }
    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (Node_<K, V> x = first; x != null; x=x.next)
            keyList.add(x.key);
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
