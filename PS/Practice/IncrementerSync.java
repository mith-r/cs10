/**
 * An incrementer simply increments a shared variable a specified number of times.
 * This version uses "synchronized" to safely increment.
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, inspired by an example by Scot Drysdale
 * @author Chris Bailey-Kellogg, revised Winter 2014 to separate out IncrementerTotal
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2025
 */
public class IncrementerSync extends Thread {
    public static class IncrementerTotal {
        int total = 0;
        public synchronized void inc() {
            total++;
        }
    }
    private static IncrementerTotal total = new IncrementerTotal();  	// a variable shared by all incrementers
    private static final int times = 1000000;							// how many times to increment total, in each thread

    /**
     * Increments total the specified number of times
     */
    public void run() {
        for (int i = 0; i < times; i++) {
            total.inc();
        }
    }

    public static void main(String [] args) throws Exception {
        IncrementerSync inc1 = new IncrementerSync();
        IncrementerSync inc2 = new IncrementerSync();

        // Fire off threads and wait for them to complete
        inc1.start();
        inc2.start();
        inc1.join();
        inc2.join();

        System.out.println("total at end = " + total.total);
    }
}







