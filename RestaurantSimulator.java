import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class RestaurantSimulator {
    public static final int NUM_WAITERS = 5;
    public static final int NUM_CUSTOMERS = 100;

    // Semaphores
    static Semaphore doors = new Semaphore(2, true);
    static Semaphore[] waiterCall = new Semaphore[NUM_WAITERS];
    static Semaphore[] getCustomerID = new Semaphore[NUM_WAITERS];
    static Semaphore[] giveCustomerID = new Semaphore[NUM_WAITERS];
    static Semaphore[] tables = new Semaphore[NUM_WAITERS];
    static Semaphore[] takeOrder = new Semaphore[NUM_WAITERS];
    static Semaphore[] giveOrder = new Semaphore[NUM_WAITERS];
    static Semaphore[] food = new Semaphore[NUM_WAITERS];
    static Semaphore kitchen = new Semaphore(1);
    static Semaphore register = new Semaphore(1);


    // Shared values between threads
    public static int completedCustomers = 0;
    public static int numServed = 0;
    public static int[] tableLine = new int[NUM_WAITERS];
    public static int[] atTable = new int[NUM_WAITERS];
    public static int[] currentOrder = new int[NUM_WAITERS];

    // Thread objects
    static Thread[] waiters;
    static Thread[] customers;



    public static void main(String[] args) throws InterruptedException {
        // Fill initial variables
        Arrays.fill(tableLine, 0);
        Arrays.fill(atTable, 0);
        Arrays.fill(currentOrder, -1);
        for(int i = 0; i < NUM_WAITERS; i++) {
            tables[i] = new Semaphore(4,true);
            takeOrder[i] = new Semaphore(0, true);
            giveOrder[i] = new Semaphore(0, true);
            food[i] = new Semaphore(0, true);
            waiterCall[i] = new Semaphore(0, true);
            getCustomerID[i] = new Semaphore(0, true);
            giveCustomerID[i] = new Semaphore(0, true);
        }

        // Create threads
        waiters = new Thread[NUM_WAITERS];
        customers = new Thread[NUM_CUSTOMERS];
        for (int k = 0; k < NUM_WAITERS; k++)
            waiters[k] = new Thread(new Waiter(k));
        for (int k = 0; k < NUM_CUSTOMERS; k++)
            customers[k] = new Thread(new Customer(k));

        // Start threads
        for (int k = 0; k < NUM_WAITERS; k++)
            waiters[k].start();
        for (int k = 0; k < NUM_CUSTOMERS; k++)
            customers[k].start();
    }

}
