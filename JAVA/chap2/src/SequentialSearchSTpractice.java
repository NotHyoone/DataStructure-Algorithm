import java.util.ArrayList;

class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;

    public Node(K key, V value, Node<K,V>next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }
}


public class SequentialSearchSTpractice<K, V> {
    private Node<K,V> first;
    private int N = 0;      // 노드 수

    public V get(K key) {
        for (Node<K,V>x=first; x !=null; x = x.next) {
            if (key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        // 존재 시 값 변경
        for (Node<K, V> x =first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.value = value;
                return;
            }
        }
        // 존재 안 하면 추가 (앞에)
        first = new Node<K, V>(key, value, first);
        N++;
    }

    public void delete(K key) {
        // 맨 앞 삭제
        if (key.equals(first.key)) {
            first = first.next;
            N--;
            return;
        }

        // 중간 및 끝 삭제
        for (Node<K, V> x=first; x.next != null; x=x.next) {
            if (key.equals(x.next.key)) {
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