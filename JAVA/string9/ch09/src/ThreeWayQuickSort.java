public class ThreeWayQuickSort {
    private static final int CUTOFF = 15; // 작은 배열에 대한 삽입 정렬 임계값

    public static void sort(String[] a) {sort(a, 0, a.length - 1, 0);}

    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo) return;

        if (hi <= lo + CUTOFF) {
            Insertion.sort(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi, v = CharAt.charAt(a[lo], d), i = lo + 1;
        while (i <= gt) {
            int t = CharAt.charAt(a[i], d);
            if (t < v) Insertion.exch(a, lt++, i++);
            else if (t > v ) Insertion.exch(a, i, gt--);
            else i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]
        sort(a, lo, lt - 1, d);
        if (v >= 0) sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }
}
