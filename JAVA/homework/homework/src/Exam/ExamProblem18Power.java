package Exam;

public class ExamProblem18Power {
    public int power(int x, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("This integer version supports only n >= 0.");
        }
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return x;
        }

        int result;
        if (n % 2 == 0) {
            result = power(x, n / 2);
            return result * result;
        } else {
            result = power(x, (n - 1) / 2);
            return result * result * x;
        }
    }

    /*
     * 빈칸 답:
     * ㄱ: Power(x, n / 2)
     * ㄴ: result * result
     * ㄷ: Power(x, (n - 1) / 2)
     * ㄹ: result * result * x
     *
     * 시간 복잡도: T(n) = T(n / 2) + O(1) = O(log n)
     */
}
