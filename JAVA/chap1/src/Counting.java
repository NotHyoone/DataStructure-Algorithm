public class Counting {
    public static int[] sort(int[] A, int K) {
        int i, N = A.length;
        int[] C = new int[K], B = new int[N];

        // 각 값의 등장 횟수를 셈
        for (i = 0; i < N; i++) C[A[i]]++;
        // 누적합으로 변환하여 각 원소의 출력 위치를 계산``
        for (i = 1; i < K; i++) C[i] += C[i-1];
        // 역순으로 A를 순회하며 B에 정렬된 결과를 저장 (안정 정렬)
        for (i = N-1; i >= 0; i--) B[--C[A[i]]] = A[i];
        return B;
    }

    public static void main(String[] args) {
        int[] A = {10, 4, 5, 8, 1, 8, 3, 6}, B;
        B = Counting.sort(A, 11);
        for (int i = 0; i < B.length; i++)
            System.out.print(B[i] + " ");
        System.out.println();
    }
}
