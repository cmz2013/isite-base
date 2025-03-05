package org.isite.commons.lang;

import org.isite.commons.lang.encoder.NumberEncoder;

import java.util.Scanner;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class NumberEncoderTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("enter a number: ");
            long number = scanner.nextLong();
            String code = NumberEncoder.encode(number);
            System.out.println("encode: " + code + " decode: " + NumberEncoder.decode(code));
        } while (true);
    }
}
