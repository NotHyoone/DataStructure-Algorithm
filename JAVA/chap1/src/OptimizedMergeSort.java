public class OptimizedMergeSort extends AbstractSort {
    private static final int CUTOFF = 7;    // 재귀 깊이 7로 설정

    public static void sort(Comparable[] a) {
        if (a == null || a.length<=1) return;
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length-1);
    }
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo + CUTOFF -1) {
            Insertion.sort(a, lo, hi);
            return;
        }
        int mid = lo + (hi-lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid+1, hi);

        if (!less(a[mid], a[mid+1])) return;

        merge(a, aux, lo, mid, hi);
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];

        int i = lo, j = mid +1;
        for (int k=lo; k <= hi; k++) {
            if (i > mid)
                a[k] = a[j++];
            else if (j > hi)
                a[k] = a[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    public static void main(String[] args) {
        String[] A = {"A", "G", "L", "O", "R", "H", "I", "M", "S", "T"};
        OptimizedMergeSort.sort(A);
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
}
