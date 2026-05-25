public class CharAt {
    public static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1; // 문자열의 끝을 나타내는 특수한 값
    }
}
