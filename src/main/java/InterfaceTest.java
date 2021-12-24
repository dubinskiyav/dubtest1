public class InterfaceTest {

    public void test1() {
        System.out.println(this.getClass().getName() + ".test1 начали");
        Dishes dishes = new Dishes("Чайник");
        dishes.print();
        dishes.print1();
        Food food = new Food("Хлеб");
        food.print();
        food.print1();
        Printable printable = new Dishes("Кофейник");
        printable.print();
        printable = new Dishes("Молоко");
        printable.print();
        // так не можем       printable.print1();
        /*
        Получается, что интерфейс Printable - это абстрактный класс у которого есть наследники
        Dishes и Food
        и мы можем создавать экземпляры этого абстрактного класса
        причем хоть Dishes хоть Food и у них гарантированно вызывать метод print
         */
        Printable p = new Dishes("Сковородка");
        p.print();
        // // Интерфейс не имеет метода getName
        // поэтому так нельзя       String s = p.getName();
        // а после явного преобразования типа можно
        String s = ((Dishes) p).getName();
        System.out.println(s);
        // Причем, не у всех экземпляров p есть метод getName, например, у Food его нет
        p.printDef();
        p = new Food("Чай");
        p.printDef();
        test2(new Dishes("Кружка"));
        test2(new Food("Пиво"));
        // Создаем кнопку со своим обработчиком
        // у которого обязательно должен быть метод execute
        Button button = new Button(new EventHandler() {
            public void execute() {
                System.out.println("Кнопка телефизора нажата");
            }
        });
        button.click();
    }

    // Интерфейс можно передавать параметром
    public void test2(Printable p) {
        p.print();
    }
}

interface Printable {

    void print();

    default void printDef() {
        System.out.println("Печать не реализована у класса");
    }
}

class Dishes implements Printable {

    String name;

    Dishes(String name) {
        this.name = name;
    }

    // Реализация интерфейса
    public void print() {
        System.out.println("Посуда " + name);
    }

    // Просто метод
    public void print1() {
        System.out.println("Посуда1 " + name);
    }

    String getName() {
        return name;
    }
}

class Food implements Printable {

    String name;

    Food(String name) {
        this.name = name;
    }

    // Реализация интерфейса
    public void print() {
        System.out.println("Еда " + name);
    }

    // Просто метод
    public void print1() {
        System.out.println("Еда1 " + name);
    }

    // Просто метод
    public void printDef() {
        System.out.println("Еда по умолчанию " + name);
    }
}

// Интерфейс - обработчик событий
interface EventHandler {

    void execute();
}

// Класс кнопка, у которого параметром - экземпляр класса, реализующего какой-то обработчик
class Button {

    EventHandler eventHandler; // обработчик

    Button(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void click() {
        // Здесь мы вызываем интерфейс execute
        eventHandler.execute();
    }
}

