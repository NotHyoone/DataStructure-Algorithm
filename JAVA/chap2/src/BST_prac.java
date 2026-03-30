import java.util.ArrayList;
class Node_bstprac<K,V> {
    K key;
    V value;
    Node_bstprac<K,V> left, right, parent;

    int N;
    int aux;
    public Node_bstprac(K key, V value) {
        this.key = key; this.value = value;
        this.N = 1;
    }
    public int getAux() {return aux;}
    public void setAux(int value) { aux = value; }
}

public class BST_prac<K extends Comparable<K>, V> {
    protected Node_bstprac<K,V> root;

    public int size() {return (root != null) ? root.N : 0;}

    protected Node_bstprac<K,V> treeSearch(K key) {
        Node_bstprac<K,V> x = root;

        while(true) {
            int cmp = key.compareTo(x.key);

            if (cmp == 0) {
                return x;
            } else if (cmp < 0) {   // 왼쪽 서브트리
                if (x.left == null) return x;
                else x = x.left;
            } else {
                if (x.right == null) return x;
                else x = x.right;
            }
        }
    }

    public V get(K key) {
        if (root == null) return null;

        Node_bstprac<K,V> x = treeSearch(key);
        if (key.equals(x.key)) return x.value;
        else return null;
    }

    public void put(K key, V val) {
        if (root == null) {
            root = new Node_bstprac<K,V>(key,val);
            return;
        }

        Node_bstprac<K,V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);

        if (cmp == 0) {
            x.value = val;
        } else {
            Node_bstprac<K,V> newNode = new Node_bstprac<K,V>(key,val);

            if (cmp < 0) x.left = newNode;
            else x.right = newNode;

            newNode.parent = x;
            rebalanceInsert(newNode);
        }
    }

    protected void rebalanceInsert(Node_bstprac<K,V> x) {
        resetSize(x.parent, 1);
    }
    protected void rebalanceDelete(Node_bstprac<K,V> p, Node_bstprac<K,V> deleted) {
        resetSize(p, -1);
    }
    private void resetSize(Node_bstprac<K,V> x, int value) {
        for( ; x != null; x = x.parent) x.N += value;
    }

    public Iterable<K> keys() {
        if (root ==null) return null;

        ArrayList<K> keyList = new ArrayList<K>(size());
        inorder(root, keyList);
        return keyList;
    }

    private void inorder(Node_bstprac<K,V> x, ArrayList<K> keyList) {
        if (x != null) {
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }

    public void delete(K key) {
        if (root == null) return;

        Node_bstprac<K,V> x,y,p;
        x = treeSearch(key);
        if (!key.equals(x.key)) return;

        if (x == root || isTwoNode(x)) {
            if (isLeaf(x)) { root = null; return;}
            else if (!isTwoNode(x)) {
                root = (x.right == null) ? x.left : x.right;
                root.parent = null;
                return;
            } else {
                y = min(x.right);
                x.key = y.key;
                x.value = y.value;
                p = y.parent;

                relink(p, y.right, y == p.left);
                rebalanceDelete(p,y);
            }
        } else {    // 루트 아니고 자식 0개 또는 1개인 경우
            p = x.parent;
            if (x.right == null) {
                relink(p, x.left, x==p.left);
            } else if (x.left == null) {
                relink(p, x.right, x==p.left);
            }
            rebalanceDelete(p, x);
        }
    }

    public boolean contains(K key) {return get(key) != null;}
    public boolean isEmpty() {return root == null;}
    protected boolean isLeaf(Node_bstprac<K,V> x) {return x.left == null && x.right == null;}
    protected boolean isTwoNode(Node_bstprac<K,V> x) {return x.left != null && x.right != null;}
    protected void relink(Node_bstprac<K,V> parent, Node_bstprac<K,V> child, boolean makeLeft) {
        if (child != null) child.parent = parent;
        if (makeLeft) parent.left = child;
        else parent.right = child;
    }
    protected Node_bstprac<K,V> min(Node_bstprac<K,V> x) {
        while(x.left != null) x = x.left;
        return x;
    }
    public K min() {
        if (root == null) return null;
        Node_bstprac<K,V> x = root;
        while(x.left != null) x = x.left;
        return x.key;
    }
    public K max() {
        if (root == null) return null;
        Node_bstprac<K,V> x = root;
        while(x.right != null) x = x.right;
        return x.key;
    }

    public K floor(K key) {
        if (this.root == null || key == null) return null;
        Node_bstprac<K,V> x = floor(root,key);
        if (x == null) return null;
        else return x.key;
    }
    private Node_bstprac<K,V> floor(Node_bstprac<K,V> x, K key) {
        if (x==null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node_bstprac<K,V> t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }
}