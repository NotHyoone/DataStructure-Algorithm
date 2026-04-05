/**
 * 최적 이진 탐색 트리 (Optimal Binary Search Tree, OBST)
 *
 * - 동적 프로그래밍으로 기대 탐색 비용을 최소화하는 BST를 구성
 * - 시간 복잡도: O(n^3),  공간 복잡도: O(n^2)
 *
 * DP 테이블 정의 (0-indexed)
 *   keys[0..n-1] : 실제 키
 *   p[0..n-1]    : keys[i]가 탐색될 확률
 *   q[0..n]      : 더미 키(탐색 실패)의 확률
 *                  q[0]  = keys[0]보다 작은 값이 탐색될 확률
 *                  q[i]  = keys[i-1]~keys[i] 사이 값이 탐색될 확률
 *                  q[n]  = keys[n-1]보다 큰 값이 탐색될 확률
 *
 *   e[i][j] = keys[i..j-1]을 포함하는 최적 서브트리의 기대 탐색 비용
 *   w[i][j] = e[i][j]의 확률 가중치 합 = sum(p[i..j-1]) + sum(q[i..j])
 *   root[i][j] = r: keys[r-1]이 e[i][j] 최적 서브트리의 루트
 *
 * 점화식:
 *   e[i][i]   = q[i]
 *   w[i][j]   = w[i][j-1] + p[j-1] + q[j]
 *   e[i][j]   = min{ e[i][r-1] + e[r][j] + w[i][j] }  (r: i+1 ~ j)
 */
public class OBST {

    // ─────────────────────────────────────────────
    //  내부 노드 클래스
    // ─────────────────────────────────────────────
    static class Node {
        int    key;
        double prob;   // 탐색 확률
        Node   left, right;

        Node(int key, double prob) {
            this.key  = key;
            this.prob = prob;
        }
    }

    // ─────────────────────────────────────────────
    //  필드
    // ─────────────────────────────────────────────
    private final Node   root;
    private final int[]  keys;
    private final double optimalCost;

    // ─────────────────────────────────────────────
    //  생성자: DP 계산 + 트리 구성
    // ─────────────────────────────────────────────
    public OBST(int[] keys, double[] p, double[] q) {
        this.keys = keys;
        int n = keys.length;

        // DP 테이블
        double[][] e       = new double[n + 1][n + 1];
        double[][] w       = new double[n + 1][n + 1];
        int[][]    rootTbl = new int[n + 1][n + 1];

        // 길이 0 초기화 (더미 키만 있는 부분 트리)
        for (int i = 0; i <= n; i++) {
            e[i][i] = q[i];
            w[i][i] = q[i];
        }

        // 길이 l 부분 문제 해결
        for (int l = 1; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l;
                e[i][j] = Double.POSITIVE_INFINITY;
                w[i][j] = w[i][j - 1] + p[j - 1] + q[j];

                // 루트 후보 r: keys[r-1] 을 루트로 했을 때의 비용 계산
                for (int r = i + 1; r <= j; r++) {
                    double cost = e[i][r - 1] + e[r][j] + w[i][j];
                    if (cost < e[i][j]) {
                        e[i][j]       = cost;
                        rootTbl[i][j] = r;
                    }
                }
            }
        }

        this.optimalCost = e[0][n];
        this.root        = buildTree(rootTbl, p, 0, n);

        // 학습용 DP 테이블 출력
        printDPTables(e, w, rootTbl, n);
    }

    // ─────────────────────────────────────────────
    //  트리 구성 (rootTbl 재귀 추적)
    // ─────────────────────────────────────────────
    private Node buildTree(int[][] rootTbl, double[] p, int i, int j) {
        if (i >= j) return null;
        int  r    = rootTbl[i][j];                     // 루트 인덱스 (1-indexed)
        Node node = new Node(keys[r - 1], p[r - 1]);
        node.left  = buildTree(rootTbl, p, i, r - 1);  // 왼쪽 서브트리
        node.right = buildTree(rootTbl, p, r, j);       // 오른쪽 서브트리
        return node;
    }

    // ─────────────────────────────────────────────
    //  탐색
    // ─────────────────────────────────────────────
    /**
     * OBST에서 키를 탐색합니다.
     * @param key 탐색할 키
     * @return 탐색 성공 여부
     */
    public boolean search(int key) {
        return searchHelper(root, key, 1);
    }

    private boolean searchHelper(Node node, int key, int depth) {
        if (node == null) {
            System.out.printf("  %3d 탐색 → 실패  (깊이 %d)%n", key, depth);
            return false;
        }
        if (key == node.key) {
            System.out.printf("  %3d 탐색 → 성공  (깊이 %d, p=%.2f)%n", key, depth, node.prob);
            return true;
        }
        if (key < node.key) return searchHelper(node.left,  key, depth + 1);
        else                 return searchHelper(node.right, key, depth + 1);
    }

    // ─────────────────────────────────────────────
    //  순회
    // ─────────────────────────────────────────────
    /** 중위 순회 (키 오름차순) */
    public void inorder() {
        System.out.print("중위 순회: ");
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(Node node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.printf("%d(p=%.2f) ", node.key, node.prob);
        inorderHelper(node.right);
    }

    // ─────────────────────────────────────────────
    //  트리 시각화
    // ─────────────────────────────────────────────
    /** 트리 구조를 콘솔에 시각화 출력 */
    public void printTree() {
        System.out.println("트리 구조:");
        if (root == null) {
            System.out.println("  (비어있음)");
            return;
        }
        System.out.printf("  %d (p=%.2f)  ← 루트%n", root.key, root.prob);
        printBranch(root.left,  "  ", true);
        printBranch(root.right, "  ", false);
    }

    private void printBranch(Node node, String prefix, boolean isLeft) {
        if (node == null) return;
        String connector   = isLeft ? "├─[L]─ " : "└─[R]─ ";
        String childPrefix = isLeft ? "│      " : "       ";
        System.out.printf("%s%s%d (p=%.2f)%n", prefix, connector, node.key, node.prob);
        printBranch(node.left,  prefix + childPrefix, true);
        printBranch(node.right, prefix + childPrefix, false);
    }

    // ─────────────────────────────────────────────
    //  트리 높이
    // ─────────────────────────────────────────────
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    // ─────────────────────────────────────────────
    //  Getter
    // ─────────────────────────────────────────────
    public double getOptimalCost() { return optimalCost; }
    public Node   getRoot()        { return root; }

    // ─────────────────────────────────────────────
    //  DP 테이블 출력 (학습용)
    // ─────────────────────────────────────────────
    private void printDPTables(double[][] e, double[][] w, int[][] rootTbl, int n) {
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println("  [DP] 기대 비용  e[i][j]");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.printf("%10s", "");
        for (int j = 0; j <= n; j++) System.out.printf("  j=%-4d", j);
        System.out.println();
        for (int i = 0; i <= n; i++) {
            System.out.printf("  i=%-6d  ", i);
            for (int j = 0; j <= n; j++) {
                if (j < i) System.out.printf("  %-6s", "-");
                else        System.out.printf("  %-6.3f", e[i][j]);
            }
            System.out.println();
        }

        System.out.println("\n─────────────────────────────────────────────────────");
        System.out.println("  [DP] 최적 루트  root[i][j]  (값 r → keys[r-1]이 루트)");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.printf("%10s", "");
        for (int j = 0; j <= n; j++) System.out.printf("  j=%-4d", j);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("  i=%-6d  ", i);
            for (int j = 0; j <= n; j++) {
                if (j <= i) System.out.printf("  %-6s", "-");
                else        System.out.printf("  %-6d", rootTbl[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────
    //  main
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║       최적 이진 탐색 트리 (OBST) 구현         ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // ─── 예제 1: CLRS 15.5 교과서 예제 ───────────────
        System.out.println("▶ 예제 1: CLRS 15.5 교과서 예제");
        System.out.println("  키:  10   20   30   40   50");
        System.out.println("  p:  0.15 0.10 0.05 0.10 0.20  (합=0.60)");
        System.out.println("  q:  0.05 0.10 0.05 0.05 0.05 0.10  (합=0.40)");
        System.out.println("  → p + q 총합 = 1.00\n");

        int[]    keys1 = {10, 20, 30, 40, 50};
        double[] p1    = {0.15, 0.10, 0.05, 0.10, 0.20};
        double[] q1    = {0.05, 0.10, 0.05, 0.05, 0.05, 0.10};  // n+1 = 6개

        OBST obst1 = new OBST(keys1, p1, q1);
        System.out.printf("최적 기대 탐색 비용: %.4f  (CLRS 정답: 2.7500)%n%n",
                obst1.getOptimalCost());
        obst1.printTree();
        System.out.println();
        obst1.inorder();
        System.out.printf("트리 높이: %d%n%n", obst1.height());

        System.out.println("─── 탐색 테스트 ───");
        for (int key : new int[]{10, 20, 30, 40, 50, 25}) {
            obst1.search(key);
        }

        // ─── 예제 2: 균등 확률 (탐색 실패 없음) ──────────
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║         예제 2: 균등 확률 분포                ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
        System.out.println("  키:  1    2    3    4    5");
        System.out.println("  p:  0.20 0.20 0.20 0.20 0.20  (합=1.00)");
        System.out.println("  q:  0.00 0.00 0.00 0.00 0.00 0.00  (탐색 실패 없음)\n");

        int[]    keys2 = {1, 2, 3, 4, 5};
        double[] p2    = {0.20, 0.20, 0.20, 0.20, 0.20};
        double[] q2    = {0.00, 0.00, 0.00, 0.00, 0.00, 0.00};  // n+1 = 6개

        OBST obst2 = new OBST(keys2, p2, q2);
        System.out.printf("최적 기대 탐색 비용: %.4f%n%n", obst2.getOptimalCost());
        obst2.printTree();
        System.out.println();
        obst2.inorder();
        System.out.printf("트리 높이: %d%n", obst2.height());
    }
}
