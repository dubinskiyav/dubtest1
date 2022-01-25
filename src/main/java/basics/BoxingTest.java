package basics;

/**
 * Автоупаковка и распаковка в Java
 */
public class BoxingTest {

    public void test1() {
        System.out.println(this.getClass().getName() + ": test1");
        String s = "12345";
        Integer i = Integer.valueOf(s);
        System.out.println(i);
        int ii = i.intValue();
        System.out.println(ii);

        // Значения между -127 : 127 это не ссылки а прямо значения
        Integer inCacheValue = 127;
        Integer inCacheValue2 = 127;
        System.out.println(inCacheValue == inCacheValue2); //true так как это простые значения
        // Значения за пределами - это ссылки
        Integer notInCache = 128; // new Integer(129)
        Integer notInCache2 = 128; // new Integer(129)
        System.out.println(notInCache == notInCache2); //false так как это не значения а ссылки
        System.out.println(notInCache.equals(notInCache2));// true так как сравниваем значения из ссылок
        int first = 1;
        int second = 5;
        System.out.println(Integer.max(first, second));
        System.out.println(Character.toLowerCase('S'));
        int int1 = 18;
        method(int1);
        Integer integer1 = 18;
        method(integer1);
    }

    public void method(int i) {
        System.out.println("int=" + i);
    }
    public void method(Integer i) {
        System.out.println("Integer=" + i);
    }

    public void test2() {
        Integer i = 12345678;
        Integer j = i.intValue();
        //System.out.println(i==j);
        System.out.println(i==j);
        System.out.println(i.equals(j));
    }

}
