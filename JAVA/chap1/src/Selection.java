public class Selection extends AbstractSort {
    public static void sort(Comparable[] a) {
        int N = a.length;
        System.out.println("Before sorting:");
        show(a);
        for (int i = 0; i < N-1; i++) {
            int min = i;
            for (int j = i+1; j < N; j++) {
                if (less(a[j], a[min]))
                    min = j;
            }
            exch(a, i, min);
        }
        System.out.println("After sorting:");
        show(a);
        System.out.println("Is sorted? " + isSorted(a));
        assert isSorted(a);
    };

    public static void main(String[] args) {
        Integer[] a = {10, 4, 5, 2, 1, 8, 3, 6};
        Selection.sort(a);
        Selection.show(a);
    }
}
