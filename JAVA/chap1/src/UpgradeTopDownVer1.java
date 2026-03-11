public class UpgradeTopDownVer1 extends AbstractSort {
    // merge 성능 개선 1 and 2
    // 부분의 크기가 특정값보다 작을 경우, 삽입 정렬 사용
    // recursion(재귀)의 부담 완화
    // Collections.sort() 에서 cutoff=7로 설정
    private static final int CUTOFF = 7;

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

    public static void sort(Comparable[] a) {
        if (a == null || a.length <= 1) return;
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo + CUTOFF - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);

        // 이미 정렬된 상태면 merge 생략
        if (!less(a[mid + 1], a[mid])) return;

        merge(a, aux, lo, mid, hi);
    }

    public static void main(String[] args) {
        String[] A = {"A", "G", "L", "O", "R", "H", "I", "M", "S", "T"};
        UpgradeTopDownVer1.sort(A);
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
}
