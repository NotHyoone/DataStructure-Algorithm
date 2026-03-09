import java.io.*;

public class TestRandomWord {
    public static void main(String[] args) throws Exception {
        // Test 1: heads tails
        System.out.println("Test 1: heads tails");
        String input1 = "heads tails\n";
        Process p1 = Runtime.getRuntime().exec("cmd /c java -cp algs4.jar;src RandomWord");
        OutputStream os1 = p1.getOutputStream();
        os1.write(input1.getBytes());
        os1.close();
        BufferedReader br1 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        String line1;
        while ((line1 = br1.readLine()) != null) {
            System.out.println("Result: " + line1);
        }
        p1.waitFor();

        // Test 2: animals
        System.out.println("\nTest 2: ant bear cat dog emu fox goat horse");
        String input2 = "ant bear cat dog emu fox goat horse\n";
        Process p2 = Runtime.getRuntime().exec("cmd /c java -cp algs4.jar;src RandomWord");
        OutputStream os2 = p2.getOutputStream();
        os2.write(input2.getBytes());
        os2.close();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
        String line2;
        while ((line2 = br2.readLine()) != null) {
            System.out.println("Result: " + line2);
        }
        p2.waitFor();
    }
}

