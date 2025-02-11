package prison;

import org.docx4j.wml.PPrBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Задача о 100 заключенных
 * <a href="https://www.youtube.com/watch?v=wWQ9YdreY9c">...</a>
 */
public class Prison {

    public boolean test() {
        // сто заключенных
        int[] prisoners = new int[100];
        // пронумерованных от 1 до 100
        for (int i=0; i < prisoners.length; i++) {
            prisoners[i] = i + 1;
        }
        // сто коробок
        int[] boxes = new int[100];
        // пронумерованных от одного до 100
        for (int i=0; i < boxes.length; i++) {
            boxes[i] = i + 1;
        }
        // Коробки с листочками
        // ключ - номер коробки
        HashMap<Integer, Integer> boxesWithList = new HashMap<>();
        // Листочки с номерами
        Integer[] lists = new Integer[100];
        for (int i = 0; i < 100; i++) {
            lists[i] = i + 1;
        }
        // Надо расположить листочки в коробках случайным образом
        for (int boxNumber = 1; boxNumber <= 100; boxNumber++) {
            int left = lists.length; // сколько листочков еще не помещены в коробки
            // random генерит псевдослучайное число от 0 до 1 (без единицы)
            // а нам надо от 1 до left
            // значит так
            int i = (int) (Math.random() * left); // случайное число от 0 до left-1
            Random random = new Random();
            i = random.nextInt(left); // вернет случайное число от 0 до 99
            // поместим i-й номер из списка lists в boxNumber-ю коробку
            boxesWithList.put(boxNumber, lists[i]);
            // и удалим его
            List<Integer> list = new ArrayList<>(Arrays.asList(lists));
            list.remove(lists[i]);
            lists = list.toArray(new Integer[0]);
            //System.out.println(Arrays.toString(lists));
        }
        //System.out.println(boxesWithList);
        // Проверим, все ли номера есть
        for (int i = 1; i <=100; i++) {
            // проверим значение
            int finalI = i;
            if (boxesWithList.values().stream().filter(b -> b.equals(finalI)).findAny().isEmpty()) {
                throw new RuntimeException("Не нашли листочек с номером " + i);
            }
        }
        // далее каждый заключенный ищет свой листочек
        for (int prisonNumber: prisoners) {
            //System.out.println(prisonNumber);
            // Стратегия такая
            // сначала заключенный ищет коробку со своим номером, то есть с номером prisonNumber
            int nextBoxNumber = prisonNumber;
            int listNumber = -123; // номер найденного листочка
            for (int i = 1; i <= 50; i++) { // всего заключенному дается 50 попыток
                // открываем коробку и смотрим номер
                listNumber = boxesWithList.get(nextBoxNumber);
                if (listNumber == prisonNumber) { // нашел
                    break;
                }
                // далее ищем в коробке написанном на найденном номере
                nextBoxNumber = listNumber;
            }
            if (listNumber != prisonNumber) { // не нашел - всем смерть
                return false;
            }
        }
        // нет не нашедших - все выжили
        return true;
    }

}
