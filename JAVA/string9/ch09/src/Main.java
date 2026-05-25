
public class Main {
    public static void main(String[] args) {
        // MSD는 문자열의 길이가 다르더라도 정렬 가능하지만, LSD는 모든 문자열의 길이가 같아야 함
        String[] a = {"banana", "apples", "grapes", "orange", "kiwi  "};
        String[] b = {"banana", "apples", "grapes", "orange", "kiwi"};
        String[] c = {"banana", "apples", "grapes", "orange", "kiwi", "app", "apple"};
        double s_time = System.nanoTime();
        LSD.sort(a, 6); // 모든 문자열의 길이가 6이라고 가정
        double e_time = System.nanoTime();
        System.out.println("LSD sort time: " + (e_time - s_time) + " nanoseconds");
        for (String s : a) System.out.println(s);
        System.out.println();

        s_time = System.nanoTime();
        MSD.sort(b, 0, b.length - 1, 0);
        e_time = System.nanoTime();
        System.out.println("MSD sort time: " + (e_time - s_time) + " nanoseconds");
        for (String s : b) System.out.println(s);
        System.out.println();

        s_time = System.nanoTime();
        ThreeWayQuickSort.sort(c);
        e_time = System.nanoTime();
        System.out.println("3-way quick sort time: " + (e_time - s_time) + " nanoseconds");
        for (String s : c) System.out.println(s);
    }
}
