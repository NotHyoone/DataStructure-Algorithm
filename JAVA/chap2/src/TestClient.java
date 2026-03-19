import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestClient extends SequentialSearchST {
    public static void main(String[] args) {
        SequentialSearchST <String, Integer> st = new SequentialSearchST<String, Integer>();
        File file;
        final JFileChooser fc = new JFileChooser(); // 파일 선택기를 사용
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { file = fc.getSelectedFile(); }
        else {
            JOptionPane.showMessageDialog(null, "파일을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Scanner sc = null; // file에 있는 단어들을 키로 해서 테이블에 차례대로 저장
        try {               // 이후, 테이블에 저장된 모든 (키, 값)의 쌍들을 출력
            sc = new Scanner(file);
            for (int i = 0; sc.hasNext(); i++) { String key = sc.next(); st.put(key, i); }
            for (String s : st.keys()) System.out.println(s + "" + st.get(s));
        } catch (FileNotFoundException e) { e.printStackTrace();}
        if (sc != null) sc.close();
    }
}
