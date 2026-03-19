import java.util.ArrayList;

class Node<K, V> {  // 하나의 노드를 표현
    // (키, 값, 다음 노드를 가리키는 참조)의 쌍으로 구성
    K key; V value; Node<K, V> next;

    public Node(K key, V value, Node<K, V> next) {
        this.key = key; this.value = value; this.next = next;
    }
}

public class SequentialSearchST<K, V> {
    private Node<K, V> first;   // 첫번째 노드에 대한 참조를 유지. 초기값 = null
    int N;  // 연결 리스트의 노드 수를 유지. 초기값 = 0

    public V get(K key) {
        for (Node<K, V> x = first; x != null; x = x.next) // 연결리스트를 스캔
            if (key.equals(x.key))
                return x.value;
        return null;
    }
    public void put(K key, V value) {
        for (Node<K, V> x = first; x != null; x = x.next)
            if (key.equals(x.key)) { // 키가 있을 경우, 값만 변경
                x.value = value;
                return;
            }
        first = new Node<K, V> (key, value, first); // 키가 없으면, 앞에 노드 추가
        N++;
    }
    public void delete(K key) {
        if (key.equals(first.key)) {    // 첫번째 노드 삭제하는 경우
            first = first.next; N--;
            return;
        }
        for (Node<K, V> x = first; x.next != null; x = x.next) { // 삭제할 노드를 검색
            if (key.equals(x.next.key)) {   // 삭제할 노드를 찾은 경우, x가 가리키는 노드의 다음 노드를 삭제
                x.next = x.next.next; N--;
                return;
            }
        }
    }
    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);    // ArrayList는 Iterable을 구현
        for (Node<K, V> x= first; x !=null; x = x.next)
            keyList.add(x.key);
        return keyList;
    }
    public boolean contains(K key) { return get(key) != null; }
    public boolean isEmpty() {return N == 0;}
    public int size() {return N;}
}
