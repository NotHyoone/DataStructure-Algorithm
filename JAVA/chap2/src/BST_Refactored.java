import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BST_Refactored<K extends Comparable<K>, V> {
    // Nested node type keeps visibility consistent with protected APIs.
    protected static class Node_BST<K, V> {
        K key;
        V value;
        Node_BST<K, V> left;
        Node_BST<K, V> right;
        Node_BST<K, V> parent;
        int size;

        Node_BST(K key, V value) {
            this.key = key;
            this.value = value;
            this.size = 1;
        }
    }

    protected Node_BST<K, V> root;

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        Node_BST<K, V> node = findNode(key);
        return (node == null) ? null : node.value;
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        if (root == null) {
            root = new Node_BST<>(key, value);
            return;
        }

        Node_BST<K, V> x = root;
        while (true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                x.value = value;
                return;
            }
            if (cmp < 0) {
                if (x.left == null) {
                    x.left = new Node_BST<>(key, value);
                    x.left.parent = x;
                    rebalanceInsert(x.left);
                    return;
                }
                x = x.left;
            } else {
                if (x.right == null) {
                    x.right = new Node_BST<>(key, value);
                    x.right.parent = x;
                    rebalanceInsert(x.right);
                    return;
                }
                x = x.right;
            }
        }
    }

    public void delete(K key) {
        if (root == null) {
            return;
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        Node_BST<K, V> target = treeSearch(root, key);
        if (target == null) {
            return;
        }

        if (target.left != null && target.right != null) {
            Node_BST<K, V> succ = minNode(target.right);
            target.key = succ.key;
            target.value = succ.value;
            // Successor has no left child.
            Node_BST<K, V> parent = succ.parent;
            relink(parent, succ.right, succ == parent.left);
            rebalanceDelete(parent);
            return;
        }

        Node_BST<K, V> child = (target.left != null) ? target.left : target.right;
        if (target == root) {
            root = child;
            if (root != null) {
                root.parent = null;
            }
            return;
        }

        Node_BST<K, V> parent = target.parent;
        relink(parent, child, target == parent.left);
        rebalanceDelete(parent);
    }

    public K min() {
        Node_BST<K, V> x = minNode(root);
        return (x == null) ? null : x.key;
    }

    public K max() {
        Node_BST<K, V> x = root;
        if (x == null) {
            return null;
        }
        while (x.right != null) {
            x = x.right;
        }
        return x.key;
    }

    public K floor(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        Node_BST<K, V> x = floor(root, key);
        return (x == null) ? null : x.key;
    }

    public int rank(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        Node_BST<K, V> x = root;
        int count = 0;

        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                count += 1 + size(x.left);
                x = x.right;
            } else {
                count += size(x.left);
                return count;
            }
        }
        return count;
    }

    public K select(int rank) {
        if (rank < 0 || rank >= size()) {
            return null;
        }

        Node_BST<K, V> x = root;
        int r = rank;

        while (x != null) {
            int leftSize = size(x.left);
            if (r < leftSize) {
                x = x.left;
            } else if (r > leftSize) {
                r -= leftSize + 1;
                x = x.right;
            } else {
                return x.key;
            }
        }
        return null;
    }

    public Iterable<K> keys() {
        if (root == null) {
            return Collections.emptyList();
        }
        List<K> list = new ArrayList<>(size());
        inorder(root, list);
        return list;
    }

    // Search helper exposed to subclasses only.
    protected Node_BST<K, V> treeSearch(Node_BST<K, V> start, K key) {
        Node_BST<K, V> x = start;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) {
                return x;
            }
            x = (cmp < 0) ? x.left : x.right;
        }
        return null;
    }

    protected void rebalanceInsert(Node_BST<K, V> inserted) {
        resetSize(inserted.parent, 1);
    }

    protected void rebalanceDelete(Node_BST<K, V> parent) {
        resetSize(parent, -1);
    }

    protected void relink(Node_BST<K, V> parent, Node_BST<K, V> child, boolean makeLeft) {
        if (child != null) {
            child.parent = parent;
        }
        if (makeLeft) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    protected Node_BST<K, V> minNode(Node_BST<K, V> x) {
        if (x == null) {
            return null;
        }
        Node_BST<K, V> cur = x;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur;
    }

    private Node_BST<K, V> findNode(K key) {
        return treeSearch(root, key);
    }

    private Node_BST<K, V> floor(Node_BST<K, V> x, K key) {
        if (x == null) {
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            return floor(x.left, key);
        }

        Node_BST<K, V> candidate = floor(x.right, key);
        return (candidate != null) ? candidate : x;
    }

    private void inorder(Node_BST<K, V> x, List<K> out) {
        if (x == null) {
            return;
        }
        inorder(x.left, out);
        out.add(x.key);
        inorder(x.right, out);
    }

    private int size(Node_BST<K, V> x) {
        return (x == null) ? 0 : x.size;
    }

    private void resetSize(Node_BST<K, V> x, int delta) {
        Node_BST<K, V> cur = x;
        while (cur != null) {
            cur.size += delta;
            cur = cur.parent;
        }
    }
}

