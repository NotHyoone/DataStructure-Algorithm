import java.io.*;

public class ExecutionTest {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("HelloWorld 프로그램 실행");
        System.out.println("========================================");
        Process p1 = Runtime.getRuntime().exec(new String[]{
            "cmd", "/c",
            "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && java -cp algs4.jar;src HelloWorld"
        });
        p1.getOutputStream().close();
        BufferedReader br1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        String line;
        while ((line = br1.readLine()) != null) {
            System.out.println(line);
        }
        p1.waitFor();

        System.out.println("\n========================================");
        System.out.println("HelloGoodbye 프로그램 실행 (입력: Alice Bob)");
        System.out.println("========================================");
        Process p2 = Runtime.getRuntime().exec(new String[]{
            "cmd", "/c",
            "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && java -cp algs4.jar;src HelloGoodbye"
        });
        OutputStream os2 = p2.getOutputStream();
        os2.write("Alice\nBob\n".getBytes());
        os2.close();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        while ((line = br2.readLine()) != null) {
            System.out.println(line);
        }
        p2.waitFor();

        System.out.println("\n========================================");
        System.out.println("RandomWord 프로그램 실행 (5번 반복)");
        System.out.println("입력: ant bear cat dog emu fox goat horse");
        System.out.println("========================================");
        for (int i = 1; i <= 5; i++) {
            Process p3 = Runtime.getRuntime().exec(new String[]{
                "cmd", "/c",
                "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && java -cp algs4.jar;src RandomWord"
            });

            String[] words = {"ant", "bear", "cat", "dog", "emu", "fox", "goat", "horse"};
            OutputStream os3 = p3.getOutputStream();
            for (String word : words) {
                os3.write((word + " ").getBytes());
            }
            os3.close();

            BufferedReader br3 = new BufferedReader(new InputStreamReader(p3.getInputStream()));
            System.out.print("실행 #" + i + " 결과: ");
            while ((line = br3.readLine()) != null) {
                System.out.println(line);
            }
            p3.waitFor();
        }
    }
}

