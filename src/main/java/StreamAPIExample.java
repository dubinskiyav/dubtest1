import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamAPIExample {

    public void test1() {
        int[] numbers = {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
        int count = 0;
        for (int i : numbers) {
            if (i > 0) { count++; }
        }
        System.out.println(count);
        long countL = IntStream.of(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5).filter(w -> w > 0).count();
        System.out.println(countL);
    }

    public void test2() {
        List<String> cities = new ArrayList<String>();
        Collections.addAll(cities, "Париж", "Лондон", "Мадрид");
        cities.stream() // получаем поток
                .filter(city -> city.length() == 6) // применяем фильтрацию по длине строки
                .forEach(p -> System.out.println(p)); // выводим отфильтрованные строки на консоль
        // разделим на шаги
        Stream<String> citiesStream = cities.stream(); // получаем поток в переменную
        citiesStream = citiesStream
                .filter(s -> s.length() == 6); // применяем фильтрацию по длине строки
        citiesStream
                .forEach(s -> System.out.println(s)); // выводим отфильтрованные строки на консоль
        // Создание потока данных из массива с помощью Arrays.stream(T[] array)
        citiesStream = Arrays.stream(new String[]{"Париж", "Лондон", "Мадрид"});
        citiesStream.forEach(s -> System.out.println(s)); // выводим все элементы массива
        // или так
        Arrays.stream(new String[]{"Париж2", "Лондон2", "Мадрид2"})
                .forEach(s -> System.out.println(s));
        // или так
        Stream<String> citiesStream1 = Stream.of("Париж1", "Лондон1", "Мадрид1");
        citiesStream1.forEach(s -> System.out.println(s));
        // сокращенная запись
        citiesStream = Stream.of("Париж", "Лондон", "Мадрид", "Берлин", "Брюссель");
        citiesStream.forEach(System.out::println);
        // int
        IntStream intStream = IntStream.of(1, 2, 4, 5, 7);
        intStream.forEach(i -> System.out.println(i));
        LongStream longStream = LongStream.of(100, 250, 400, 5843787, 237);
        longStream.forEach(l -> System.out.println(l));
        DoubleStream doubleStream = DoubleStream.of(3.4, 6.7, 9.5, 8.2345, 121);
        doubleStream.forEach(d -> System.out.println(d));
    }

    public void test3() {
        // фильрация
        Stream.of("Париж", "Лондон", "Мадрид", "Берлин", "Брюссель")
                .filter(s -> s.length() == 6)
                .forEach(s -> System.out.println(s));
        Stream.of(new Phone("iPhone 6 S", 54000),
                new Phone("Lumia 950", 45000),
                new Phone("Samsung Galaxy S 6", 40000))
                .filter(p -> p.getPrice() < 50000)
                .forEach(p -> System.out.println(p.getName()));
        // Поиск элемента
        Integer price = Stream.of(new Phone("iPhone 6 S", 54000),
                new Phone("Lumia 950", 45000),
                new Phone("Samsung Galaxy S 6", 40000))
                .filter(p -> p.getPrice() == 40000)
                .findAny()
                .orElse(new Phone(null,0)).getPrice();

        // преобразуем тип
        Stream.of(new Phone("iPhone 6 S", 54000),
                new Phone("Lumia 950", 45000),
                new Phone("Samsung Galaxy S 6", 40000))
                .map(p -> p.getName()) // помещаем в поток только названия телефонов
                .forEach(s -> System.out.println(s));
        Stream.of(new Phone("iPhone 6 S", 54000),
                new Phone("Lumia 950", 45000),
                new Phone("Samsung Galaxy S 6", 40000))
                .mapToInt(p -> p.getPrice()) // помещаем в поток только цену телефонов
                .forEach(s -> System.out.println(s));
        // из одного элемента нужно получить несколько
        Stream<Phone> phoneStream = Stream.of(new Phone("iPhone 6 S", 54000),
                new Phone("Lumia 950", 45000),
                new Phone("Samsung Galaxy S 6", 40000));
        phoneStream
                .flatMap(p -> Stream.of(
                        String.format("название: %s  цена без скидки: %d", p.getName(),
                                p.getPrice()),
                        String.format("название: %s  цена со скидкой: %d", p.getName(),
                                p.getPrice() - (int) (p.getPrice() * 0.1))
                ))
                .forEach(s -> System.out.println(s));
        // Найти companytype_oc которых нет в companytype_dc
        ArrayList<Integer> companytype_oc = new ArrayList<>(Arrays.asList(1,2,3,5,6,8,9));
        ArrayList<Integer> companytype_dc = new ArrayList<>(Arrays.asList(1,7));
        int i =companytype_oc.stream()
                .filter(oc -> !companytype_dc.contains(oc))
                .findAny().orElse(0);
        System.out.println(i);
    }

    public void test4() {
        List<String> phones = new ArrayList<String>();
        Collections.addAll(phones,
                "iPhone X",
                "Nokia 9",
                "Huawei Nexus 6P",
                "Samsung Galaxy S8",
                "LG G6",
                "Xiaomi MI6",
                "ASUS Zenfone 3",
                "Sony Xperia Z5",
                "Meizu Pro 6",
                "Pixel 2");
        phones.stream()
                .filter(p -> p.length() < 12)
                .sorted() // сортировка по возрастанию
                .forEach(s -> System.out.println(s));
        // Поток телефонов
        Stream<Phone> phoneStream = Stream.of(new Phone("iPhone X", "Apple", 600),
                new Phone("Pixel 2", "Google", 500),
                new Phone("iPhone 8", "Apple", 450),
                new Phone("Nokia 9", "HMD Global", 150),
                new Phone("Galaxy S9", "Samsung", 300));
        phoneStream.sorted(new PhoneComparator())
                .forEach(p -> System.out.printf("%s (%s) - %d \n",
                        p.getName(), p.getCompany(), p.getPrice()));
    }

    public void test5() {
        // Операции сведения
        // Массив
        ArrayList<String> names = new ArrayList<String>();
        // заполняем массив
        names.addAll(Arrays.asList(new String[]{"Tom", "Sam", "Bob", "Alice"}));
        // Формируем поток из массива и получаем количество
        System.out.println(names.stream().count());  // 4
        // количество элементов с длиной не больше 3 символов
        System.out.println(names.stream().filter(n -> n.length() <= 3).count());
        // min и max
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.addAll(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
        // Optional<T> min(Comparator<? super T> comparator)
        Optional<Integer> min = numbers.stream().min(Integer::compare);
        Optional<Integer> max = numbers.stream().max(Integer::compare);
        System.out.println(min.get());  // 1
        System.out.println(max.get());  // 9
        // полум минимальный и макс телефоны
        ArrayList<Phone> phones = new ArrayList<Phone>(Arrays.asList(
                new Phone("iPhone 8", 52000),
                new Phone("Nokia 9", 35000),
                new Phone("Samsung Galaxy S9", 48000),
                new Phone("HTC U12", 36000)));
        Phone minP = phones.stream().min(Phone::compare).get();
        Phone maxP = phones.stream().max(Phone::compare).get();
        System.out.printf("Дешевейший: %s Цена: %d \n", minP.getName(), minP.getPrice());
        System.out.printf("Дорогейший: %s Цена: %d \n", maxP.getName(), maxP.getPrice());
        // Здесь, для сравнения используется статический метод сравнения телефонов compare
    }

    public void test6() {
        // список numbers пустой
        ArrayList<Integer> numbers = new ArrayList<>();
        Optional<Integer> min = numbers.stream().min(Integer::compare);
        if (min.isPresent()) {
            System.out.println(min.get());  // java.util.NoSuchElementException
        } else {
            System.out.println("список пуст");
        }
        System.out.println(min.orElse(-1));
    }

    public void test7() {
        // создаем и запоняем коллекицю
        List<String> phones = new ArrayList<String>();
        Collections.addAll(phones, "iPhone 8", "HTC U12", "Huawei Nexus 6P",
                "Samsung Galaxy S9", "LG G6", "Xiaomi MI6", "ASUS Zenfone 2",
                "Sony Xperia Z5", "Meizu Pro 6", "Lenovo S850");
        // коллекция из потока созданного из коллекции и отфильтрованного
        List<String> filteredPhones = phones.stream()
                .filter(s -> s.length() < 10)
                .collect(Collectors.toList());
        for (String s : filteredPhones) {
            System.out.println(s);
        }
        // коллекия типа HashSet
        Stream<String> phonesH = Stream.of("iPhone 8", "HTC U12", "Huawei Nexus 6P",
                "Samsung Galaxy S9", "LG G6", "Xiaomi MI6", "ASUS Zenfone 2",
                "Sony Xperia Z5", "Meizu Pro 6", "Lenovo S850");
        HashSet<String> filteredPhonesH = phonesH.filter(s -> s.length() < 12).
                collect(Collectors.toCollection(HashSet::new));
        filteredPhonesH.forEach(s -> System.out.println(s));
        // здесь Выражение HashSet::new представляет функцию создания коллекции
        // аналогично
        phonesH = Stream.of("iPhone 8", "HTC U12", "Huawei Nexus 6P",
                "Samsung Galaxy S9", "LG G6", "Xiaomi MI6", "ASUS Zenfone 2",
                "Sony Xperia Z5", "Meizu Pro 6", "Lenovo S850");
        ArrayList<String> result = phonesH.collect(Collectors.toCollection(ArrayList::new));
    }

    public void test8() {
        System.out.println(this.getClass().getName() + " начали");
        // Поток телефонов
        Stream<Phone> phoneStream = Stream.of(
                new Phone("iPhone X", "Apple", 600),
                new Phone("Pixel 2", "Google", 500),
                new Phone("iPhone 8", "Apple", 450),
                new Phone("Nokia 9", "HMD Global", 150),
                new Phone("Galaxy S9", "Samsung", 300));
        phoneStream.sorted(new PhoneComparator())
                .forEach(p -> {
                    System.out.print("Телефон ");
                    System.out.println(p.getName() + p.getCompany());
                });
    }

    public void test9() {
        System.out.println(this.getClass().getName() + ".test9 начали");
        PhoneList phoneList = new PhoneList("Список 1");
        System.out.println(phoneList.name);
        StreamTest streamTest = new PhoneList("Список 2");
        System.out.println(streamTest.getName());
        // Так можем обратиться к методу класса, который хранится в переменной,
        // а не интерфейса
        System.out.println(((PhoneList) streamTest).getName1());
        streamTest.add(new Phone("1 iPhone X", "Apple", 600));
        streamTest.add(new Phone("2 Pixel 2", 400));
        streamTest.add(new Phone("3 Nokia 9", "HMD Global", 150));
        streamTest.add(new Phone("4 Galaxy S9", "Samsung", 300));
        System.out.println(streamTest.add(new Phone("iPhone 8",1000)));

        // Поток из метода интерфейса возвращающего List телефонов
        Stream<Phone> phs = streamTest.listPhoneList().stream();
        phs
                .forEach(p -> System.out.println(p.getName()));
        // или проще
        streamTest.listPhoneList().stream()
                .forEach(p -> System.out.println(p.getName()));
        streamTest.streamName()
                .forEach(s -> System.out.println(s));
        ((PhoneList) streamTest).testIterator();

        System.out.println("Поток");
        streamTest.stream()
                .forEach(s -> System.out.println(s));
        // Проверка, есть ли телефон ценой 150
        if (streamTest.listPhoneList().stream()
                .map(a -> a.getPrice())
                .filter(a -> a.intValue() == 150)
                .findAny()
                .orElse(-1).equals(150)) {
        }
        // Сцепление через запятую
        String s = streamTest.listPhoneList().stream()
                .map(d -> d.getCompany())
                .reduce((t, u) -> t + "," + u)
                .orElse("");
    }
    public void test10() {
        // Стрим в лист
        List<Phone> phonesCopy = Stream.of(
                new Phone("iPhone X", "Apple", 600),
                new Phone("Pixel 2", "Google", 500),
                new Phone("iPhone 8", "Apple", 450),
                new Phone("Nokia 9", "HMD Global", 150),
                new Phone("Galaxy S9", "Samsung", 300))
                .map(p -> new Phone(p.getCompany(),p.getName(),p.getPrice() * 10))
                .collect(Collectors.toList());
    }
}

// Сравнитель для телефонов
class PhoneComparator implements Comparator<Phone> {

    public int compare(Phone a, Phone b) {
        return a.getName().toUpperCase().compareTo(b.getName().toUpperCase());
    }
}

class Phone {

    private String name;
    private String company;
    private int price;

    public Phone(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Phone(String name, String comp, int price) {
        this.name = name;
        this.company = comp;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public static int compare(Phone p1, Phone p2) {
        if (p1.getPrice() > p2.getPrice()) { return 1; }
        return -1;
    }
}

interface StreamTest {

    Integer add(Phone phone);
    String getName();
    List<Phone> listPhoneList();

    Stream<String> streamName();
    <T> Stream<T> stream();

}

class PhoneList implements StreamTest {

    String name;
    List<Phone> list;
    Iterator<Phone> iterator;

    PhoneList(String name) {
        this.name = name;
        this.list = new ArrayList<>();
    }

    String getName1() {
        return name;
    }

    @Override
    public Integer add(Phone phone) {
        list.add(phone);
        return list.size();
    }

    public void testIterator(){
        System.out.println("testIterator:");
        // Метод коллекции iterator() возвращает объект итератор, то есть объект,
        // реализующий интерфейс Iterator
        iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getName());
        }
    }

    @Override
    public String getName() {
        return "Интерфейс " + name;
    }

    @Override
    public List<Phone> listPhoneList() {
        List<Phone> retList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            retList.add(list.get(i));
        }
        return retList;
    }

    @Override
    public Stream<String> streamName() {
        // Поток из list
        return list.stream()
                // Оставляем толлько названия
                .map(p -> p.getName());
    }

    @Override
    public Stream<Phone> stream() {
        return StreamSupport.stream(new MyListSpliterator(list),true);
    }

    public static class MyListSpliterator implements Spliterator {
        private final List<Phone> lo;
        private int firstPosition, lastPosition;
        private final Function<Phone, String> function;

        Function<Phone, String> convert = x-> x.getName() + " телефон";

        MyListSpliterator(List<Phone> lo) {
            this.lo = lo;
            this.firstPosition = 0;
            this.lastPosition = lo.size()-1;
            this.function = convert;
        }

        MyListSpliterator(List<Phone> lo, int f, int l) {
            this.lo = lo;
            this.firstPosition = f;
            this.lastPosition = l;
            this.function = convert;
        }

        @Override
        public int characteristics() {
            return IMMUTABLE | SIZED | SUBSIZED;
            // SIZED говорит о том, что мы точно знаем размер набора данных
            // SUBSIZED и что после разделения Spliterator мы всё ещё точно будем знать размер.
            // IMMUTABLE говорит о том, что исходные данные не могут быть изменены
            // (добавлены или удалены элементы или изменены).
            //
            // Существуют и другие характеристики:
            //
            //ORDERED говорит о том, что порядок элементов в Spliterator важен
            //SORTED обычно используется с ORDERED и говорит, что элементы в этом Spliterator отсортированы
            //DISTINCT говорит что элементы исходного набора данных уникальны.
            //NONNULL гарантирует, что в Spliterator нет null элементов.
        }

        @Override
        public long estimateSize() {
            // возвращает ожидамое (оценочное) число элементов данных, которые вернёт Spliterator
            // В случае, если Spliterator имеет характеристику SIZED,
            // этот метод обязан вернуть точное значение элементов
            return lastPosition - firstPosition;
        }

        @Override
        public long getExactSizeIfKnown() {
            // должен возвращать результат вызова estimateSize() для SIZED Spliterator
            // или -1 в остальных случаях.
            return estimateSize();
        }

        @Override
        public boolean tryAdvance(Consumer action) {
            //  проверяет, существует ли следующий элемент (от firstPosition)
            if (firstPosition < lastPosition) {
                // если существует, для него, следующего
                firstPosition++;
                // применяет на него переданную функцию
                // В данном случае не применяем никакую фонкцию
                // - просто принимаем элемент
                //action.accept(lo.get(firstPosition).getName());
                action.accept(function.apply(lo.get(firstPosition)));
                // и возвращает true
                return true;
            }
            // если элементов больше не осталось, вернуть false
            return false;
        }

        @Override
        public Spliterator<Phone> trySplit() {
            if (true) return null; // То есть просто фигачим подрял как есть, без распараллеливания
            // делим пополам
            int half = (lastPosition - firstPosition)/2;
            if (half<=1) {
                // Если Spliterator не удаётся разделить
                // Больше нечего делить - вернем null
                return null;
            }
            int f = firstPosition;
            int l = firstPosition + half;

            firstPosition = firstPosition + half +1;

            return new MyListSpliterator(lo, f, l);
        }
    }
}