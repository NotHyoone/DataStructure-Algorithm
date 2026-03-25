import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) {
        BST<String, Integer> st = new BST<String, Integer>();
        File file;
        final JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) file = fc.getSelectedFile();
        else {
            JOptionPane.showMessageDialog(null, "파일을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            for (int i = 0; sc.hasNext(); i++) {
                String key = sc.next();
                st.put(key, i);
            }
            for (String key : st.keys()) {
                System.out.println(key + " " + st.get(key));
            }
        } catch(FileNotFoundException e) {e.printStackTrace();}
        if (sc != null) {sc.close();}
    }
}
