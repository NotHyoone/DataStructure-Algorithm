import java.util.ArrayList;

// BST의 개별 노드를 표현하는 클래스
class Node_BST<K, V> {
    K key;                           // 노드의 키
    V value;                         // 노드가 저장하는 값
    Node_BST<K, V> left, right;      // 왼쪽/오른쪽 자식 포인터
    int N;                           // 이 노드를 루트로 하는 서브트리의 노드 개수
    int aux;                         // 필요 시 보조 정보 저장용 필드
    Node_BST<K, V> parent;           // 부모 노드 포인터

    // 생성자: 새 노드는 자기 자신만 포함하므로 N=1
    public Node_BST(K key, V val) {
        this.key = key;              // 전달받은 키를 저장
        this.value = val;            // 전달받은 값을 저장
        this.N = 1;                  // 단일 노드 서브트리 크기
    }

    // aux 필드 조회 메서드
    public int getAux() {
        return aux;                  // 보조 정보 반환
    }

    // aux 필드 설정 메서드
    public void setAux(int value) {
        aux = value;                 // 보조 정보 갱신
    }
}

// 제네릭 BST 구현: K는 비교 가능해야 하므로 Comparable 제약을 둠
public class BST<K extends Comparable<K>, V> {
    protected Node_BST<K, V> root;   // 트리의 루트 노드

    // 전체 트리 크기 반환(루트가 null이면 0)
    public int size() {
        return (root != null) ? root.N : 0; // 루트 기준 서브트리 크기를 그대로 사용
    }

    // key를 탐색하며, key를 가진 노드 또는 삽입이 일어날 부모 노드를 반환
    protected Node_BST<K, V> treeSearch(K key) {
        Node_BST<K, V> x = root;         // 탐색 시작점은 루트

        while (true) {                   // 찾거나 더 내려갈 수 없을 때까지 반복
            int cmp = key.compareTo(x.key); // 찾는 key와 현재 노드 key 비교

            if (cmp == 0) {
                return x;                // 같은 키를 찾았으므로 해당 노드 반환
            } else if (cmp < 0) {       // 찾는 key가 더 작으면 왼쪽으로 이동
                if (x.left == null) {
                    return x;            // 왼쪽 자식이 없으면 현재 노드가 삽입 부모
                } else {
                    x = x.left;          // 왼쪽 자식으로 내려가 탐색 계속
                }
            } else {                     // 찾는 key가 더 크면 오른쪽으로 이동
                if (x.right == null) {
                    return x;            // 오른쪽 자식이 없으면 현재 노드가 삽입 부모
                } else {
                    x = x.right;         // 오른쪽 자식으로 내려가 탐색 계속
                }
            }
        }
    }

    // key에 대응하는 value를 반환(없으면 null)
    public V get(K key) {
        if (root == null) {
            return null;                 // 빈 트리에서는 어떤 key도 찾을 수 없음
        }

        Node_BST<K, V> x = treeSearch(key); // key 또는 삽입 부모 위치까지 탐색

        if (key.equals(x.key)) {
            return x.value;              // key를 정확히 찾았으므로 value 반환
        } else {
            return null;                 // 탐색 종료 노드가 key와 다르면 미존재
        }
    }

    // key-value를 삽입하거나, 이미 있으면 value를 갱신
    public void put(K key, V val) {
        if (root == null) {                 // 트리가 비어 있으면
            root = new Node_BST<K, V>(key, val); // 새 노드를 루트로 생성
            return;                         // 루트 생성으로 삽입 완료
        }

        Node_BST<K, V> x = treeSearch(key); // 같은 key 또는 삽입 부모 탐색
        int cmp = key.compareTo(x.key);     // 탐색 종료 노드와 key 비교

        if (cmp == 0) {
            x.value = val;                  // 같은 key가 있으면 value만 갱신
        } else {                            // 같은 key가 없으면 새 노드 삽입
            Node_BST<K, V> newNode = new Node_BST<K, V>(key, val); // 새 노드 생성

            if (cmp < 0) {
                x.left = newNode;           // 부모의 왼쪽 자식으로 연결
            } else {
                x.right = newNode;          // 부모의 오른쪽 자식으로 연결
            }

            newNode.parent = x;             // 역참조를 위해 부모 포인터 설정
            rebalanceInsert(newNode);       // 삽입으로 증가한 N 값을 조상에 반영
        }
    }

    // 삽입 후 조상들의 서브트리 크기를 1씩 증가
    protected void rebalanceInsert(Node_BST<K, V> x) {
        resetSize(x.parent, 1);             // 부모부터 루트까지 +1 누적
    }

    // 삭제 후 조상들의 서브트리 크기를 1씩 감소
    protected void rebalanceDelete(Node_BST<K, V> p, Node_BST<K, V> deleted) {
        resetSize(p, -1);                   // 삭제된 노드의 부모부터 루트까지 -1 누적
    }

    // 시작 노드 x부터 루트까지 올라가며 N 값을 value만큼 누적 보정
    private void resetSize(Node_BST<K, V> x, int value) {
        for (; x != null; x = x.parent) {   // 부모 포인터를 따라 루트 방향으로 이동
            x.N += value;                   // 각 조상 노드의 서브트리 크기 보정
        }
    }

    // 중위 순회 결과(오름차순 키 목록)를 Iterable로 반환
    public Iterable<K> keys() {
        if (root == null) {
            return null;                    // 현재 구현 정책: 빈 트리면 null 반환
        }

        ArrayList<K> keyList = new ArrayList<K>(size()); // 결과 저장 버퍼 생성
        inorder(root, keyList);             // 중위 순회로 오름차순 키 수집
        return keyList;                     // 수집된 키 목록 반환
    }

    // 중위 순회: left -> self -> right 순서로 key 추가
    private void inorder(Node_BST<K, V> x, ArrayList<K> keyList) {
        if (x != null) {                    // null이 아니면 현재 서브트리 처리
            inorder(x.left, keyList);       // 1) 왼쪽 서브트리 방문
            keyList.add(x.key);             // 2) 현재 노드 키 기록
            inorder(x.right, keyList);      // 3) 오른쪽 서브트리 방문
        }
    }

    // key를 갖는 노드를 삭제
    public void delete(K key) {
        if (root == null) {
            return;                         // 빈 트리면 삭제 대상 없음
        }

        Node_BST<K, V> x, y, p;             // x:대상, y:후속자, p:부모
        x = treeSearch(key);                // 삭제 대상 또는 삽입 부모 탐색

        if (!key.equals(x.key)) {           // 정확히 같은 key를 못 찾은 경우
            return;                         // 삭제할 노드가 없으므로 종료
        }

        // 루트이거나 자식이 2개인 노드는 케이스 분기가 많아 별도 처리
        if (x == root || isTwoNode(x)) {
            if (isLeaf(x)) {                // 루트가 리프인 특수 상황
                root = null;                // 유일한 노드를 제거해 빈 트리로 만듦
                return;
            } else if (!isTwoNode(x)) {     // 루트인데 자식이 1개인 경우
                root = (x.right == null) ? x.left : x.right; // 유일한 자식을 루트로 승격
                root.parent = null;         // 루트의 부모는 항상 null
                return;
            } else {                        // 자식이 2개인 일반 삭제(루트 포함)
                y = min(x.right);           // 중위 후속자(오른쪽 서브트리 최소)
                x.key = y.key;              // 후속자의 key를 대상 노드에 복사
                x.value = y.value;          // 후속자의 value를 대상 노드에 복사
                p = y.parent;               // 실제 제거 노드(y)의 부모 기록

                // 후속자 y는 left가 없으므로 right만 재연결하면 구조가 유지됨
                relink(p, y.right, y == p.left);

                rebalanceDelete(p, y);      // 실제 제거(y) 기준으로 N 감소 반영
            }
        } else {                            // 루트가 아니고 자식이 0개 또는 1개인 경우
            p = x.parent;                   // 연결 수정의 기준이 되는 부모

            if (x.right == null) {
                relink(p, x.left, x == p.left); // 왼쪽 자식만 있거나 리프인 경우
            } else if (x.left == null) {
                relink(p, x.right, x == p.left); // 오른쪽 자식만 있는 경우
            }

            rebalanceDelete(p, x);          // x 제거로 줄어든 N 반영
        }
    }

    // key 존재 여부 확인(get 결과가 null이 아니면 존재)
    public boolean contains(K key) {
        return get(key) != null;            // 존재 여부를 boolean으로 변환
    }

    // 트리가 비어 있는지 확인
    public boolean isEmpty() {
        return root == null;                // 루트가 없으면 빈 트리
    }

    // 리프 노드 여부(자식이 둘 다 null)
    protected boolean isLeaf(Node_BST<K, V> x) {
        return x.left == null && x.right == null; // 양쪽 자식이 모두 없으면 리프
    }

    // 자식이 두 개인 내부 노드 여부
    protected boolean isTwoNode(Node_BST<K, V> x) {
        return x.left != null && x.right != null; // 양쪽 자식이 모두 있으면 2-자식 노드
    }

    // parent의 왼쪽/오른쪽 자식 링크를 child로 교체
    protected void relink(Node_BST<K, V> parent, Node_BST<K, V> child, boolean makeLeft) {
        if (child != null) {
            child.parent = parent;          // 새로 연결될 child의 부모 포인터 동기화
        }

        if (makeLeft) {
            parent.left = child;            // 부모의 왼쪽 링크를 교체
        } else {
            parent.right = child;           // 부모의 오른쪽 링크를 교체
        }
    }

    // 주어진 서브트리 x에서 최소 키 노드를 반환
    protected Node_BST<K, V> min(Node_BST<K, V> x) {
        while (x.left != null) {
            x = x.left;                     // 더 작은 key를 찾기 위해 왼쪽 끝까지 이동
        }
        return x;                           // 왼쪽 끝 노드가 최소 key
    }

    // 전체 트리의 최소 키 반환
    public K min() {
        if (root == null) {
            return null;                    // 빈 트리는 최소값 없음
        }

        Node_BST<K, V> x = root;            // 루트부터 시작
        while (x.left != null) {
            x = x.left;                     // 왼쪽 끝으로 이동
        }
        return x.key;                       // 최소 key 반환
    }

    // 전체 트리의 최대 키 반환
    public K max() {
        if (root == null) {
            return null;                    // 빈 트리는 최대값 없음
        }

        Node_BST<K, V> x = root;            // 루트부터 시작
        while (x.right != null) {
            x = x.right;                    // 오른쪽 끝으로 이동
        }
        return x.key;                       // 최대 key 반환
    }

    // key 이하의 최대 키(floor)를 반환
    public K floor(K key) {
        if (this.root == null || key == null) {
            return null;                    // 빈 트리 또는 null key는 floor 정의 불가
        }

        Node_BST<K, V> x = floor(root, key); // 재귀 탐색으로 floor 노드 탐색
        if (x == null) {
            return null;                    // key 이하 key가 하나도 없는 경우
        } else {
            return x.key;                   // 찾은 floor 노드의 key 반환
        }
    }

    // 서브트리 x에서 key의 floor를 갖는 노드를 반환
    private Node_BST<K, V> floor(Node_BST<K, V> x, K key) {
        if (x == null) {
            return null;                    // 더 내려갈 노드가 없으면 floor 없음
        }

        int cmp = key.compareTo(x.key);     // key와 현재 노드 key 비교

        if (cmp == 0) {
            return x;                       // 정확히 같은 key면 그 노드가 floor
        }

        if (cmp < 0) {
            return floor(x.left, key);      // key가 더 작으면 왼쪽에서만 floor 가능
        }

        Node_BST<K, V> t = floor(x.right, key); // key가 더 크면 오른쪽에서 더 큰 후보 탐색
        if (t != null) {
            return t;                       // 오른쪽에서 찾은 후보가 있으면 그게 더 근접
        } else {
            return x;                       // 없으면 현재 노드가 최선의 floor
        }
    }

    // key보다 작은 키의 개수(rank)를 반환
    public int rank(K key) {
        if (root == null || key == null) {
            return 0;                       // 빈 트리/null key는 0으로 처리
        }

        Node_BST<K, V> x = root;            // 탐색 포인터를 루트로 초기화
        int num = 0;                        // 누적된 "작은 키 개수"

        while (x != null) {                 // 탐색 포인터가 유효한 동안 반복
            int cmp = key.compareTo(x.key); // key와 현재 노드 비교

            if (cmp < 0) {
                x = x.left;                 // key가 더 작으므로 왼쪽에서 계속 탐색
            } else if (cmp > 0) {
                num += 1 + size(x.left);    // 왼쪽 서브트리 + 현재 노드는 모두 key보다 작음
                x = x.right;                // 오른쪽에서 추가로 작은 키 개수 탐색
            } else {
                num += size(x.left);        // 같으면 왼쪽 서브트리 크기만 더하면 완료
                break;                      // 정답 확정이므로 루프 종료
            }
        }

        return num;                         // 최종 rank 반환
    }

    // 서브트리 x의 크기 반환(null이면 0)
    private int size(Node_BST<K, V> x) {
        return (x != null) ? x.N : 0;       // null 보호 처리된 서브트리 크기
    }

    // rank번째(0-based)로 작은 키를 반환
    public K select(int rank) {
        if (root == null || rank < 0 || rank >= size()) {
            return null;                    // 트리가 비었거나 rank 범위가 잘못되면 null
        }

        Node_BST<K, V> x = root;            // 루트부터 rank 탐색 시작

        while (true) {                      // 유효한 rank라면 반드시 반환 지점에 도달
            int t = size(x.left);           // 현재 노드 왼쪽 서브트리의 노드 수

            if (rank < t) {
                x = x.left;                 // 찾는 순위가 왼쪽 구간에 있으므로 왼쪽 이동
            } else if (rank > t) {
                rank = rank - t - 1;        // 왼쪽 구간 + 현재 노드를 제외한 상대 순위로 변환
                x = x.right;                // 오른쪽 서브트리에서 계속 탐색
            } else {
                return x.key;               // rank == 왼쪽 크기면 현재 노드가 정답
            }
        }
    }
}
