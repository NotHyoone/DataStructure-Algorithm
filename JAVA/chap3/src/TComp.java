import java.util.Comparator;

public class TComp implements Comparator<String> {
    public int compare(String aStr, String bStr) {
        int i,j,k;

        i = aStr.lastIndexOf(' ');
        j = bStr.lastIndexOf(' ');

        k = aStr.substring(i).compareToIgnoreCase(bStr.substring(j));
        return (k == 0) ? aStr.compareToIgnoreCase(bStr) : k;
    }
}
