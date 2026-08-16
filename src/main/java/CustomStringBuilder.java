import java.util.Arrays;
import java.util.Stack;

public class CustomStringBuilder {

    private char[] value;
    private int size;                                               // Количество фактически записанных символов (длина строки)
    private final Stack<Snapshot> history = new Stack<>();          // Стек для хранения внешних объектов-снимков (история изменений)
    private static final int DEFAULT_CAPACITY = 16;

    public CustomStringBuilder() {                                    // Конструктор по умолчанию (создает пустую строку)
        this.value = new char[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public CustomStringBuilder(String str) {                         // Конструктор, принимающий начальный текст
        if (str == null) {
            str = "null";
        }
        this.size = str.length();
        this.value = new char[this.size + DEFAULT_CAPACITY];
        str.getChars(0, this.size, this.value, 0);
    }

    private void saveSnapshot() {                                     // Сохранение текущего состояния в стек перед изменениями
        history.push(new Snapshot(this.value, this.size));             // Теперь мы создаем объект внешнего класса Snapshot
    }

    private void ensureCapacity(int minimumCapacity) {                 // Автоматическое расширение массива при нехватке места
        if (minimumCapacity - value.length > 0) {
            int newCapacity = (value.length * 2) + 2;
            if (newCapacity - minimumCapacity < 0) {
                newCapacity = minimumCapacity;
            }
            value = Arrays.copyOf(value, newCapacity);
        }
    }

    public CustomStringBuilder append(String str) {                   //  добавление строки
        if (str == null) {
            str = "null";
        }
        saveSnapshot();                                     // Запомнил состояние ДО изменения
        int len = str.length();
        ensureCapacity(size + len);             // Проверяем, поместится ли новая строка
        str.getChars(0, len, value, size);            // Копируем символы новой строки в наш массив
        size += len;                                          // Увеличиваем размер строки
        return this;
    }

    public CustomStringBuilder undo() {                         //  отмена последнего действия
        if (!history.isEmpty()) {
            Snapshot lastState = history.pop();                   // Достал верхний снимок из стека
            this.value = lastState.getValueCopy();             // Восстанавливаю массив и размер, используя публичные геттеры Snapshot
            this.size = lastState.getSizeCopy();
        } else {
            System.out.println("Нечего отменять! История изменений пуста.");
        }
        return this;
    }

    @Override
    public String toString() {                        // представил массив в виде строки
        return new String(value, 0, size);
    }
}
