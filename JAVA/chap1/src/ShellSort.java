public class ShellSort extends AbstractSort {
    public static void sort(Comparable[] a) {
        int N = a.length;
        int h = 1;

        while (N/h > 0) h = 3*h + 1;

        while (h > 0) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j-h]); j-=h)
                    exch(a, j, j-h);
            }
            h/=3;
        }

    }

    public static void main(String[] args) {
        Integer[] a = {10, 4, 5, 2, 1, 8, 3, 6};
        ShellSort.sort(a);
        ShellSort.show(a);
    }
}
