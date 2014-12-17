package fpc.java;

/**
 * UnsafeSequence
 *
 * @author Brian Goetz and Tim Peierls
 */

public class UnsafeSequence {
    private int value;

    /**
     * Returns a unique value.
     */
    /*public int getNext() {
        return value++;
    }*/

    /* Folgender Code provoziert den Fehler

    Aber in der Tat besteht value++ aus diesen Aktionen

    */

    public int getNext() {
        int vorher = value;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value = vorher + 1;
        return value;
    }

    static class MyThread implements Runnable {
        private UnsafeSequence unsafe;
        private int pause;

        public MyThread( UnsafeSequence unsafe, int pause) {
            this.unsafe = unsafe;
            this.pause = pause;
        }
        public void run(){
            try {
            long id = Thread.currentThread().getId();
            System.out.println( String.format("%d: Starting", id));
            System.out.println( String.format("%d: Found: %d", id, unsafe.getNext()));
            Thread.sleep(pause);
            System.out.println(String.format("%d: Found: %d", id, unsafe.getNext()));
            Thread.sleep(pause);
            System.out.println(String.format("%d: Found: %d", id, unsafe.getNext()));
            Thread.sleep(pause);
            System.out.println(String.format("%d: Found: %d", id, unsafe.getNext()));
            Thread.sleep(pause);
            System.out.println(String.format("%d: Found: %d", id, unsafe.getNext()));
            Thread.sleep(pause);
            System.out.println( String.format("%d: Found: %d", id, unsafe.getNext()));
            System.out.println( String.format("%d: Stopped", id));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args) {
        UnsafeSequence unsafe = new UnsafeSequence();

        new Thread (new MyThread(unsafe, 100)).start();
        new Thread (new MyThread(unsafe, 150)).start();



    }

}
