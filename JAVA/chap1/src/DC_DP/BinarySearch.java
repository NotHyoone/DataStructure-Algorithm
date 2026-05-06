package DC_DP;

public class BinarySearch {
    public static int binarySearch(int[] S, int x) {
        return location(S, x, 0, S.length - 1);
    }
    private static int location(int[] S, int x, int low, int high) {
        if (low > high) return -1;

        int mid = (low + high) / 2;

        if (x == S[mid]) return mid;
        else if (x < S[mid]) return location(S, x, low, mid-1);
        else return location(S, x, mid+1, high);
    }

    public static void main(String[] args) {
        int[] A = {1, 3, 5, 7, 9, 11};
        int key = 5;
        int index = binarySearch(A, key);

        if (index != -1)
            System.out.println("키 " + key + "는 인덱스 " + index + "에 있습니다.");
        else
            System.out.println("키 " + key + "는 배열에 없습니다.");
    }
}
