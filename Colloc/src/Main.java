import java.math.BigInteger;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) throws IllegalArgumentException{
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("N: ");
            int n = in.nextInt();


            List<BigInteger> factorials = getFactorials(n);

            for (int i = 0; i < n; i++) {
                System.out.println(i + 1 + "!= " + factorials.get(i));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: " + e.getMessage() );
        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            in.close();
        }
    }

    public static List<BigInteger> getFactorials(int n) throws IllegalArgumentException{
        List<BigInteger> factorials = new ArrayList<>();
        BigInteger factorial = BigInteger.ONE;

        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }

        factorials.add(factorial);

        for (int i = 2; i <= n; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
            factorials.add(factorial);
        }

        return factorials;
    }
}