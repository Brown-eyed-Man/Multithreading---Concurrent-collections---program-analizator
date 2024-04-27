import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static int textQuantity = 100;
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    public static Thread textGenerator;

    public static void main(String[] args) throws InterruptedException {
        textGenerator = new Thread(() -> {
            try {
                for (int i = 0; i < textQuantity; i++) {
                    String text = generateText("abc", 100_000);
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        textGenerator.start();

        Thread threadA = createThread(queueA, 'a');
        Thread threadB = createThread(queueB, 'b');
        Thread threadC = createThread(queueC, 'c');

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Thread createThread(BlockingQueue<String> queue, char ch) {
        return new Thread(() -> {
            int max = findMaxLetters(queue, ch);
            System.out.println("Max quantity of " + ch + " is " + max + " times.");
        });
    }

    public static int findMaxLetters(BlockingQueue<String> queue, char ch) {
        int counter = 0;
        int max = 0;
        try {
            String text = queue.take();
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == ch) {
                    counter++;
                }
            }
            if (counter > max) {
                max = counter;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return max;
    }
}