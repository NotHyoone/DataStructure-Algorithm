import java.io.*;

public class FinalTest {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("1. HelloWorld 실행 결과");
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
        System.out.println("2. HelloGoodbye 실행 결과 (Kevin Bob 입력)");
        System.out.println("========================================");
        Process p2 = Runtime.getRuntime().exec(new String[]{
            "cmd", "/c",
            "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && java -cp algs4.jar;src HelloGoodbye"
        });
        OutputStream os2 = p2.getOutputStream();
        os2.write("Kevin\nBob\n".getBytes());
        os2.close();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        while ((line = br2.readLine()) != null) {
            System.out.println(line);
        }
        p2.waitFor();

        System.out.println("\n========================================");
        System.out.println("3. RandomWord 실행 결과 (여러 번 실행)");
        System.out.println("========================================");
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n실행 #" + i + ": ant bear cat dog emu fox goat horse");
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
            while ((line = br3.readLine()) != null) {
                System.out.println("결과: " + line);
            }
            p3.waitFor();
        }

        System.out.println("\n========================================");
        System.out.println("4. RandomWord 실행 결과 (heads tails)");
        System.out.println("========================================");
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n실행 #" + i + ": heads tails");
            Process p4 = Runtime.getRuntime().exec(new String[]{
                "cmd", "/c",
                "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && java -cp algs4.jar;src RandomWord"
            });

            OutputStream os4 = p4.getOutputStream();
            os4.write("heads tails\n".getBytes());
            os4.close();

            BufferedReader br4 = new BufferedReader(new InputStreamReader(p4.getInputStream()));
            while ((line = br4.readLine()) != null) {
                System.out.println("결과: " + line);
            }
            p4.waitFor();
        }
    }
}

