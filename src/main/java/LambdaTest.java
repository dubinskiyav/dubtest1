import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public class LambdaTest {

    public void test1() {
        MyRunner r = new MyRunner();
        new Thread(r).start();
        mysleep(1);
        String[] strings = new String[]{"Вася", "Федор", "Михаил", "Афанасий"};
        // Сортируем по умолчанию
        Arrays.sort(strings);
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }
        // Сортируем компанатором
        Arrays.sort(strings, new LengthStringComparator());
        for (int i = 0; i < strings.length; i++) {
            // Проверим
            System.out.println(strings[i]);
            // ничего не делаем
        }
        // Сортируем с помощью лямбда-выражения
        Arrays.sort(strings,
                (firstStr, secondStr) -> -1 * Integer.compare(firstStr.length(), secondStr.length())
        );
        for (String string : strings) {
            System.out.println(string);
        }
        AtomicReference<String> s1 = new AtomicReference<>("s1 ");
        System.out.println("Поток");
        Arrays.stream(strings)
                .forEach(p -> {
                    s1.set(s1 + " " + p);
                    System.out.println(s1);
                    System.out.println(p);
                });
        String s = s1.get();
        System.out.println("s1 Итог: " + s);
    }

    public static void mysleep(int sec) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class MyRunner implements Runnable {

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            LambdaTest.mysleep(1);
        }
    }
}

class LengthStringComparator implements Comparator<String> {

    public int compare(String firstStr, String secondStr) {
        // вызов Integer.compare (х, у) возвращает ноль, если х и у равны,
        // отрицательное число, если х < у, и положительное число, если х > у.
        return Integer.compare(firstStr.length(), secondStr.length());
    }
}
