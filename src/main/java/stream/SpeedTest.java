package stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Тест скорости работы цикла vs потока
 * Правтически одинаково
 */
public class SpeedTest {

    public SpeedTest() {
    }

    public void test() {
        List<Integer> list = new ArrayList<>();
        int limit = 100000000;
        System.out.println("Заполнение до ");
        long start = new Date().getTime();
        for (int i = 0; i < limit; i++) {
            list.add(i);
            if (i % (limit / 10) == 0) {
                System.out.println(i);
            }
        }
        long finish = new Date().getTime();
        System.out.println(list.size());
        System.out.println("Мили секунд " + (finish - start));
        start = new Date().getTime();
        for (Integer i : list) {
            if (i % (limit / 10) == 0) {
                System.out.println(i);
            }
        }
        finish = new Date().getTime();
        System.out.println("Цикл " + (finish - start));
        start = new Date().getTime();
        list.forEach(i -> {
            if (i % (limit / 10) == 0) {
                System.out.println(i);
            }
        });
        finish = new Date().getTime();
        System.out.println("Поток " + (finish - start));
    }

}
