package basics;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Сравнения объектов в Java
 */
public class ComparableExample {

    public void test1() {
        Person person = new Person("Вася");
        System.out.println(person);
        HashSet<Person> persons = new HashSet<>();
        persons.add(person);
        persons.add(new Person("Петя"));
        persons.add(new Person("Коля"));
        boolean isAdded = persons.add(new Person("Коля"));
        System.out.println(isAdded);
        System.out.printf("Set содержит %d элементов: \n", persons.size());
        for (Person pers : persons) {
            System.out.println(pers);
        }
        persons.remove(person);
        System.out.printf("Set содержит %d элементов: \n", persons.size());
        for (Person pers : persons) {
            System.out.println(pers);
        }
    }

    public void test2() {
        Person person = new Person("Вася");
        System.out.println(person);
        TreeSet<Person> persons = new TreeSet<>();
        persons.add(person);
        persons.add(new Person("Петро"));
        persons.add(new Person("Владимир"));
        boolean isAdded = persons.add(new Person("Афанасий"));
        System.out.println(isAdded);
        System.out.printf("Set содержит %d элементов: \n", persons.size());
        for (Person pers : persons) {
            System.out.println(pers);
        }
    }

    public void test3() {
        Person person = new Person("Вася");
        System.out.println(person);
        PersonNameComparator pcomp = new PersonNameComparator();
        // Создадим set, используя свой созданный сравнитель, а не тот, который в классе
        TreeSet<Person> persons = new TreeSet<>(pcomp);
        persons.add(person);
        persons.add(new Person("Петро"));
        persons.add(new Person("Владимир"));
        boolean isAdded = persons.add(new Person("Афанасий"));
        System.out.println(isAdded);
        System.out.printf("Set содержит %d элементов: \n", persons.size());
        for (Person pers : persons) {
            System.out.println(pers);
        }
    }

    public void test4() {
        Person person = new Person("Вася", 12);
        System.out.println(person);
        Comparator<Person> pcomp = new PersonNameComparator()
                .thenComparing(new PersonAgeComparator());
        // Создадим set, используя свой созданный сравнитель, а не тот, который в классе
        TreeSet<Person> persons = new TreeSet<>(pcomp);
        persons.add(person);
        persons.add(new Person("Петро", 23));
        persons.add(new Person("Владимир", 46));
        boolean isAdded = persons.add(new Person("Афанасий", 21));
        System.out.println(isAdded);
        isAdded = persons.add(new Person("Афанасий", 12));
        System.out.println(isAdded);
        persons.add(new Person("Афанасий", 18));
        System.out.printf("Set содержит %d элементов: \n", persons.size());
        for (Person pers : persons) {
            System.out.println(pers);
        }
    }

}

class Person implements Comparable<Person> {

    private String name;
    private int age;

    Person(String name) {
        this.name = name;
    }

    Person(String n, int a) {
        this.name = n;
        this.age = a;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return getClass().getName() + ":"
                + " name=" + getName()
                + " age=" + age;
    }

    public int compareTo(Person p) {
        //return name.compareTo(p.getName());
        return name.length() - p.getName().length();
    }
}

// Создадим свой сравнитель сравнивающий персоны по имени
class PersonNameComparator implements Comparator<Person> {

    public int compare(Person a, Person b) {
        return a.getName().compareTo(b.getName());
    }
}

// Создадим свой сравнитель сравнивающий персоны по возрасту
class PersonAgeComparator implements Comparator<Person> {

    public int compare(Person a, Person b) {
        if (a.getAge() > b.getAge()) { return 1; } else if (a.getAge() < b.getAge()) {
            return -1;
        } else { return 0; }
    }
}


