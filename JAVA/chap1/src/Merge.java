public class Merge extends AbstractSort {
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // a[lo ... mid] and a[mid+1 ... hi] 는 이미 정렬됨

        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];      // aux[] 배열에 a[]의 내용을 일단 복사

        // aux[] 배열을 비교하여 병합된 결과를 a[] 배열에 다시 저장
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)                    a[k] = aux[j++];
            else if (j > hi)                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))  a[k] = aux[j++];
            else                            a[k] = aux[i++];
        }
    };

    public static void main(String[] args) {
        String[] A = {"A", "G", "L", "O", "R", "H", "I", "M", "S", "T"};
        Merge.sort(A);
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
}
