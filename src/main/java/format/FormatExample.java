package format;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Примеры работы с форматировнаием
 */
public class FormatExample {

    public static void decimalFormat() {
        System.out.println("FormatExample");
        Double d = 12345678.8910123;
        String s = new DecimalFormat("###,##0.00").format(d);
        System.out.println(d + "=" + s);
    }

    public static void doubleFormat() {
        String input = "1234567890.123456";
        double d = Double.parseDouble(input);

        System.out.println("d="+d);

        // 2 decimal points
        System.out.println(String.format("%,.2f", d));     // 1,234,567,890.12

        // 4 decimal points
        System.out.println(String.format("%,.4f", d));     // 1,234,567,890.1235

        // 20 digits, if enough digits, puts 0
        System.out.println(String.format("%,020.2f", d));  // 00001,234,567,890.12

        // 10 decimal points, if not enough digit, puts 0
        System.out.println(String.format("%,.010f", d));   // 1,234,567,890.1234560000

        // in scientist format
        System.out.println(String.format("%e", d));        // 1.234568e+09

        // different locale - FRANCE
        System.out.println(String.format(
                Locale.getDefault(), "%,.2f", d));               // 1 234 567 890,12

        // different locale - GERMAN
        System.out.println(String.format(
                Locale.getDefault(), "%,.2f", d));               // 1.234.567.890,12
    }

}
