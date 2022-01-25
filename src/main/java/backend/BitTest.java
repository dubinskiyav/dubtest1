package backend;

/**
 * Иллюстрация работы с библиотекой печати Birt
 * @see <a href="https://javarush.ru/quests/lectures/questmultithreading.level10.lecture01">javarush</a>
 */
public class BitTest {

    public void test1() {

        int x = 342;
        System.out.println(x + " = " + Integer.toBinaryString(x));
        for (int i : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}) {
            System.out.println(i + " = " + Integer.toBinaryString(i));
        }
        // Шестнадцатиричная система исчисления
        for (int i : new int[]{0x0, 0x1, 0x10, 0x100, 0x1000}) {
            System.out.println(i + " = " + Integer.toBinaryString(i));
        }
        // Двиоичная система исчисления
        for (int i : new int[]{0b0, 0b1, 0b10, 0b100, 0b1000}) {
            System.out.println(i + " = " + Integer.toBinaryString(i));
        }
    }

    public void test2() {
        for (int i : new int[]{0b0, 0b1, 0b10, 0b100, 0b1000, 0b1101}) {
            System.out.println(i + " = " + Integer.toBinaryString(i));
            System.out.println("Инвертируем 0 бит = " + Integer.toBinaryString(flipBit(i, 0)));
            System.out.println("Инвертируем 1 бит = " + Integer.toBinaryString(flipBit(i, 1)));
            System.out.println("Инвертируем 2 бит = " + Integer.toBinaryString(flipBit(i, 2)));
        }

        System.out.println("Установка маски");
        int source = 0b000101010011;
        System.out.println("source = " + Integer.toBinaryString(source));
        System.out.println("         " + Integer.toBinaryString(setBit(source, 2)));

        System.out.println("Снятие маски");
        source = 0b000101010111;
        System.out.println("source = " + Integer.toBinaryString(source));
        System.out.println("         " + Integer.toBinaryString(resetBit(source, 2)));

        System.out.println("Получение бита");
        source = 0b000101010111;
        System.out.println("source = " + Integer.toBinaryString(source));
        System.out.println("0-й бит source = " + getBit(source, 0));
        System.out.println("1-й бит source = " + getBit(source, 1));
        System.out.println("2-й бит source = " + getBit(source, 2));
        System.out.println("3-й бит source = " + getBit(source, 3));
        System.out.println("4-й бит source = " + getBit(source, 4));
        System.out.println("5-й бит source = " + getBit(source, 5));
        System.out.println("6-й бит source = " + getBit(source, 6));
        System.out.println("7-й бит source = " + getBit(source, 7));
        System.out.println("8-й бит source = " + getBit(source, 8));
        System.out.println("9-й бит source = " + getBit(source, 9));
        System.out.println("10-й бит source = " + getBit(source, 10));
        System.out.println("11-й бит source = " + getBit(source, 11));
        System.out.println("12-й бит source = " + getBit(source, 12));
    }

    // Инвертирование бита в числе (нумерация битов с 0)
    public static int flipBit(int value, int position) {
        // Чтобы инвертировать бит на определенной позиции,
        // нужно сдвинуть 1 на желаемое число позиций влево (это будет маска)
        int m = 1 << position;
        // и затем сделать XOR с исходным числом.
        return value ^ m;
    }

    // Установка бита в числе (нумерация битов с 0)
    public static int setBit(int value, int position) {
        // Генерируем маску
        int m = 1 << position;
        System.out.println("mask = " + Integer.toBinaryString(m));
        // Установка бита производится при помощи операции «побитового «ИЛИ»
        return value | m;
    }

    // Снятие бита в числе (нумерация битов с 0)
    public static int resetBit(int value, int position) {
        // Генерируем маску
        int m = 1 << position;
        System.out.println("mask = " + Integer.toBinaryString(m));
        //  «маску» необходимо поразрядно инвертировать,
        //  то есть единственный бит равный 1 должен стать равен 0,
        //  а остальные биты, изначально равные 0, должны стать равными 1
        int mm = ~m;
        System.out.println("invert mask = " + Integer.toBinaryString(mm));
        // Для снятия бита используется операция «побитового «И»
        return value & mm;
    }

    // Получить значение бита в числе (нумерация битов с 0)
    public static int getBit(int value, int position) {
        // Непонятно как, но работает
        if (value << ~position < 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
