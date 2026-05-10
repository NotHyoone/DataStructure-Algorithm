package DC_DP;

public class Folyd {
    private static void floyd(int[][] W, int[][] D, int[][] P) {
        int i, j, k;

        for (i = 0; i < W.length; i++)
            for (j = 0; j < W.length; j++) {
                D[i][j] = W[i][j];
                P[i][j] = -1;
            }

        for (k = 0; k < W.length; k++) {
            for (i = 0; i < W.length; i++) {
                for (j = 0; j < W.length; j++) {
                    if (D[i][k] != Integer.MAX_VALUE
                            && D[k][j] != Integer.MAX_VALUE
                            && D[i][k] + D[k][j] < D[i][j]) {
                        P[i][j] = k;
                        D[i][j] = D[i][k] + D[k][j];
                    }
                }
            }
        }
    }

    private static void path(int q, int r, int[][] P) {
        if (P[q][r] != -1) {
            path(q, P[q][r], P);
            System.out.print(" v" + (P[q][r] + 1));
            path(P[q][r], r, P);
        }
    }

    public static void main(String[] args) {
        int[][] W = {
                {0, 1, Integer.MAX_VALUE, 1, 5},
                {9, 0, 3, 2, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 4, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, 2, 0, 3},
                {3, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0}
        };
        int[][] D = new int[W.length][W.length];
        int[][] P = new int[W.length][W.length];

        // W 초기값 출력
        System.out.println("========== W 초기값 (가중치 행렬) ==========");
        printMatrix(W, "W");

        floyd(W, D, P);

        // 최단거리 행렬 D 출력
        System.out.println("\n========== D 최단거리 행렬 (계산 후) ==========");
        printMatrix(D, "D");

        // 경로 선행자 행렬 P 출력
        System.out.println("\n========== P 선행자 행렬 (경로 추적용) ==========");
        printMatrixP(P, "P");

        // 경로 출력 예제
        System.out.println("\n========== 경로 예제 ==========");
        System.out.print("path(v5, v3) =");
        path(4, 2, P);  // 배열 인덱스: v5->4, v3->2
        System.out.println();
    }

    private static void printMatrix(int[][] matrix, String name) {
        System.out.print("     ");
        for (int i = 1; i <= matrix.length; i++) {
            System.out.print("  v" + i);
        }
        System.out.println();
        System.out.println("   " + "─".repeat(matrix.length * 4 + 2));

        for (int i = 0; i < matrix.length; i++) {
            System.out.print("v" + (i + 1) + " │ ");
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE) {
                    System.out.print("  ∞  ");
                } else {
                    System.out.printf("%3d ", matrix[i][j]);
                }
            }
            System.out.println("│");
        }
        System.out.println("   " + "─".repeat(matrix.length * 4 + 2));
    }

    private static void printMatrixP(int[][] matrix, String name) {
        System.out.print("     ");
        for (int i = 1; i <= matrix.length; i++) {
            System.out.print("  v" + i);
        }
        System.out.println();
        System.out.println("   " + "─".repeat(matrix.length * 4 + 2));

        for (int i = 0; i < matrix.length; i++) {
            System.out.print("v" + (i + 1) + " │ ");
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == -1) {
                    System.out.print("  -  ");
                } else {
                    System.out.printf("  v%d ", (matrix[i][j] + 1));
                }
            }
            System.out.println("│");
        }
        System.out.println("   " + "─".repeat(matrix.length * 4 + 2));
    }
}
