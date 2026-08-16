import java.util.Arrays;

/**
 * Неизменяемый класс-снимок для паттерна Snapshot.
 * Хранит копию состояния CustomStringBuilder на определенный момент времени.
 */
public final class Snapshot {
    private final char[] valueCopy;
    private final int sizeCopy;

    public Snapshot(char[] value, int size) {           // Конструктор принимает текущий рабочий массив и размер строки.
        this.valueCopy = Arrays.copyOf(value, value.length);    // Делаем глубокое изолированное копирование массива символов в памяти
        this.sizeCopy = size;
    }

    public char[] getValueCopy() {
        return Arrays.copyOf(valueCopy, valueCopy.length);  // Геттер для получения сохраненной копии массива. Возвращает копию, чтобы никто не мог изменить внутренний массив снимка извне.
    }

    public int getSizeCopy() {     // Геттер для получения сохраненного размера строки.
        return sizeCopy;
    }
}
