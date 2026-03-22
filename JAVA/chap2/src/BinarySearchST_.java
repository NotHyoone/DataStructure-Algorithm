import java.util.ArrayList;

public class BinarySearchST_<K extends Comparable<K>, V> {
    private static final int INIT_CAPACITY = 10;
    private K[] keys;
    private V[] vals;
    private int N;

    public BinarySearchST_() {
        keys = (K[]) new Comparable[INIT_CAPACITY];
        vals = (V[]) new Object[INIT_CAPACITY];
    }

    public BinarySearchST_(int capacity) {
        keys = (K[]) new Comparable[capacity];
        vals = (V[]) new Object[capacity];
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

    // 배열 크기를 동적으로 변경
    public void resize(int capacity) {
        K[] tempk = (K[]) new Comparable[capacity];
        V[] tempv = (V[]) new Object[capacity];
        // for 문
//        for (int i = 0; i<N; i++) {
//            tempk[i] = keys[i];
//            tempv[i] = vals[i];
//        }
        // arraycopy로 복사
        System.arraycopy(keys, 0, tempk, 0, N);
        System.arraycopy(vals, 0, tempv, 0, N);
        vals = tempv;
        keys = tempk;
    }

    private int search(K key) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = (hi + lo) / 2;
            int cmp = key.compareTo(keys[mid]);

            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        // 키가 없을 경우, -1이 아니라 lo가 반환
        // lo는 키가 삽입될 위치이기도 함
        return lo;
    }

    public V get(K key) {
        if (isEmpty()) return null;
        int i = search(key); // 이진 검색
        if (i < N && keys[i].compareTo(key) == 0) return vals[i];
        else return null;       // 키가 없으면 null 반환
    }

    public void put(K key, V value) {
        int i = search(key);  // 일단 키를 찾고
        // 있으면, 값만 변경
        // 왜 비교를 다시 하냐면, search는 키가 없을 때 삽입될 위치를 반환하기 때문
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = value; return;
        }
        // 없으면, 추가해야 하니 배열 크기 확장
        if (N == keys.length)
            resize(2 * keys.length);

        // 추가될 곳의 공간 확보
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
        int i = search(key);    // 이진검색 : 모든 키는 이진 검색으로 찾자!!
        if (i == N || keys[i].compareTo(key) != 0) return; // 없으면, 그냥 반환

        // 뒤에 있는 키들을 한칸 앞으로
        for (int j = i; j < N-1; j++) {
            keys[j] = keys[j+1];
            vals[j] = vals[j+1];
        }
        N--;
        keys[N] = null; // 왜 null로 초기화하냐면, GC가 객체를 수거할 수 있도록 하기 위해서
        vals[N] = null; // GC가 객체를 수거할 수 있도록 하기 위해서
        // 크기가 절반으로 줄어들 때 배열 크기도 절반으로 줄이기
        if (N > INIT_CAPACITY && N == keys.length/4) {
            resize(keys.length/2);
        }
    }
    // 연결 리스트의 경우와 거의 동일
    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (int i = 0; i < N; i++) {
            keyList.add(keys[i]);
        }
        return keyList;
    }
}