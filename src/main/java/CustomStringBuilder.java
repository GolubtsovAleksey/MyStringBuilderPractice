import java.util.Arrays;
import java.util.Stack;

public class CustomStringBuilder {

    private char[] value;                        // Внутренний массив для физического хранения текста
    private int size;                            // Количество записанных символов
    private final Stack<Snapshot> history = new Stack<>();       // Опекун(Caretaker) -> Стек для хранения снимков состояний (история изменений)
    private static final int DEFAULT_CAPACITY = 16;             // размер массива


    private static class Snapshot {              // Внутренний класс-снимок для паттерна Snapshot
        private final char[] valueCopy;          // хранение копии символов в момент снимка
        private final int sizeCopy;              //  размер строки в момент создания снимка

        public Snapshot(char[] value, int size) {                    // Делаю глубокую копию массива символов
            this.valueCopy = Arrays.copyOf(value, value.length);    // создаю копию массива в памяти и сохраняем ее ссылку
            this.sizeCopy = size;                                   // текущий размер строки
        }
    }

    public CustomStringBuilder() {                      //  с пустой строкой
        this.value = new char[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public CustomStringBuilder(String str) {               // принимает строку
        if (str == null) {
            str = "null";
        }
        this.size = str.length();                             // получаю длину переданной строки и сохраняю ее в size
        this.value = new char[this.size + DEFAULT_CAPACITY];   // создаю массив с запасом(длина текста + 16 ячеек)
        str.getChars(0, this.size, this.value, 0);     // копирую символы из объекта String в value
    }

    private void saveSnapshot() {                                // Сохранение текущего состояния в стек перед изменениями
        history.push(new Snapshot(this.value, this.size));        // создал объект Snapshot с текущими данными на вершине стека
    }

    private void ensureCapacity(int minimumCapacity) {                 // расширение массива при нехватке места
        if (minimumCapacity - value.length > 0) {                      // если требуемый размер строки превышает текущую длину массива
            int newCapacity = (value.length * 2) + 2;
            if (newCapacity - minimumCapacity < 0) {                   // если новая расчетная емкость все еще меньше, чем необходимо
                newCapacity = minimumCapacity;                         // Принудительно делаем новую = минимально необходимой
            }
            value = Arrays.copyOf(value, newCapacity);                 // выделяю новый массив большего размера и копирую туда старые символы
        }
    }

    public CustomStringBuilder append(String str) {
        if (str == null) {
            str = "null";
        }
        saveSnapshot();                             // 1) Создаю снимок и кладем в стек ДО внесения изменений
        int len = str.length();                     // длина добавляемой строки
        ensureCapacity(size + len);     // 2) Проверяем, поместится ли новая строка
        str.getChars(0, len, value, size);      // 3) записываю символы новой строки в массив, начиная с позиции size
        size += len;                                      // счётчик
        return this;                                    // возвращаю ссылку на текущий объект для вызовов по цепочке через точку
    }


    public CustomStringBuilder undo() {               // мой метод отмены последнего действия
        if (!history.isEmpty()) {
            Snapshot lastState = history.pop();             // Достаем самый верхний (последний) снимок из стека
            this.value = lastState.valueCopy;                 // Восстанавливаем массив и размер из этого снимка
            this.size = lastState.sizeCopy;
        } else {
            System.out.println("Нечего отменять! История изменений пуста.");
        }
        return this;
    }

    @Override
    public String toString() {                       // Преобразовал массив в строку для вывода
        return new String(value, 0, size);
    }
}
