# HW2.java — 2-3 트리(Tree23) 상세 문서

## 목차
1. [개요](#개요)
2. [전체 구조](#전체-구조)
3. [내부 클래스: `Node23<K, V>`](#내부-클래스-node23k-v)
4. [내부 클래스: `Split`](#내부-클래스-split)
5. [Tree23 필드](#tree23-필드)
6. [공개 메서드 (Public Methods)](#공개-메서드-public-methods)
   - [isEmpty()](#isempty)
   - [size()](#size)
   - [contains(K key)](#containsk-key)
   - [get(K key)](#getk-key)
   - [put(K key, V value)](#putk-key-v-value)
   - [keys()](#keys)
   - [depth()](#depth)
   - [delete(K key)](#deletek-key)
7. [비공개 메서드 (Private Methods)](#비공개-메서드-private-methods)
   - [insert(Node23, K, V)](#insertnode23-k-v)
   - [splitLeaf(Node23, K, V)](#splitleafnode23-k-v)
   - [splitInternal(Node23, Split, int)](#splitinternalnode23-split-int)
   - [inorder(Node23, ArrayList)](#inordernode23-arraylist)
   - [deleteRec(Node23, K)](#deleterecnode23-k)
   - [findMinNode(Node23)](#findminnodenode23)
   - [removeKeyAt(Node23, int)](#removekeyatnode23-int)
   - [fixUnderflow(Node23, int)](#fixunderflownode23-int)
   - [rotateFromLeft(Node23, int)](#rotatefromleftnode23-int)
   - [rotateFromRight(Node23, int)](#rotatefromrightnode23-int)
   - [combineWithLeft(Node23, int)](#combinewithleftnode23-int)
   - [combineWithRight(Node23, int)](#combinewithright-node23-int)
8. [HW2 클래스](#hw2-클래스)
   - [main(String[] args)](#mainstring-args)
   - [print_tree(Tree23)](#print_treetree23)

---

## 개요

이 파일은 **2-3 트리(2-3 Tree)** 자료구조를 Java로 구현한 코드입니다.

### 2-3 트리란?
- 각 노드가 **1개 또는 2개의 키**를 가질 수 있는 균형 탐색 트리
- 1개의 키를 가진 노드(2-노드): 자식이 **2개**
- 2개의 키를 가진 노드(3-노드): 자식이 **3개**
- **모든 리프 노드는 동일한 깊이**에 위치 → 완벽한 균형 트리
- 삽입/삭제/탐색 모두 **O(log N)** 보장

### 주요 특징
| 속성 | 값 |
|------|-----|
| 제네릭 타입 | `K extends Comparable<K>`, `V` |
| 삽입 전략 | 하향식(top-down 아님), 분할(split) 방식으로 재귀 후 상향 전파 |
| 삭제 전략 | 중위 후계자(in-order successor)로 대체 후 리프 삭제, 언더플로우 보정 |

---

## 전체 구조

```
Tree23<K, V>
├── (내부 클래스) Node23<K, V>     ← 노드 구조 정의
├── (내부 클래스) Split            ← 노드 분할 결과 전달 객체
├── (필드) root, size, inserted
├── (공개) isEmpty, size, contains, get, put, keys, depth, delete
└── (비공개) insert, splitLeaf, splitInternal, inorder,
             deleteRec, findMinNode, removeKeyAt,
             fixUnderflow, rotateFromLeft, rotateFromRight,
             combineWithLeft, combineWithRight
```

---

## 내부 클래스: `Node23<K, V>`

```java
private static class Node23<K, V> {
    int n;                          // 현재 노드에 저장된 키의 수 (1 또는 2, 또는 삭제 직후 0)
    private final Object[] keys   = new Object[2];  // 최대 2개의 키
    private final Object[] values = new Object[2];  // 최대 2개의 값
    private final Object[] child  = new Object[3];  // 최대 3개의 자식 포인터
    ...
}
```

### 노드 구조 시각화

```
  [ key0 | key1 ]        ← 2-노드(n=1): key0만 사용, 3-노드(n=2): key0, key1 사용
  /      |      \
ch(0)  ch(1)  ch(2)      ← 2-노드는 ch(0), ch(1)만 사용
```

### 메서드 상세

| 메서드 | 역할 |
|--------|------|
| `boolean leaf()` | `child[0] == null` 이면 리프 노드. 자식이 아예 없으면 리프 |
| `K key(int i)` | `keys[i]`를 `K`로 언체크 캐스팅하여 반환 |
| `V val(int i)` | `values[i]`를 `V`로 언체크 캐스팅하여 반환 |
| `Node23<K,V> ch(int i)` | `child[i]`를 `Node23<K,V>`로 캐스팅하여 반환 |
| `void key(int i, K k)` | `keys[i] = k` 설정 |
| `void val(int i, V v)` | `values[i] = v` 설정 |
| `void ch(int i, Node23<K,V> c)` | `child[i] = c` 설정 |

> **설계 포인트**: `Object[]`를 사용한 이유는 Java에서 제네릭 배열(`new K[2]`)을 직접 생성할 수 없기 때문입니다. `@SuppressWarnings("unchecked")`로 경고를 억제합니다.

---

## 내부 클래스: `Split`

```java
private class Split {
    K key; V value;
    Node23<K, V> left; Node23<K, V> right;

    Split(K key, V value, Node23<K, V> left, Node23<K, V> right) {
        this.key = key; this.value = value;
        this.left = left; this.right = right;
    }
}
```

### 역할
노드가 **가득 찼을 때(3개의 키)** 분할이 발생하며, 분할의 결과를 부모 노드에 "올려 보내기" 위한 **전달 객체(DTO)** 입니다.

```
분할 전:              분할 후 Split 객체:
[ k0 | k1 | k2 ]  →  key=k1, value=v1
                      left  = [k0]
                      right = [k2]
```

- `key` / `value`: 부모로 올라갈 중간 키와 값
- `left`: 중간 키보다 **작은** 키들이 있는 왼쪽 새 노드
- `right`: 중간 키보다 **큰** 키들이 있는 오른쪽 새 노드

---

## Tree23 필드

```java
private Node23<K, V> root;    // 트리의 루트 노드
private int size = 0;         // 전체 키(단어) 개수
private boolean inserted;     // 삽입 시 새 키가 실제 추가되었는지 여부 (업데이트 구분용)
```

> `inserted` 플래그는 `put()` 호출 시 단순 값 업데이트인지 새 키 삽입인지 구분하여 `size`를 올바르게 관리하기 위해 사용됩니다.

---

## 공개 메서드 (Public Methods)

---

### `isEmpty()`

```java
public boolean isEmpty() { return size == 0; }
```
트리가 비어있으면 `true` 반환. 단순 크기 비교.

---

### `size()`

```java
public int size() { return size; }
```
현재 저장된 키의 개수 반환.

---

### `contains(K key)`

```java
public boolean contains(K key) { return get(key) != null; }
```
`get(key)`가 `null`이 아니면 해당 키가 존재한다고 판단.

---

### `get(K key)`

```java
public V get(K key) {
    Node23<K, V> x = root;                           // (1) 루트부터 탐색 시작
    while (x != null) {                              // (2) null(리프 아래)이 될 때까지 반복
        int idx = 0;
        while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;
        // (3) 현재 노드 내에서 key보다 크지 않은 첫 번째 위치 idx를 찾음
        //     - key > keys[0] 이면 idx=1 로 이동 (3-노드일 때 keys[1]도 비교 가능)
        //     - key > keys[1] 이면 idx=2 로 이동
        if (idx < x.n && key.compareTo(x.key(idx)) == 0) return x.val(idx);
        // (4) 위치에서 key와 정확히 일치하면 값 반환
        x = x.ch(idx);
        // (5) 일치하지 않으면 idx번째 자식으로 내려감
        //     - idx=0: key < keys[0]  → 왼쪽 자식
        //     - idx=1: key0 < key < key1 → 중간 자식 (또는 2-노드라면 오른쪽)
        //     - idx=2: key > keys[1]  → 오른쪽 자식
    }
    return null;  // (6) 찾지 못한 경우
}
```

#### 탐색 경로 예시
```
트리:        [dog | fox]
            /     |     \
         [cat]  [elk]  [rat]

get("elk") 호출:
  x = [dog|fox], idx=0
  "elk" > "dog" → idx=1
  "elk" < "fox" → 멈춤
  "elk" != "fox" → x = ch(1) = [elk]
  x = [elk], idx=0
  "elk" == "elk" → return val(0) ✓
```

---

### `put(K key, V value)`

```java
public void put(K key, V value) {
    if (root == null) {
        // (1) 트리가 완전히 비어있는 경우: 루트 노드 직접 생성
        root = new Node23<>();
        root.n = 1;
        root.key(0, key);
        root.val(0, value);
        size++;
        return;
    }
    inserted = false;              // (2) 삽입 여부 초기화
    Split sp = insert(root, key, value);
    // (3) 재귀 삽입 수행. 루트까지 분할이 전파되면 sp != null
    if (sp != null) {
        // (4) 루트가 분할된 경우: 새 루트를 생성하여 트리 높이를 1 증가시킴
        Node23<K, V> nr = new Node23<>();
        nr.n = 1;
        nr.key(0, sp.key); nr.val(0, sp.value);  // 올라온 중간 키를 새 루트에 삽입
        nr.ch(0, sp.left); nr.ch(1, sp.right);   // 분할된 두 서브트리를 자식으로 연결
        root = nr;
    }
    if (inserted) size++;          // (5) 새 키가 삽입된 경우에만 size 증가
}
```

> **핵심**: 2-3 트리의 삽입은 항상 **리프에서 시작**되며, 분할이 필요하면 분할 결과를 위로 전파합니다. 루트까지 분할이 전달될 때만 트리 높이가 증가합니다.

---

### `keys()`

```java
public ArrayList<K> keys() {
    ArrayList<K> list = new ArrayList<>();
    inorder(root, list);   // 중위 순회로 정렬된 순서로 키 수집
    return list;
}
```
트리에 저장된 모든 키를 **오름차순**으로 반환합니다. 내부적으로 `inorder()` 재귀 메서드를 호출합니다.

---

### `depth()`

```java
public int depth() {
    int d = 0;
    Node23<K, V> x = root;
    while (x != null) {
        d++;           // 현재 레벨 카운트
        x = x.ch(0);  // 항상 가장 왼쪽 자식으로 이동
    }
    return d;
}
```

> **2-3 트리의 특성 활용**: 모든 리프가 동일 깊이에 있으므로, 루트에서 가장 왼쪽 경로만 따라가도 전체 깊이를 정확히 측정할 수 있습니다.

---

### `delete(K key)`

```java
public void delete(K key) {
    if (key == null) throw new IllegalArgumentException();  // (1) null 키 예외
    if (root == null) return;                               // (2) 빈 트리 예외
    if (!deleteRec(root, key)) return;                      // (3) 키가 없으면 종료
    size--;                                                 // (4) 성공 삭제 → size 감소
    if (root.n == 0) root = root.leaf() ? null : root.ch(0);
    // (5) 루트의 키가 0이 된 경우 처리:
    //     - 리프였다면(트리에 아무것도 없음) → root = null (트리 비움)
    //     - 내부 노드였다면 유일한 자식을 새 루트로 승격 (트리 높이 감소)
}
```

---

## 비공개 메서드 (Private Methods)

---

### `insert(Node23, K, V)`

> **가장 핵심적인 재귀 삽입 메서드**

```java
private Split insert(Node23<K, V> x, K key, V value) {

    // --- Step 1: 현재 노드에서 삽입 위치 idx 탐색 ---
    int idx = 0;
    while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;
    // key가 들어갈 위치(idx)를 찾음
    // 예) n=2, keys=[B, D], key=C → idx=1 (B<C이므로 idx++, C<D이므로 멈춤)

    // --- Step 2: 키 중복 체크 (업데이트) ---
    if (idx < x.n && key.compareTo(x.key(idx)) == 0) {
        x.val(idx, value);   // 기존 키: 값만 업데이트
        return null;         // 분할 불필요
    }

    // --- Step 3: 리프 노드인 경우 ---
    if (x.leaf()) {
        if (x.n == 2) return splitLeaf(x, key, value);
        // 이미 2개의 키가 있으면 분할 필요 → splitLeaf() 호출

        // 여유 공간이 있으면(n==1) 키를 직접 삽입
        for (int i = x.n; i > idx; i--) {
            x.key(i, x.key(i - 1));   // 기존 키들을 오른쪽으로 한 칸씩 이동
            x.val(i, x.val(i - 1));
        }
        x.key(idx, key);    // 빈 자리에 새 키 삽입
        x.val(idx, value);
        x.n++;              // 키 개수 증가
        inserted = true;    // 새 키 삽입 성공 플래그 설정
        return null;
    }

    // --- Step 4: 내부 노드인 경우 → 자식으로 재귀 내려감 ---
    Split sp = insert(x.ch(idx), key, value);
    if (sp == null) return null;   // 자식에서 분할 없었음 → 완료

    // 자식에서 분할 발생 (sp != null)
    if (x.n == 2) return splitInternal(x, sp, idx);
    // 현재 노드도 가득 찼으면 내부 노드 분할

    // 현재 노드에 여유 공간이 있으면 sp의 키를 직접 흡수
    for (int i = x.n; i > idx; i--) {
        x.key(i, x.key(i - 1));
        x.val(i, x.val(i - 1));
        x.ch(i + 1, x.ch(i));     // 자식 포인터도 함께 이동
    }
    x.key(idx, sp.key);            // 올라온 중간 키 삽입
    x.val(idx, sp.value);
    x.ch(idx, sp.left);            // 분할된 왼쪽 자식 연결
    x.ch(idx + 1, sp.right);       // 분할된 오른쪽 자식 연결
    x.n++;
    return null;
}
```

#### 동작 흐름 요약
```
put("C", v) 호출 시 (트리에 [B,D] 노드가 루트인 경우):
  insert(root=[B,D], "C")
    → idx = 1  (B < C < D)
    → 리프이고 n==2 → splitLeaf(root, "C", v) 호출
    → Split(key="C", left=[B], right=[D]) 반환
  put()에서 sp != null → 새 루트 생성
  새 루트 = [C], ch(0)=[B], ch(1)=[D]
```

---

### `splitLeaf(Node23, K, V)`

> **가득 찬 리프 노드를 3개의 키로 분리하는 핵심 로직**

```java
private Split splitLeaf(Node23<K, V> x, K key, V value) {
    // x는 이미 2개의 키를 가진 리프 노드
    // key를 포함한 3개의 키를 오름차순 정렬: (k0, k1, k2)
    // k1이 부모로 올라가고, k0은 왼쪽, k2는 오른쪽 새 노드로 분리됨

    int idx = 0;
    while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;
    // 새 key가 들어갈 위치 탐색

    K k0; K k1; K k2;
    V v0; V v1; V v2;

    if (idx == 0) {
        // 새 key가 가장 작음: [key, x.key(0), x.key(1)]
        k0 = key;      k1 = x.key(0); k2 = x.key(1);
        v0 = value;    v1 = x.val(0); v2 = x.val(1);
    } else if (idx == 1) {
        // 새 key가 중간: [x.key(0), key, x.key(1)]
        k0 = x.key(0); k1 = key;      k2 = x.key(1);
        v0 = x.val(0); v1 = value;    v2 = x.val(1);
    } else {
        // 새 key가 가장 큼: [x.key(0), x.key(1), key]
        k0 = x.key(0); k1 = x.key(1); k2 = key;
        v0 = x.val(0); v1 = x.val(1); v2 = value;
    }

    // 왼쪽 노드: k0만 포함
    Node23<K, V> l = new Node23<>();
    l.n = 1; l.key(0, k0); l.val(0, v0);

    // 오른쪽 노드: k2만 포함
    Node23<K, V> r = new Node23<>();
    r.n = 1; r.key(0, k2); r.val(0, v2);

    inserted = true;  // 새 키 추가 확정
    return new Split(k1, v1, l, r);
    // k1을 부모로 전파, l과 r을 새로운 자식으로 전달
}
```

#### 시각화
```
분할 전:
  리프 [A | C] 에 B 삽입

분할 후:
  부모로 전파: Split(key=B, left=[A], right=[C])
  
       [B]        ← 부모에 흡수됨
      /   \
    [A]   [C]
```

---

### `splitInternal(Node23, Split, int)`

> **가득 찬 내부 노드를 분할하여 분할 결과를 상위 노드로 전파**

```java
private Split splitInternal(Node23<K, V> x, Split csp, int idx) {
    // x: 현재 가득 찬 내부 노드 (키 2개, 자식 3개)
    // csp: 자식에서 올라온 분할 결과
    // idx: csp가 올라온 자식의 위치
    
    // 3개의 키(기존 2개 + 올라온 1개)와 4개의 자식을 정렬하여 처리

    K k0, k1, k2;
    V v0, v1, v2;
    Node23<K,V> c0, c1, c2, c3;   // 4개의 자식

    if (idx == 0) {
        // 분할이 왼쪽 자식에서 발생
        // 키 순서: [csp.key, x.key(0), x.key(1)]
        k0 = csp.key;    k1 = x.key(0);  k2 = x.key(1);
        v0 = csp.value;  v1 = x.val(0);  v2 = x.val(1);
        c0 = csp.left;   c1 = csp.right; c2 = x.ch(1); c3 = x.ch(2);
    } else if (idx == 1) {
        // 분할이 중간 자식에서 발생
        // 키 순서: [x.key(0), csp.key, x.key(1)]
        k0 = x.key(0);   k1 = csp.key;   k2 = x.key(1);
        v0 = x.val(0);   v1 = csp.value; v2 = x.val(1);
        c0 = x.ch(0);    c1 = csp.left;  c2 = csp.right; c3 = x.ch(2);
    } else {
        // 분할이 오른쪽 자식에서 발생
        // 키 순서: [x.key(0), x.key(1), csp.key]
        k0 = x.key(0);   k1 = x.key(1);  k2 = csp.key;
        v0 = x.val(0);   v1 = x.val(1);  v2 = csp.value;
        c0 = x.ch(0);    c1 = x.ch(1);   c2 = csp.left; c3 = csp.right;
    }

    // k1이 부모로 올라가고, 왼쪽(k0)과 오른쪽(k2)으로 분할
    Node23<K, V> l = new Node23<>();
    l.n = 1; l.key(0, k0); l.val(0, v0);
    l.ch(0, c0); l.ch(1, c1);   // 왼쪽 2개의 자식

    Node23<K, V> r = new Node23<>();
    r.n = 1; r.key(0, k2); r.val(0, v2);
    r.ch(0, c2); r.ch(1, c3);   // 오른쪽 2개의 자식

    return new Split(k1, v1, l, r);
}
```

#### 시각화 (idx == 1인 경우)
```
분할 전 내부 노드:
  [A | C]  +  자식에서 올라온 Split(B, [자식L], [자식R])
  /    |    \
ch0  ch1  ch2

분할 후:
        [B]           ← 부모로 전파
       /   \
    [A]    [C]
   /   \  /   \
  c0  cL cR  c2   ← c0=ch0, cL=csp.left, cR=csp.right, c2=ch2
```

---

### `inorder(Node23, ArrayList)`

```java
private void inorder(Node23<K, V> x, ArrayList<K> list) {
    if (x == null) return;     // (1) 기저 조건: 빈 서브트리

    if (x.n == 1) {
        // (2) 2-노드 (키 1개, 자식 2개): 중위 순회
        inorder(x.ch(0), list);  // 왼쪽 서브트리
        list.add(x.key(0));      // 현재 키
        inorder(x.ch(1), list);  // 오른쪽 서브트리
    } else {
        // (3) 3-노드 (키 2개, 자식 3개): 확장된 중위 순회
        inorder(x.ch(0), list);  // 첫 번째 서브트리 (key0보다 작은 값들)
        list.add(x.key(0));      // 첫 번째 키
        inorder(x.ch(1), list);  // 두 번째 서브트리 (key0 ~ key1 사이 값들)
        list.add(x.key(1));      // 두 번째 키
        inorder(x.ch(2), list);  // 세 번째 서브트리 (key1보다 큰 값들)
    }
}
```

결과적으로 BST의 중위 순회와 동일하게 **오름차순 정렬된 키 목록**을 반환합니다.

---

### `deleteRec(Node23, K)`

> **삭제 재귀 메서드 — 가장 복잡한 로직**

```java
private boolean deleteRec(Node23<K, V> x, K key) {

    // --- Step 1: 현재 노드에서 key의 위치 탐색 ---
    int idx = 0;
    while (idx < x.n && key.compareTo(x.key(idx)) > 0) idx++;

    if (idx < x.n && key.compareTo(x.key(idx)) == 0) {
        // key를 현재 노드에서 발견한 경우

        if (x.leaf()) {
            // (2a) 리프 노드: 직접 삭제
            removeKeyAt(x, idx);
            return true;
        }

        // (2b) 내부 노드: 중위 후계자(in-order successor)로 대체 후 삭제
        Node23<K, V> succ = findMinNode(x.ch(idx + 1));
        // idx+1번째 서브트리에서 가장 작은 값(리프)을 찾음
        K sk = succ.key(0);
        x.key(idx, sk);             // 현재 위치를 후계자 키로 교체
        x.val(idx, succ.val(0));    // 현재 위치를 후계자 값으로 교체

        boolean removed = deleteRec(x.ch(idx + 1), sk);
        // 후계자가 있던 서브트리에서 후계자를 재귀 삭제
        if (removed) fixUnderflow(x, idx + 1);
        // 삭제 후 언더플로우 보정
        return removed;
    }

    // key가 현재 노드에 없음 → 자식으로 내려감
    Node23<K, V> next = x.ch(idx);
    if (next == null) return false;           // 리프 아래 → 키 없음
    boolean removed = deleteRec(next, key);
    if (removed) fixUnderflow(x, idx);        // 자식 삭제 후 언더플로우 확인
    return removed;
}
```

#### 중위 후계자 삭제 예시
```
트리:        [dog]
            /     \
         [cat]  [fox | rat]

delete("dog") 호출:
  idx=0, "dog" == key(0) → 내부 노드
  succ = findMinNode(ch(1)=[fox|rat]) → [fox|rat]의 key(0) = "fox"
  "dog" 자리를 "fox"로 교체
  deleteRec(ch(1)=[fox|rat], "fox") → 리프에서 "fox" 삭제
  결과:
       [fox]
      /     \
   [cat]   [rat]
```

---

### `findMinNode(Node23)`

```java
private Node23<K, V> findMinNode(Node23<K, V> x) {
    while (!x.leaf()) x = x.ch(0);  // 리프가 될 때까지 왼쪽 자식으로만 이동
    return x;
    // 반환값: 서브트리에서 가장 작은 키를 가진 리프 노드
}
```

2-3 트리에서 **중위 후계자(in-order successor)**를 찾기 위해 사용됩니다. 항상 가장 왼쪽(최솟값) 리프까지 내려갑니다.

---

### `removeKeyAt(Node23, int)`

```java
private void removeKeyAt(Node23<K, V> x, int idx) {
    if (x.n == 2) {
        // 3-노드에서 삭제 → 2-노드가 됨 (정상 상태 유지)
        if (idx == 0) {
            x.key(0, x.key(1));   // key(1)을 key(0)으로 당김
            x.val(0, x.val(1));
        }
        // idx == 1이면 그냥 key(1)만 null로 지우면 됨
        x.key(1, null);
        x.val(1, null);
        x.n = 1;
    } else {
        // 2-노드에서 삭제 → 언더플로우 발생 (n=0인 빈 노드)
        x.key(0, null);
        x.val(0, null);
        x.n = 0;  // ← 이 상태를 상위에서 fixUnderflow()로 보정해야 함
    }
}
```

> **언더플로우**: n==1인 노드에서 키를 삭제하면 n==0이 됩니다. 이는 2-3 트리 규칙 위반이므로 `fixUnderflow()`로 보정해야 합니다.

---

### `fixUnderflow(Node23, int)`

> **삭제 후 불균형(언더플로우)을 복구하는 전략 결정 메서드**

```java
private void fixUnderflow(Node23<K, V> parent, int idx) {
    Node23<K, V> child = parent.ch(idx);
    if (child == null || child.n > 0) return;
    // 언더플로우가 없으면 바로 종료

    // 형제 노드 파악
    Node23<K, V> left  = idx > 0       ? parent.ch(idx - 1) : null;  // 왼쪽 형제
    Node23<K, V> right = idx < parent.n ? parent.ch(idx + 1) : null;  // 오른쪽 형제

    // 전략 1: 왼쪽 형제가 3-노드 → 왼쪽에서 빌려옴 (회전)
    if (left != null && left.n == 2) {
        rotateFromLeft(parent, idx);
        return;
    }
    // 전략 2: 오른쪽 형제가 3-노드 → 오른쪽에서 빌려옴 (회전)
    if (right != null && right.n == 2) {
        rotateFromRight(parent, idx);
        return;
    }
    // 전략 3: 양쪽 모두 2-노드 → 합병(combine)
    if (left != null)        combineWithLeft(parent, idx);
    else if (right != null)  combineWithRight(parent, idx);
    // 합병 후 parent의 키가 줄어들므로 상위에서 다시 fixUnderflow() 호출됨
}
```

#### 전략 선택 우선순위
```
언더플로우 발생
      ↓
왼쪽 형제가 3-노드? → YES → rotateFromLeft (빌려오기)
      ↓ NO
오른쪽 형제가 3-노드? → YES → rotateFromRight (빌려오기)
      ↓ NO
왼쪽 형제 있음? → YES → combineWithLeft (합병)
      ↓ NO
오른쪽 형제와 합병 → combineWithRight
```

---

### `rotateFromLeft(Node23, int)`

> **왼쪽 형제 3-노드에서 키를 빌려와 언더플로우를 해소 (오른쪽 회전)**

```java
private void rotateFromLeft(Node23<K, V> parent, int idx) {
    Node23<K, V> child = parent.ch(idx);       // 언더플로우 노드
    Node23<K, V> left  = parent.ch(idx - 1);   // 키를 빌려줄 왼쪽 형제 (3-노드)

    // Step 1: 부모의 키를 child로 내림 (child의 맨 앞에 삽입)
    child.key(0, parent.key(idx - 1));
    child.val(0, parent.val(idx - 1));

    // Step 2: 내부 노드이면 left의 오른쪽 자식(ch(2))을 child로 이전
    if (!left.leaf()) {
        child.ch(1, child.ch(0));   // child의 기존 자식을 오른쪽으로 밈
        child.ch(0, left.ch(2));    // left의 오른쪽 자식을 child의 왼쪽으로
        left.ch(2, null);
    }
    child.n = 1;

    // Step 3: left의 오른쪽 키를 부모로 올림
    parent.key(idx - 1, left.key(1));
    parent.val(idx - 1, left.val(1));
    left.key(1, null);
    left.val(1, null);
    left.n = 1;
}
```

#### 시각화
```
회전 전:
    parent: [M | ...]
           /     \
    left:[A|G]  child:[](빈 노드)

회전 후:
    parent: [G | ...]
           /     \
    left:[A]   child:[M]
```

---

### `rotateFromRight(Node23, int)`

> **오른쪽 형제 3-노드에서 키를 빌려와 언더플로우를 해소 (왼쪽 회전)**

```java
private void rotateFromRight(Node23<K, V> parent, int idx) {
    Node23<K, V> child = parent.ch(idx);       // 언더플로우 노드
    Node23<K, V> right = parent.ch(idx + 1);   // 키를 빌려줄 오른쪽 형제 (3-노드)

    // Step 1: 부모의 키를 child로 내림 (child 맨 뒤에 삽입)
    child.key(0, parent.key(idx));
    child.val(0, parent.val(idx));

    // Step 2: 내부 노드이면 right의 왼쪽 자식(ch(0))을 child로 이전
    if (!right.leaf()) {
        child.ch(1, right.ch(0));    // right의 왼쪽 자식을 child의 오른쪽으로
        right.ch(0, right.ch(1));    // right의 자식들을 왼쪽으로 이동
        right.ch(1, right.ch(2));
        right.ch(2, null);
    }
    child.n = 1;

    // Step 3: right의 왼쪽 키를 부모로 올림
    parent.key(idx, right.key(0));
    parent.val(idx, right.val(0));
    right.key(0, right.key(1));    // right의 키를 왼쪽으로 이동
    right.val(0, right.val(1));
    right.key(1, null);
    right.val(1, null);
    right.n = 1;
}
```

#### 시각화
```
회전 전:
    parent: [... | M]
                  /     \
         child:[]   right:[P|Z]

회전 후:
    parent: [... | P]
                  /     \
         child:[M]   right:[Z]
```

---

### `combineWithLeft(Node23, int)`

> **왼쪽 형제와 합병 — 부모의 키 하나를 내려 3-노드 생성**

```java
private void combineWithLeft(Node23<K, V> parent, int idx) {
    Node23<K, V> left  = parent.ch(idx - 1);   // 왼쪽 형제 (2-노드)
    Node23<K, V> child = parent.ch(idx);        // 언더플로우 노드 (n=0)

    // Step 1: 부모의 구분 키를 left로 내리고 left를 3-노드로 만듦
    left.key(1, parent.key(idx - 1));
    left.val(1, parent.val(idx - 1));
    left.n = 2;

    // Step 2: 내부 노드이면 child의 (유일한) 자식을 left의 세 번째 자식으로 이전
    if (!left.leaf()) left.ch(2, child.ch(0));

    // Step 3: 부모에서 구분 키와 child 포인터를 제거
    if (parent.n == 2 && idx - 1 == 0) {
        parent.key(0, parent.key(1));   // 부모가 3-노드였다면 남은 키를 앞으로 이동
        parent.val(0, parent.val(1));
    }
    parent.key(parent.n - 1, null);
    parent.val(parent.n - 1, null);
    for (int i = idx; i < parent.n; i++) parent.ch(i, parent.ch(i + 1));
    parent.ch(parent.n, null);
    parent.n--;
    // 부모의 키가 1개 줄어들었으므로 부모가 언더플로우(n=0)가 될 수도 있음
    // → 상위에서 다시 fixUnderflow() 호출됨
}
```

#### 시각화
```
합병 전:
    parent: [M]
           /   \
        left:[A]  child:[]

합병 후:
    parent: [] (n=0 → 상위에서 처리)
    left 가 새로운 자식:
        left: [A | M]    ← left에 parent의 M을 흡수
```

---

### `combineWithRight(Node23, int)`

> **오른쪽 형제와 합병 — 부모의 키 하나를 내려 언더플로우 노드를 3-노드로 확장**

```java
private void combineWithRight(Node23<K, V> parent, int idx) {
    Node23<K, V> child = parent.ch(idx);       // 언더플로우 노드 (n=0)
    Node23<K, V> right = parent.ch(idx + 1);   // 오른쪽 형제 (2-노드)

    // Step 1: 부모의 구분 키 + right의 키를 child에 흡수 → 3-노드 생성
    child.key(0, parent.key(idx));
    child.val(0, parent.val(idx));
    child.key(1, right.key(0));
    child.val(1, right.val(0));
    child.n = 2;

    // Step 2: 내부 노드이면 right의 자식들을 child로 이전
    if (!right.leaf()) {
        child.ch(1, right.ch(0));
        child.ch(2, right.ch(1));
    }

    // Step 3: 부모에서 구분 키와 right 포인터 제거
    if (parent.n == 2 && idx == 0) {
        parent.key(0, parent.key(1));
        parent.val(0, parent.val(1));
    }
    parent.key(parent.n - 1, null);
    parent.val(parent.n - 1, null);
    for (int i = idx + 1; i < parent.n; i++) parent.ch(i, parent.ch(i + 1));
    parent.ch(parent.n, null);
    parent.n--;
}
```

#### 시각화
```
합병 전:
    parent: [M]
           /   \
       child:[]  right:[Z]

합병 후:
    parent: [] (n=0 → 상위에서 처리)
    child: [M | Z]    ← parent의 M + right의 Z를 child에 흡수
```

---

## HW2 클래스

---

### `main(String[] args)`

```java
public static void main(String[] args) {
    Tree23<String, Integer> st = new Tree23<>();  // (1) 단어 빈도 저장용 2-3 트리 생성

    Scanner sc = new Scanner(System.in);
    System.out.print("입력 파일 이름? ");
    String fname = sc.nextLine();                  // (2) 파일 경로 입력
    System.out.print("난수 생성을 위한 seed 값? ");
    Random rand = new Random(sc.nextLong());        // (3) 랜덤 셔플용 시드 입력
    sc.close();

    try {
        sc = new Scanner(new File(fname));         // (4) 파일 열기
        long start = System.currentTimeMillis();

        while (sc.hasNext()) {
            String word = sc.next();               // (5) 단어 하나씩 읽기
            if (!st.contains(word))
                st.put(word, 1);                   // (6a) 처음 등장: 빈도 1로 삽입
            else
                st.put(word, st.get(word) + 1);    // (6b) 재등장: 빈도 +1 업데이트
        }

        long end = System.currentTimeMillis();
        System.out.println("입력 완료: 소요 시간 = " + (end - start) + "ms");

        System.out.println("### 생성 시점의 트리 정보");
        print_tree(st);                            // (7) 삽입 후 트리 상태 출력

        ArrayList<String> keyList = (ArrayList<String>) st.keys();
        Collections.shuffle(keyList, rand);        // (8) 키 목록을 랜덤하게 섞음
        int loopCount = (int)(keyList.size() * 0.95);  // (9) 전체의 95%를 삭제

        for (int i = 0; i < loopCount; i++) {
            st.delete(keyList.get(i));             // (10) 하나씩 삭제
        }

        System.out.println("\n### 키 삭제 후 트리 정보");
        print_tree(st);                            // (11) 삭제 후 트리 상태 출력

    } catch (FileNotFoundException e) { e.printStackTrace(); }
    if (sc != null) sc.close();
}
```

#### 전체 흐름 요약
```
① 파일에서 단어 읽기 → Tree23에 (단어, 빈도수) 삽입
② print_tree() → 삽입 완료 후 통계 출력
③ 키 목록을 랜덤 셔플 후 95% 삭제
④ print_tree() → 삭제 완료 후 통계 출력
```

---

### `print_tree(Tree23)`

```java
private static void print_tree(Tree23<String, Integer> st) {
    System.out.println("등록된 단어 수 = " + st.size());   // 총 고유 단어 수
    System.out.println("트리의 깊이 = " + st.depth());      // 트리 높이

    String maxKey = "";
    int maxValue = 0;
    for (String word : st.keys()) {           // 모든 단어 순회
        if (st.get(word) > maxValue) {
            maxValue = st.get(word);           // 최대 빈도 갱신
            maxKey = word;                     // 최빈 단어 갱신
        }
    }
    System.out.println("가장 빈번히 나타난 단어와 빈도수: " + maxKey + " " + maxValue);
}
```

트리의 3가지 정보를 출력합니다:
1. **등록된 단어 수** (`size()`) — 고유 단어 개수
2. **트리의 깊이** (`depth()`) — 루트~리프 거리
3. **최빈 단어와 빈도수** — 선형 스캔으로 최댓값 탐색

---

## 시간 복잡도 정리

| 연산 | 시간 복잡도 | 근거 |
|------|------------|------|
| `get` | O(log N) | 트리 높이만큼 순회 |
| `put` | O(log N) | 삽입 + 분할 상향 전파 |
| `delete` | O(log N) | 삭제 + 언더플로우 상향 보정 |
| `keys` | O(N) | 전체 중위 순회 |
| `depth` | O(log N) | 왼쪽 경로만 순회 |
| `contains` | O(log N) | `get` 호출 |

> 2-3 트리는 **항상 완벽하게 균형**을 유지하므로 최악의 경우에도 O(log N)이 보장됩니다.

---

## 삽입 vs 삭제 흐름 비교

```
삽입 (put/insert):
  하향(top-down): 탐색 위치까지 내려감
  상향(bottom-up): 분할(Split)을 루트 방향으로 전파
  루트 분할 시 트리 높이 +1

삭제 (delete/deleteRec):
  하향: 삭제 대상 탐색 (내부 노드면 중위 후계자로 대체)
  상향: fixUnderflow 보정을 루트 방향으로 전파
       └─ 회전(rotate): 형제에서 빌리기 → 트리 높이 불변
       └─ 합병(combine): 부모 키 흡수 → 부모 키 감소
  루트 키가 0개가 되면 트리 높이 -1
```

