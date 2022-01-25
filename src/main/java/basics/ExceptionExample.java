package basics;

import java.util.Scanner;

/**
 * Примеры исключений
 */
public class ExceptionExample {

    public void test1() {
        System.out.println(this.getClass().getName());
        {
            int[] numbers = new int[3];
            try {
                numbers[3] = 45;
                System.out.println("Число " + numbers[2]);
            } catch (Exception e) {
                System.out.println("Ошибка:" + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("Блок finally");
            }
            System.out.println("Программа завершена");
        }
    }

    public void test2() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Введите целое число < 30");
            int x = in.nextInt();
            if (x >= 30) {
                throw new Exception("Число х должно быть меньше 30");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Программа завершена");
    }

    public void test3() throws Exception {
        int x = 33;
        if (x >= 30) {
            throw new Exception("Число х должно быть меньше 30");
        }
        System.out.println("Программа завершена");
    }
}
