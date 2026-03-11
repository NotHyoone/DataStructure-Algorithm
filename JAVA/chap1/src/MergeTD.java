public class MergeTD extends AbstractSort {
    // merge basic
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                a[k] = aux[i++];
            else if (j > hi)
                a[k] = aux[j++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
        }
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length-1);
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);      // 아랫부분 정렬
        sort(a, aux, mid+1, hi);    // 윗부분 정렬
        merge(a, aux, lo, mid, hi); // 두 부분을 병합
    }

    public static void main(String[] args) {
        String[] A = {"A", "G", "L", "O", "R", "H", "I", "M", "S", "T"};
        Merge.sort(A);
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
}
