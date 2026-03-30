import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class AVLTree<K extends Comparable<K>, V> {

    private static class Node<K,V> {
        K key; V value;
        int N, aux;
        Node<K,V> left, right, parent;

        Node(K key, V val) {
            this.key = key; this.value = val;
            this.N = 1;
            this.aux = 1;   // 리프 노드 높이 = 1
        }
    }
    private Node<K,V> root;

    public AVLTree() {
        this.root = null;
    }

    // -- [Helper Methods] --
    private int height(Node<K,V> x) {return (x == null) ? 0 : x.aux;}
    private int size(Node<K,V> x) {return (x == null) ? 0 : x.N;}

    private void recomputeHeight(Node<K,V> x) {
        if (x != null) x.aux = 1 + Math.max(height(x.left), height(x.right));
    }

    private void recomputeSize(Node<K,V> x) {
        if (x != null) x.N = 1 + size(x.left) + size(x.right);
    }

    private boolean isBalanced(Node<K,V> x) {
        return (x == null || Math.abs( height(x.left) - height(x.right)) <= 1);
    }

    // --- [Core AVL Restructuring] ---
    protected void relink(Node<K,V> parent, Node<K,V> child, boolean makeLeft) {
        if (child != null) child.parent = parent;
        if (makeLeft) parent.left = child;
        else parent.right = child;
    }

    protected void rotate(Node<K,V> x) {
        Node<K,V> y = x.parent;
        Node<K,V> z = y.parent;
        if (z == null) { root = x; x.parent = null; }
        else relink(z, x, y == z.left);

        if (x == y.left) {  // LL rotate
            relink(y, x.right, true);
            relink(x, y, false);
        } else {    // RR rotate
            relink(y, x.left, false);
            relink(x, y, true);
        }
        recomputeHeight(y);
        recomputeHeight(x);
        recomputeSize(y);
        recomputeSize(x);
    }

    protected Node<K,V> restructure(Node<K,V> x) {  // x -> y -> z 가 문제
        Node<K,V> y = x.parent;
        Node<K,V> z = y.parent;

        if ( (x == y.left) == (x == z.left) ) { rotate(y); return y;}   // LL or RR : y가 중간값
        else { rotate(x); rotate(x); return x;} // 중간값이 x : LR/RL -> LL/RR로 일단 변경
    }

    private void rebalance(Node<K,V> x) {
        Node<K,V> curr = x;
        while(curr != null) {
            recomputeHeight(curr);
            if (!isBalanced(curr)) {
                curr = restructure(tallerChild(tallerChild(curr)));
                recomputeHeight(curr.left);
                recomputeHeight(curr.right);
                recomputeHeight(curr);
            }
            curr = curr.parent;
        }
    }

    private Node<K,V> tallerChild(Node<K,V> x) {
        if (height(x.left) > height(x.right))  return x.left;
        if (height(x.left) < height(x.right)) return x.right;
        return (x == root || x == x.parent.left) ? x.left : x.right;
    }

    // --- [public API : Search, Insert, Delete] ---
    public V get(K key) {
        if (root == null) return null;
        Node<K,V> x = treeSearch(key);
        return (key.equals(x.key)) ? x.value : null;
    }
    public void put(K key, V val) {
        if (root == null) { root = new Node<K,V>(key, val); return;}
        Node<K,V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) x.value = val;
        else {
            Node<K,V> newNode = new Node<>(key, val);
            relink(x, newNode, cmp < 0);
            rebalance(newNode); // AVL 균형 유지
        }
    }

    public void delete(K key) {
        if (root == null) return;
        Node<K,V> x = treeSearch(key);
        if (!key.equals(x.key)) return;

        Node<K,V> p;
        if (x.left != null && x.right != null) {
            Node<K,V> y = min(x.right);
            x.key = y.key; x.value = y.value;
            p = y.parent;
            relink(p, y.right, y == p.left);
        } else {
            p = x.parent;
            Node<K,V> child = (x.left != null) ? x.left : x.right;
            if (p == null) {root = child; if (root != null) root.parent = null; }
            else relink(p, child, x==p.left);
        }
        rebalance(p);   // 삭제 후 조상 노드들 균형 재조정
    }

    // --- [Auxiliary BST Operations] ---
    protected Node<K,V> treeSearch(K key) {
        Node<K,V> x = root;
        while(true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            else if (cmp < 0) {
                if (x.left == null) return x;
                x = x.left;
            } else {
                if (x.right == null) return x;
                x = x.right;
            }
        }
    }

    private Node<K,V> min(Node<K,V> x) {
        while(x.left != null) x = x.left;
        return x;
    }

    public int size() { return (root != null) ? root.N : 0; }

    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<>(size());
        inorder(root, keyList);
        return keyList;
    }
    private void inorder(Node<K,V> x, ArrayList<K> list) {
        if (x == null) return;
        inorder(x.left, list);
        list.add(x.key);
        inorder(x.right, list);
    }
    // contains
    public boolean contains(K key) {
        return get(key) != null;
    }


}
