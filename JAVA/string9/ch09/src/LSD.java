public class LSD {
    public static void sort(String[] a, int W) {    // 모든 문자의 길이가 W
        int N = a.length;
        int R = 256;    // 기수 = R
        String[] aux = new String[N];

        for (int d = W - 1; d >= 0; d--) {  // d: 자리수, LSD -> 뒤에서부터
            int[] count = new int[R];
            for (int i = 0; i < N; i++)
                count[a[i].charAt(d)]++;    // d 자리 문자들의 빈도수 계산
            for (int r = 1; r < R; r++)
                count[r] += count[r - 1];
            for (int i = N - 1; i >= 0; i--)    // 뒤 문자열부터 뒤에서 저장: stable
                aux[--count[a[i].charAt(d)]] = a[i];
            for (int i = 0; i < N; i++)
                a[i] = aux[i];
        }
    }
}
