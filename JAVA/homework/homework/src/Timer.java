/**
 * 최소한의 복잡도 측정기 (Time & Space Complexity)
 * 다른 클래스에서 쉽게 적용 가능한 간단한 성능 측정 도구
 *
 * 사용법:
 * Timer timer = new Timer();
 * timer.start();
 * // 코드 실행
 * timer.end();
 * timer.print();
 */
public class Timer {
    private long startTime;
    private long endTime;
    private long startMemory;
    private long endMemory;

    // 시작
    public void start() {
        startTime = System.nanoTime();
        startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    // 종료
    public void end() {
        endTime = System.nanoTime();
        endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    // 시간 (나노초)
    public long timeNano() {
        return endTime - startTime;
    }

    // 시간 (마이크로초)
    public long timeMicro() {
        return timeNano() / 1_000;
    }

    // 시간 (밀리초)
    public long timeMillis() {
        return timeNano() / 1_000_000;
    }

    // 메모리 (바이트)
    public long memoryByte() {
        return endMemory - startMemory;
    }

    // 메모리 (킬로바이트)
    public long memoryKB() {
        return memoryByte() / 1024;
    }

    // 출력 - 간단
    public void print() {
        System.out.printf("⏱ 시간: %d ms | 💾 메모리: %d KB%n", timeMillis(), memoryKB());
    }

    // 출력 - 이름 포함
    public void print(String name) {
        System.out.printf("%s | 시간: %d ms | 메모리: %d KB%n", name, timeMillis(), memoryKB());
    }

    // 출력 - 상세
    public void printDetail() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.printf("│ ⏱  시간: %15d ns (%.2f ms)      │%n", timeNano(), (double)timeMillis());
        System.out.printf("│ 💾 메모리: %13d bytes (%.2f KB)  │%n", memoryByte(), (double)memoryKB());
        System.out.println("└─────────────────────────────────────────┘");
    }
}