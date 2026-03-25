import java.util.ArrayList;

class Node_BST<K, V> {
    K key;
    V value;
    Node_BST<K, V> left, right;
    int N;
    int aux;
    Node_BST<K, V> parent;
    public Node_BST(K key, V val) {
        this.key = key;
        this.value = val;
        this.N = 1;
    }
    public int getAux() {return aux;}
    public void setAux(int value) {aux = value;}

}

public class BST<K extends Comparable<K>, V>{
    protected Node_BST<K,V> root;
    public int size() {return (root != null) ? root.N : 0;}
    protected Node_BST<K,V> treeSearch(K key) {
        Node_BST<K,V> x = root;   // BST에 대한 모든 연산은 루트부터 시작
        while (true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            else if (cmp < 0) {
                if (x.left == null)     return x;
                else                    x = x.left;
            }
            else {
                if (x.right == null)    return x;
                else                    x = x.right;
            }
        }
    }

    public V get(K key) {
        if (root == null) return null;
        Node_BST<K,V> x =treeSearch(key);
        if (key.equals(x.key))
            return x.value;
        else
            return null;
    }

    public void put(K key, V val) {
        if (root == null)   { root = new Node_BST<K,V>(key,val); return; }
        Node_BST<K,V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0)   x.value = val;  // 업데이트
        else {  // 삽입
            Node_BST<K,V> newNode = new Node_BST<K,V>(key,val);
            if (cmp < 0)    x.left = newNode;
            else            x.right = newNode;
            newNode.parent = x;
            rebalanceInsert(newNode);
        }
    }
    protected void rebalanceInsert(Node_BST<K,V> x) {
        resetSize(x.parent, 1); // root까지 조상 노드들의 size를 1 증가
    }
    protected void rebalanceDelete(Node_BST<K,V> p, Node_BST<K,V> deleted) {
        resetSize(p, -1);   // root까지 조상 노드들의 size를 1 감소
    }
    private void resetSize(Node_BST<K,V> x, int value) {
        for (; x!=null; x=x.parent)
            x.N += value;
    }

    public Iterable<K> keys() {
        if (root == null) return null;
        ArrayList<K> keyList = new ArrayList<K>(size());
        inorder(root, keyList);
        return keyList;
    }
    private void inorder(Node_BST<K,V> x, ArrayList<K> keyList) {
        if (x != null) {
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }

    public void delete(K key) {
        if (root == null) return;
        Node_BST<K,V> x,y,p;
        x=treeSearch(key);

        // key가 없는 경우
        if (!key.equals(x.key))
            return;

        // 루트이거나 자식이 두 개인 경우
        if (x == root || isTwoNode(x)) {
            if (isLeaf(x)) {    // 루트가 리프 == 트리에 노드 하나인 경우
                root = null; return;
            } else if (!isTwoNode(x)) {   // 루트
                root = (x.right == null) ? x.left : x.right;    // 자식을 루트로
                root.parent = null;
                return;
            } else {    // 자식이 둘인 노드(루트 포함)
                y = min(x.right);   // inorder successor
                x.key = y.key;      // y를 x에 복사
                x.value = y.value;  // y를 x에 복사
                p = y.parent;
                // y의 자식을 p의 자식으로(y 삭제)
                relink(p, y.right, y == p.left);
                // y의 조상 노드들의 size 감소
                rebalanceDelete(p, y);
            }
        } else {    // 자식 <= 1 이고, 루트 아님
            p = x.parent;
            if (x.right == null) relink(p,x.left, x == p.left);
            else if (x.left == null) relink(p, x.right, x == p.left);
            rebalanceDelete(p, x);
        }
    }
    public boolean contains(K key) {return get(key) != null;}
    public boolean isEmpty() {return root == null;}

    protected boolean isLeaf(Node_BST<K, V> x) {
        return x.left == null && x.right == null;
    }
    protected boolean isTwoNode(Node_BST<K,V> x) {
        return x.left != null && x.right != null;
    }
    protected void relink(Node_BST<K,V> parent, Node_BST<K,V> child, boolean makeLeft) {
        if (child != null)  child.parent = parent;      // child를 parent의 자식으로
        if (makeLeft)       parent.left = child;        // 왼쪽 자식, 또는
        else                parent.right = child;       // 오른쪽 자식
    }

    protected Node_BST<K,V> min(Node_BST<K,V> x) {while(x.left != null) x = x.left; return x;}

    public K min() {    // 제일 작은 키를 반환
        if (root == null) return null;
        Node_BST<K,V> x =root;
        while(x.left != null)
            x = x.left; // 제일 왼쪽에 있는 노드
        return x.key;
    }

    public K max() {    // 제일 큰 키를 반환
        if (root == null) return null;
        Node_BST<K,V> x = root;
        while (x.right != null) // 제일 오른쪽에 있는 노드
            x = x.right;
        return x.key;
    }

    public K floor(K key) {
        if (this.root == null || key == null) return null;
        Node_BST<K,V> x = floor(root, key);
        if (x == null)  return null;
        else            return x.key;
    }

    private Node_BST<K,V> floor(Node_BST<K,V> x, K key) {
        if (x == null)  return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0)   return x;                   // key와 동일한 키를 가진 노드
        if (cmp < 0)    return floor(x.left, key);  // key보다 크다면 계속 왼쪽으로
        Node_BST<K,V> t = floor(x.right, key);      // key가 클 경우, 오른쪽으로
        if (t != null)  return t;                   // 오른쪽에 작은 키가 있을 경우
        else            return x;                   // 오른쪽에 작은 키가 없을 경우
    }

    public int rank(K key) {    // key보다 작은 키의 수
        if (root == null || key == null) return 0;
        Node_BST<K, V> x = root;
        int num = 0;
        while (x != null) {     // 루트부터 비교하면서 key보다 작은 키의 수를 합산
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) {             // key보다 작은 키를 갖는 노드를 발견
                num += 1 + size(x.left);    // 왼족 subtree의 노드 수를 합산
                x = x.right;                // 오른쪽 subtree도 계속 검사
            } else {                        // key값을 갖는 노드 : 왼쪽 subtree만 합산
                num += size(x.left); break;
            }
        }
        return num;
    }

    private int size(Node_BST<K,V> x) {return (x != null) ? x.N : 0; }

    public K select(int rank) { // rank 등수에 해당하는 키를 반환
        if (root == null || rank < 0 || rank >= size())
            return null;
        Node_BST<K,V> x= root;
        while(true) {
            int t = size(x.left);
            if (rank < t)           // 왼쪽 subtree의 노드 수가 rank보다 크면
                x = x.left;         // rank보다 작은 키가 왼쪽 subtree에 있으므로 왼쪽으로
            else if (rank > t) {    // 왼쪽 subtree의 노드 수가 rank보다 작으면
                rank = rank - t -1; // rank에서 왼쪽 subtree의 노드 수와 현재 노드를 제외한 수를 빼고
                x = x.right;        // 오른쪽 subtree로 이동
            } else                  // rank와 왼쪽 subtree의 노드 수가 같으면
                return x.key;       // 현재 노드의 키가 rank에 해당하는 키
        }
    }
}
