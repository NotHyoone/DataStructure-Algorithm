import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class FerquencyCounter {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("사용법: java FerquencyCounter <minlen> [filePath]");
            return;
        }

        int minlen = Integer.parseInt(args[0]);
        Scanner sc;
        BST<String, Integer> st = new BST<String, Integer>();
        File file;
        if (args.length >= 2) {
            file = new File(args[1]);
            if (!file.exists() || !file.isFile()) {
                System.out.println("유효한 파일 경로가 아닙니다: " + args[1]);
                return;
            }
        } else {
            final JFileChooser fc = new JFileChooser(); // 파일 선택기 사용
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(null, "파일을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            sc = new Scanner(file);
            long start = System.currentTimeMillis();
            while (sc.hasNext()) {
                String word = sc.next();
                if (word.length() < minlen) continue;
                if (!st.contains(word)) st.put(word, 1);
                else st.put(word, st.get(word) + 1);
            }
            String maxKey = "";
            int maxValue = 0;
            for (String word : st.keys()) {
                if (st.get(word) > maxValue) {
                    maxValue = st.get(word);
                    maxKey = word;
                }
            }
            long end = System.currentTimeMillis();
            System.out.println(maxKey + " " + maxValue);
            System.out.println("소요 시간 = " + (end - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
