import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    @Test
    public void testGetFactorials_Zero() {
        List<BigInteger> result = Main.getFactorials(0);
        assertEquals(List.of(BigInteger.ONE), result, "Факториалы для n=0 должны содержать только 1! = 1");
    }

    @Test
    public void testGetFactorials_One() {
        List<BigInteger> result = Main.getFactorials(1);
        assertEquals(List.of(BigInteger.ONE), result, "Факториалы для n=1 должны содержать только 1! = 1");
    }

    @Test
    public void testGetFactorials_Two() {
        List<BigInteger> result = Main.getFactorials(2);
        assertEquals(List.of(BigInteger.ONE, BigInteger.valueOf(2)), result, "Факториалы для n=2 должны быть [1!, 2!] = [1, 2]");
    }

    @Test
    public void testGetFactorials_Three() {
        List<BigInteger> result = Main.getFactorials(3);
        assertEquals(List.of(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(6)),
                result, "Факториалы для n=3 должны быть [1!, 2!, 3!] = [1, 2, 6]");
    }

    @Test
    public void testGetFactorials_Four() {
        List<BigInteger> result = Main.getFactorials(4);
        assertEquals(List.of(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(6), BigInteger.valueOf(24)),
                result, "Факториалы для n=4 должны быть [1!, 2!, 3!, 4!] = [1, 2, 6, 24]");
    }

    @Test
    public void testGetFactorials_Negative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Main.getFactorials(-1);
        });
        assertEquals("N cannot be negative", exception.getMessage());
    }
}