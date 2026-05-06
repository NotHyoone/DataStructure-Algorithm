package DC_DP;

public class MatrixMult {
    private static final int DEFAULT_THRESHOLD = 64;
    private final int threshold;

    public MatrixMult() {
        this(DEFAULT_THRESHOLD);
    }

    public MatrixMult(int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("threshold must be > 0");
        }
        this.threshold = threshold;
    }

    /*
     * [의사코드] matrixmult(n, A, B, C)
     * for i = 0..n-1
     *   for j = 0..n-1
     *     C[i][j] = 0
     *     for k = 0..n-1
     *       C[i][j] += A[i][k] * B[k][j]
     */
    public void matrixmult(int n, int[][] A, int[][] B, int[][] C) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
    }

    /*
     * [의사코드] strassen(n, A, B, C)
     * if n <= 임계점:
     *   단순한 알고리즘으로 C = A * B
     * else:
     *   A를 A11,A12,A21,A22로 분할
     *   B를 B11,B12,B21,B22로 분할
     *   M1..M7 계산(스트라센 공식)
     *   C11,C12,C21,C22 조합
     *   C로 병합
     */
    public void strassen(int n, int[][] A, int[][] B, int[][] C) {
        int[][] result = strassenRecursive(n, A, B);
        for (int i = 0; i < n; i++) {
            System.arraycopy(result[i], 0, C[i], 0, n);
        }
    }

    private int[][] strassenRecursive(int n, int[][] A, int[][] B) {
        if (n <= threshold || (n % 2 != 0)) {
            int[][] C = new int[n][n];
            matrixmult(n, A, B, C);
            return C;
        }

        int m = n / 2;

        int[][] A11 = copySubMatrix(A, 0, 0, m);
        int[][] A12 = copySubMatrix(A, 0, m, m);
        int[][] A21 = copySubMatrix(A, m, 0, m);
        int[][] A22 = copySubMatrix(A, m, m, m);

        int[][] B11 = copySubMatrix(B, 0, 0, m);
        int[][] B12 = copySubMatrix(B, 0, m, m);
        int[][] B21 = copySubMatrix(B, m, 0, m);
        int[][] B22 = copySubMatrix(B, m, m, m);

        int[][] M1 = strassenRecursive(m, add(A11, A22), add(B11, B22));
        int[][] M2 = strassenRecursive(m, add(A21, A22), B11);
        int[][] M3 = strassenRecursive(m, A11, subtract(B12, B22));
        int[][] M4 = strassenRecursive(m, A22, subtract(B21, B11));
        int[][] M5 = strassenRecursive(m, add(A11, A12), B22);
        int[][] M6 = strassenRecursive(m, subtract(A21, A11), add(B11, B12));
        int[][] M7 = strassenRecursive(m, subtract(A12, A22), add(B21, B22));

        int[][] C11 = add(subtract(add(M1, M4), M5), M7);
        int[][] C12 = add(M3, M5);
        int[][] C21 = add(M2, M4);
        int[][] C22 = add(subtract(add(M1, M3), M2), M6);

        int[][] C = new int[n][n];
        writeSubMatrix(C, C11, 0, 0);
        writeSubMatrix(C, C12, 0, m);
        writeSubMatrix(C, C21, m, 0);
        writeSubMatrix(C, C22, m, m);

        return C;
    }

    private int[][] add(int[][] X, int[][] Y) {
        int n = X.length;
        int[][] Z = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Z[i][j] = X[i][j] + Y[i][j];
            }
        }
        return Z;
    }

    private int[][] subtract(int[][] X, int[][] Y) {
        int n = X.length;
        int[][] Z = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Z[i][j] = X[i][j] - Y[i][j];
            }
        }
        return Z;
    }

    private int[][] copySubMatrix(int[][] src, int row, int col, int size) {
        int[][] dst = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(src[row + i], col, dst[i], 0, size);
        }
        return dst;
    }

    private void writeSubMatrix(int[][] dst, int[][] src, int row, int col) {
        int size = src.length;
        for (int i = 0; i < size; i++) {
            System.arraycopy(src[i], 0, dst[row + i], col, size);
        }
    }

    public static void main(String[] args) {
        int n = 4; // 4x4 행렬
        int[][] A = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        int[][] B = {
            {16, 15, 14, 13},
            {12, 11, 10, 9},
            {8, 7, 6, 5},
            {4, 3, 2, 1}
        };
        int[][] C = new int[n][n];

        MatrixMult mm = new MatrixMult();
        mm.strassen(n, A, B, C);

        System.out.println("Result of A * B:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }
}
