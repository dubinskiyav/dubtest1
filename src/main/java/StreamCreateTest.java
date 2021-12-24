import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamCreateTest {

    public void test1() {
        System.out.println(this.getClass().getName() + ".test1 начали");
        StreamCreatable sc = new StreamCreate();
        sc.StreamStrInt(1,10)
                .forEach(s -> System.out.println(s));
        sc.ParallelStreamStrInt(1,10)
                .forEach(s -> System.out.println(s));
        System.out.println(this.getClass().getName() + ".test1 кончили");
    }

}

// Интерфейс создания потока
interface StreamCreatable {
    // Поток из строк из числе
    Stream<String> StreamStrInt(int from, int to);
    Stream<String> ParallelStreamStrInt(int from, int to);
}

// Класс, реализующий интерфейс создавания потоков
class StreamCreate implements StreamCreatable {

    @Override
    public Stream<String> StreamStrInt(int from, int to) {
        //return StreamSupport.stream(new CreateSpliterator(1,10),false);
        return StreamSupport.stream(new IntSpliterator(1,10),false);
    }

    @Override
    public Stream<String> ParallelStreamStrInt(int from, int to) {
        //return StreamSupport.stream(new IntSpliterator(1,10),true);
        return StreamSupport.stream(new IntSpliterator(1,10),true);
    }

    public class IntSpliterator extends Spliterators.AbstractSpliterator {
        private int last; // последнее число
        private int curr; // текущее число

        // Функция преобразования Integer в String
        Function<Integer, String> convertIntToStr = x-> Integer.toString(x);

        protected IntSpliterator(int first, int last) {
            super(Long.MAX_VALUE, 0);
            this.last = last;
            this.curr = first;
        }

        @Override
        public boolean tryAdvance(Consumer action) {
            if (curr <= last) {
                // текущий меньше или равен последнему - еще есть элементы
                // просто принимаем элемент
                //action.accept(Integer.toString(curr));
                // Применяет функцию convertIntToStr к элементу
                action.accept(convertIntToStr.apply(curr));
                curr++;
                return true;
            } else {
                // больше нет элементов
                return false;
            }
        }
    }

    public class CreateSpliterator implements Spliterator {
        private int first; // первое число
        private int last; // последнее число
        private int curr; // текущее число

        // Функция преобразования Integer в String
        Function<Integer, String> convertIntToStr = x-> Integer.toString(x);

        public CreateSpliterator(int first, int last) {
            this.first = first;
            this.last = last;
            this.curr = first;
        }

        @Override
        // Возвращает следующий элемент потока
        public boolean tryAdvance(Consumer action) {
            if (curr <= last) {
                // текущий меньше или равен последнему - еще есть элементы
                // просто принимаем элемент
                //action.accept(Integer.toString(curr));
                // Применяет функцию convertIntToStr к элементу
                action.accept(convertIntToStr.apply(curr));
                curr++;
                return true;
            } else {
                // больше нет элементов
                return false;
            }
        }

        @Override
        // Возвращает String - это и есть тип данных в потоке
        public Spliterator<String> trySplit() {
            // для начала просто вернем null
            //return null;
            int half = (last - first)/2;
            if (half<=1) {
                // Больше нечего делить - вернем null
                return null;
            }
            // 5 6 7 8 9 10
            // first = 5
            // last = 10
            // half = 2
            int newLast = last; // 10
            int newFirst = first + half + 1; // 8
            last = newFirst - 1; // 7
            return new CreateSpliterator(newFirst,newLast); // 8 9 10
        }

        @Override
        public long estimateSize() {
            // возвращает ожидамое (оценочное) число элементов данных, которые вернёт Spliterator
            // В случае, если Spliterator имеет характеристику SIZED,
            // этот метод обязан вернуть точное значение элементов
            return last - first + 1;
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
    }

}
