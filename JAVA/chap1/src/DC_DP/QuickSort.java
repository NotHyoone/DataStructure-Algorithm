package DC_DP;

public class QuickSort extends AbstractSort {
    public static void sort(Comparable[] a) {
        quicksort(a, 0, a.length-1);
    }

    private static void quicksort(Comparable[] a, int low, int high) {
        if (high > low) {
            int pivot = partition(a, low, high);
            quicksort(a, low, pivot-1);
            quicksort(a, pivot+1, high);
        }
    }
    // 교재 version
//    private static int partition(Comparable[] a, int low, int high) {
//        Comparable pivotitem = a[low];
//        int j = low;
//        for (int i = low+1; i<=high; i++) {
//            if (less(a[i], pivotitem)) {
//                j += 1;
//                exch(a, i, j);
//            }
//        }
//        int pivotpoint = j;
//        exch(a, low, pivotpoint);
//        return pivotpoint;
//    }

    // 개선안
    // Hoare partition 사용
    // pivotitem을 low로 고정하고, i는 low+1에서 시작, j는 high에서 시작하여 양쪽에서 탐색
    // i는 pivotitem보다 큰 요소를 찾을 때까지 오른쪽으로 이동, j는 pivotitem보다 작은 요소를 찾을 때까지 왼쪽으로 이동
    // i와 j가 교차하기 전까지 반복, 교차하면 pivotitem과 j 위치의 요소를 교환
    // Hoare partition은 평균적으로 더 효율적이며, 특히 중복된 요소가 많은 경우에 유리
    private static int partition(Comparable[] a, int low, int high) {
        Comparable pivot = a[low];
        int i = low + 1; int j = high;

        while (true) {
            while (i <= high && less(a[i], pivot)) i++;
            while (j >= low+1 && less(pivot, a[j])) j--;

            if (i >= j) break;

            exch(a, i, j);
            i++;
            j--;
        }
        exch(a,low, j);
        return j;
    }
}
