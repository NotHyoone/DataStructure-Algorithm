import java.io.*;

public class DirectTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            System.out.println("\n=== Test " + (i+1) + " ===");
            Process p = Runtime.getRuntime().exec(new String[]{
                "cmd", "/c",
                "cd /d D:\\Github_Repo\\DataStructure-Algorithm\\JAVA\\coursera && " +
                "java -cp algs4.jar;src RandomWord"
            });

            String[] words = {"ant", "bear", "cat", "dog", "emu", "fox", "goat", "horse"};
            OutputStream os = p.getOutputStream();
            for (String word : words) {
                os.write((word + " ").getBytes());
            }
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Output: " + line);
            }
            p.waitFor();
        }
    }
}

