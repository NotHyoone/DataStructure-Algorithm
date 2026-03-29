public class FerquencyCounter {
    public static void main(String[] args) {
        BST<String, Integer> st = new BST<String, Integer>();
        String[] words = {"a", "b", "c", "a", "b", "a"};
        for (String word : words) {
            if (st.get(word) == null) st.put(word, 1);
            else st.put(word, st.get(word) + 1);
        }
        for (String key : st.keys()) {
            System.out.println(key + " " + st.get(key));
        }
    }
}
