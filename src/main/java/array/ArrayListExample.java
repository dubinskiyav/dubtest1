package array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Примеры работы с коллекциями
 */
public class ArrayListExample {

    public void test1() {
        // Создадим переменную типа список
        List<String> streets = new ArrayList<>();
        // Добавим элементы
        streets.add("Карла Маркса");
        streets.add("Фридриха Энгельса");
        streets.add("Ленина");
        streets.add("Крупской");
        streets.add("Клары Цеткин");
        streets.add("Долоресс Ибаррури");
        // Выведем размер
        System.out.println(streets.size());
        // Выведем в цикле все элементы списка
        for (int i = 0; i <= streets.size() - 1; i++) {
            System.out.println(streets.get(i));
        }
        // Добавим вторым элементом
        streets.add(1, "Кирова");
        // Выведем размер
        System.out.println(streets.size());
        // Выведем в цикле все элементы списка
        for (int i = 0; i <= streets.size() - 1; i++) {
            System.out.println(streets.get(i));
        }
        // Добавим в конец списка
        streets.add(streets.size(), "Бориса Ельцина");
        // Выведем размер
        System.out.println(streets.size());
        // Выведем в цикле все элементы списка
        for (int i = 0; i <= streets.size() - 1; i++) {
            System.out.println(streets.get(i));
        }
        //
        Book b1 = new Book("Конституция", "Народ");
        b1.printPrint();
        // Создаем экземпляр типа интерфейс но типа array.Book
        Testable testable = new Book("Капитал", "К. Маркс");
        testable.printPrint();
        testable = new Journal("PlayBoy");
        testable.printPrint();
        // Интерфейс не имеет метода getName, необходимо явное приведение
        String name = ((Journal) testable).getName();
        System.out.println(name);
    }

    public void test2() {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        System.out.println(this.getClass().getName() + "." + methodName + " начали");
        List<String> streets = new ArrayList<>();
        // Добавим элементы
        streets.add("Карла Маркса");
        streets.add("Фридриха Энгельса");
        streets.add("Ленина");
        streets.add("Крупской");
        streets.add("Клары Цеткин");
        streets.add("Долоресс Ибаррури");
        if (false) { // так не вышло - валится
            // Поток
            streets.stream() // получаем поток
                    .forEach(p -> {
                        System.out.println(p);
                        if (p.equals("Ленина")) {
                            streets.add(p + " 2");
                            System.out.println(p + " 1");
                        }
                    });
        }
        // Выведем в цикле все элементы списка
        for (int i = 0; i <= streets.size() - 1; i++) {
            System.out.println(streets.get(i));
            if (streets.get(i).equals("Ленина")) {
                System.out.println(streets.get(i) + " 1");
                streets.add("Ленина 2");
            }
        }
        {
            Integer a1 = 2;
            Integer b1 = a1;
            System.out.println(a1);
            System.out.println(b1);
            a1 = 3;
            System.out.println(a1);
            System.out.println(b1);
        }
        {
            Integer a1 = 50;
            Integer a2 = 50;
            Integer b1 = 500;
            Integer b2 = 500;
            System.out.println(a1 == a2);
            System.out.println(b1 == b2);
            System.out.println(b1.equals(b2));
        }
        Integer i = null;
        if (i != null && i == 13456) {
            System.out.println("13456");
        } else {
            System.out.println("не 13456");
        }
        if (i.equals(13456)) {
            System.out.println("13456");
        }

    }

    public void test3() {
        List<Date> dates = new ArrayList<>();
        Date d = new Date();
        System.out.println(d.toString());
        dates.add(d);
        System.out.println(dates.get(0).toString());
        d.setTime(d.getTime() + 1000 * 60 * 60);
        System.out.println(d.toString());
        System.out.println(dates.get(0).toString());
        dates.add(new Date(d.getTime()));
        d.setTime(d.getTime() + 1000 * 60 * 60);
        System.out.println(d.toString());
        System.out.println(dates.get(1).toString());
    }

    class Appealactiontodel {

        Integer appealaction_id;
        Integer level;

        Appealactiontodel(
                Integer appealaction_id,
                Integer level
        ) {
            this.appealaction_id = appealaction_id;
            this.level = level;
        }
    }

    public void test4() {
        List<Appealactiontodel> appealactiontodel = new ArrayList<>();
        appealactiontodel.add(new Appealactiontodel(100, 10));
        appealactiontodel.add(new Appealactiontodel(200, 20));
        appealactiontodel.add(new Appealactiontodel(300, 30));
        appealactiontodel.add(new Appealactiontodel(400, 40));
        appealactiontodel.forEach(a -> System.out.println(a.appealaction_id + " " + a.level));
        for (Appealactiontodel value : appealactiontodel) {
            value.level = value.level + 2;
        }
        appealactiontodel.forEach(a -> System.out.println(a.appealaction_id + " " + a.level));

    }

    public void test5() {
        // цикл
        for (String element : Arrays.asList("a", "b", "c", "d", "e")) {
            System.out.println(element);
        }
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        list.forEach(element -> {
            System.out.println(element);
            System.out.println(element);
        });
        list.stream().forEach(element -> {
            System.out.println(element);
            System.out.println(element);
        });

    }

    // List в List c фильром
    public void listToList() {
        // цикл
        for (String element : Arrays.asList("a", "b", "c", "d", "e")) {
            System.out.println(element);
        }
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        List<String> filteredList = list.stream()
                .filter(s -> s.contains("b"))
                .collect(Collectors.toList());
    }

}


// Интерфейс
interface Testable {

    // пустой метод интерфейса
    // который должен быть реализован в классе, реализующим этот интерфейс
    void printPrint();
}

class Book implements Testable {

    String name;
    String author;

    Book(String name, String author) {

        this.name = name;
        this.author = author;
    }

    // Реализация метода, объявленного в интерфейсе Printable
    // который реализует класс array.Book
    public void printPrint() {

        System.out.printf("%s (%s) \n", name, author);
    }
}

class Journal implements Testable {

    private String name;

    String getName() {
        return name;
    }

    Journal(String name) {

        this.name = name;
    }

    public void printPrint() {
        System.out.println(name);
    }
}

class Account {

    private int id;
    private int sum;

    Account(int id, int sum) {
        this.id = id;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}