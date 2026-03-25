import java.util.ArrayList;
import java.util.Arrays;

public class BinarySearchST<K extends Comparable<K>, V> {
    private static final int INIT_CAPACITY = 10;
    private K[] keys;   // 키의 배열
    private V[] vals; // 값의 배열
    private int N;     // 키-값 쌍의 수

    public BinarySearchST() {
        keys = (K[]) new Comparable[INIT_CAPACITY];
        vals = (V[]) new Object[INIT_CAPACITY];
    }

    public BinarySearchST(int capacity) {
        keys = (K[]) new Comparable[capacity];  // 제네릭 배열 생성
        vals = (V[]) new Object[capacity];    // 제네릭 배열 생성
    }

    public boolean contains(K key) { return get(key) != null; }
    public boolean isEmpty() { return N == 0; }
    public int size() { return N; }

    private void resize(int capacity) {
        K[] tempk = (K[]) new Comparable[capacity];
        V[] tempv = (V[]) new Object[capacity];
        System.arraycopy(keys, 0, tempk, 0, N);
        System.arraycopy(vals, 0, tempv, 0, N);
        keys = tempk;
        vals = tempv;
    }

    // search()는 키가 존재하면 해당 키의 인덱스, 존재하지 않으면 삽입될 위치를 반환
    private int search(K key) {
        int lo = 0;
        int hi = N-1;
        while (lo <= hi) {
            int mid = (hi + lo) / 2;
            int cmp = key.compareTo(keys[mid]);

            if (cmp < 0)        hi = mid - 1;
            else if (cmp > 0)   lo = mid + 1;
            else                return mid;
        }
        return lo;  // 키가 존재하지 않을 때, 삽입될 위치 반환
    }

    public V get(K key) {
        if (isEmpty()) return null;
        int i = search(key);    // 이진 검색
        if (i < N && keys[i].compareTo(key) == 0) return vals[i];
        else return null;
    }


    private void put(K key, V value) {
        int i = search(key);

        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = value;
            return;
        }
        if (N == keys.length)
            resize(2 * keys.length);

        // 한 칸씩 뒤로 민다
        for (int j = N; j > i; j--) {
            keys[j] = keys[j-1];
            vals[j] = vals[j-1];
        }

        keys[i] = key;
        vals[i] = value;
        N++;
    }

    public void delete(K key) {
        if (isEmpty()) return;
        int i = search(key);
        if (i == N || keys[i].compareTo(key) != 0)   return;

        // 한 칸 앞으로 당긴다
        for (int j = i; j < N-1; j++) {
            keys[j] = keys[j+1];
            vals[j] = vals[j+1];
        }
        N--;
        keys[N] = null;
        vals[N] = null;

        if (N > INIT_CAPACITY && N == keys.length/4)
            resize(keys.length/2);
    }

    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (int i = 0; i<N;i++)
            keyList.add(keys[i]);
        return keyList;
    }


}
