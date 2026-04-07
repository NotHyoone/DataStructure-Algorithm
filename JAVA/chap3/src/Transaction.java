import java.security.Key;
import java.util.Date;

public class Transaction {
    private final String who;
    private final Date when;
    private final double amount;
    private static final int M = 997; // Size of the hash table

    public Transaction(String who, Date when, double amount) {
        this.who = who;
        this.when = when;
        this.amount = amount;
    }

    public String getCustomer() {
        return who;
    }

    public double getAmount() {
        return amount;
    }

    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + who.hashCode();
        hash = 31 * hash + when.hashCode();
        hash = 31 * hash + Double.hashCode(amount);
        return hash;
    }
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff ) % M;
    }
}
