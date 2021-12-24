public class ArrayExample {

    public void test1() {
        System.out.println(this.getClass().getName());
        // Индексация элементов массива начинается с 0
        {
            // После объявления массива
            int nums[];
            // мы можем инициализовать его:
            nums = new int[4];  // массив из 4 чисел
        }
        {
            // Также можно сразу при объявлении массива инициализировать его:
            int nums[] = new int[4];    // массив из 4 чисел
            int[] nums2 = new int[5];   // массив из 5 чисел
        }
        {
            //  можно задать конкретные значения для элементов массива при его создании:
            int[] nums = new int[]{1, 2, 3, 5};
            int[] nums2 = {1, 2, 3, 5};
            // в этом случае в квадратных скобках не указывается размер массива
        }
        {
            int[] array = new int[]{1, 2, 3, 4, 5};
            // Цикл по всем элементам
            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);
            }
            // Специальная версия цикла for
            for (int i : array) {
                System.out.println(i);
            }
        }
        {
            System.out.println("двумерный массив три строчки и четыре столбца");
            int[][] nums2 = new int[3][4];
            System.out.println("Длина массива - это число строк = " + nums2.length);
            for (int i = 0; i < nums2.length; i++) {
                for (int j = 0; j < nums2[i].length; j++) {
                    nums2[i][j] = (i + 1) * (j + 1);
                    System.out.print(nums2[i][j] + " ");
                }
                System.out.println(
                        "      Длина элемента массива - это длина массива этого элемента = "
                                + nums2[i].length);
            }

        }
        {
            System.out.println(
                    "двумерный зубчатый массив три строчки и переменные количества столбцов");
            int[][] nums2 = new int[3][];
            nums2[0] = new int[2];
            nums2[1] = new int[3];
            nums2[2] = new int[5];
            System.out.println("Длина массива - это число строк = " + nums2.length);
            for (int i = 0; i < nums2.length; i++) {
                for (int j = 0; j < nums2[i].length; j++) {
                    nums2[i][j] = (i + 1) * (j + 1);
                    System.out.printf("%d ", nums2[i][j]);
                }
                System.out.println(
                        "      Длина элемента массива - это длина массива этого элемента = "
                                + nums2[i].length);
            }
        }
        {
            int i = 4;
            System.out.println("Факториал числа " + i + " = " + factorial(i));
        }
        {
            int j = 10;
            System.out.println("Числа Фибоначчи от 0 до " + j);
            for (int i = 0; i <= j; i++) {
                System.out.println(i + ": " + fibonachi(i));
            }
        }
    }
    static int factorial(int x){
        if (x == 1){
            return 1;
        }
        return x * factorial(x - 1);
    }
    static int fibonachi(int n){
        // Следующее число равно сумме двух предыдущих
        if (n == 0){
            return 0;
        }
        if (n == 1){
            return 1;
        }
        else{
            return fibonachi(n - 1) + fibonachi(n - 2);
        }
    }
}
