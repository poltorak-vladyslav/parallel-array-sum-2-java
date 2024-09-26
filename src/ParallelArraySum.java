import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelArraySum {

    public static void main(String[] args) throws InterruptedException {
        int[] array = {1, 2, 3, 4, 5, 6}; // Початковий масив
        int processors = Runtime.getRuntime().availableProcessors(); // Кількість доступних процесорів
        ExecutorService executor = Executors.newFixedThreadPool(processors); // Пул потоків

        while (array.length > 1) {
            int length = array.length / 2; // Кількість пар
            AtomicIntegerArray currentArray = new AtomicIntegerArray(array); // Масив для поточної хвилі
            CountDownLatch latch = new CountDownLatch(length); // Синхронізація хвилі

            for (int i = 0; i < length; i++) {
                int index = i; // Остаточний індекс для потоку
                executor.submit(() -> {
                    int sum = currentArray.get(index) + currentArray.get(currentArray.length() - 1 - index); // Обчислення суми пари
                    currentArray.set(index, sum); // Оновлення елемента з меншим індексом
                    latch.countDown(); // Зменшення лічильника після завершення
                });
            }

            latch.await(); // Очікування завершення всіх потоків у хвилі

            // Оновлення масиву для наступної хвилі
            // Створюємо новий масив для зберігання результатів
            int[] newArray = new int[length + (array.length % 2)]; // Додаємо 1, якщо є непарна кількість елементів

            // Копіюємо результати
            for (int i = 0; i < length; i++) {
                newArray[i] = currentArray.get(i); // Копіюємо лише актуальні елементи
            }
            // Якщо є непарний елемент, переносимо його
            if (array.length % 2 == 1) {
                newArray[length] = currentArray.get(length); // Додаємо середній елемент
            }
            array = newArray; // Оновлюємо масив
        }

        executor.shutdown(); // Завершення роботи пулу потоків
        System.out.println("Сума елементів масиву: " + array[0]); // Виведення результату
    }
}