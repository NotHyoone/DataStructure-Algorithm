import java.util.ArrayList;

class Node2<K, V> {
    K key;
    V value;
    Node<K, V> next;
    public Node2(K key, V value, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}

public class SequentialSearchST2<K, V> {
    private Node<K, V> first;
    private int N = 0;

    public V get(K key) {
        for (Node<K, V> x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }
    public void put(K key, V value) {
        // 키가 있는 경우
        for (Node<K, V> x = first; x != null; x=x.next) {
            if (key.equals(x.key)) {
                x.value = value;
                return;
            }
        }
        first = new Node<K, V>(key, value, first);  // 키 없으면, 앞에 노드 추가
        N++;
    }
    public void delete(K key) {
        // 맨 앞 삭제
        if (key.equals(first.key)){
            first = first.next;
            N--;
            return;
        }

        for (Node<K, V> x=first; x.next != null; x=x.next) {
            if(key.equals(x.next.key)) {
                x.next = x.next.next;
                N--;
                return;
            }
        }
    }
    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (Node<K, V> x=first; x!=null; x=x.next) {
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
