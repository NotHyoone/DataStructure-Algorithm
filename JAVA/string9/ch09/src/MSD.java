public class MSD {
    public static void sort(String[] a, int lo, int hi, int d) {
        int R = 256;    // 기수 = R
        String[] aux = new String[hi - lo + 1];
        int M = 15; // 작은 배열에 대한 삽입 정렬 임계값
        if (hi <= lo + M) { Insertion.sort(a, lo, hi, d); return; }

        int[] count = new int[R + 2]; // R개의 배열 생성, count[r] = d 1자리 문자가 r인 문자열의 개수
        for (int i = lo; i <= hi; i++)
            count[CharAt.charAt(a[i], d) + 2]++;    // d 자리 문자들의 빈도수 계산
        for (int r = 0; r < R + 1; r++)
            count[r + 1] += count[r];

        for (int i = lo; i <= hi; i++)
            aux[count[CharAt.charAt(a[i], d) + 1]++] = a[i];

        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];

        for (int r = 0 ;r < R; r++)
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1);   // 재귀적으로 각 키에 대해 문자열 배열을 정렬
    }
}
