import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;

public class ParallelArraySum {

    public static void main(String[] args) throws InterruptedException {
        int[] array = new int[500000];
        Arrays.fill(array, 1);
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        int length = array.length;

        while (length > 1) {
            int pairs = length / 2;
            CountDownLatch latch = new CountDownLatch(pairs);

            for (int i = 0; i < pairs; i++) {
                int finalIndex = i;
                int finalLength = length;
                executor.submit(() -> {
                    array[finalIndex] += array[finalLength - 1 - finalIndex];
                    latch.countDown();
                });
            }

            latch.await();

            if (length % 2 == 1) {
                length = pairs + 1;
            } else {
                length = pairs;
            }
        }

        executor.shutdown();
        System.out.println("Сума елементів масиву: " + array[0]);
    }
}