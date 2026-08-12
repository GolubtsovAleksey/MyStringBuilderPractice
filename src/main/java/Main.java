public class Main {
    public static void main(String[] args) {

        CustomStringBuilder sb = new CustomStringBuilder("Java");
        System.out.println("Старт: " + sb);


        sb.append(" привет");
        sb.append(" пока");
        System.out.println("После добавлений: " + sb);

        System.out.println("\n--- Начинаем откаты (undo) ---");

        sb.undo();                                     // Первая отмена — уберет " пока"
        System.out.println("Undo 1: " + sb);           // Java привет

        sb.undo();                                      // Вторая отмена — уберет " привет"
        System.out.println("Undo 2: " + sb);             // Java

        sb.undo();                                       // Нечего отменять! История изменений пуста.
    }
}
