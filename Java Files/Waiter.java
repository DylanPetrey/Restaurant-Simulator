public class Waiter extends RestaurantSimulator implements Runnable{
    // Waiter ID
    private final int waiterId;

    Waiter(int waiterId){
        this.waiterId = waiterId;
    }

    public void run() {
        try {
            // Pick table
            int tableNumber = waiterId;
            System.out.println("Waiter " + waiterId + " chooses table " + tableNumber);

            while(true) {
                // Wait for customer's order
                System.out.println("Waiter " + waiterId + " is waiting for a customer");

                // Take customer's order
                waiterCall[tableNumber].acquire();
                System.out.println("Waiter " + waiterId + " goes to the customer and is ready to take the order");

                // Get customer's name
                System.out.println("Waiter " + waiterId + " asks for the customer's name");
                getCustomerID[tableNumber].release();
                giveCustomerID[tableNumber].acquire();

                // Take order from the customer
                takeOrder[tableNumber].release();
                giveOrder[tableNumber].acquire();
                System.out.println("Waiter " + waiterId + " takes Customer " + currentOrder[tableNumber] + "'s order");

                // Place the order
                kitchen.acquire();
                System.out.println("Waiter " + waiterId + " enters the kitchen");
                Thread.sleep((int)(Math.random() * 400 + 100));
                System.out.println("Waiter " + waiterId + " delivers the order to the kitchen");
                System.out.println("Waiter " + waiterId + " exits the kitchen");
                kitchen.release();

                // Wait for order
                System.out.println("Waiter " + waiterId + " waits for the order");
                Thread.sleep((int)(Math.random() * 700 + 300));

                // Pick up order
                kitchen.acquire();
                System.out.println("Waiter " + waiterId + " enters the kitchen");
                Thread.sleep((int)(Math.random() * 400 + 100));
                System.out.println("Waiter " + waiterId + " picks up order in the kitchen");
                System.out.println("Waiter " + waiterId + " exits the kitchen");
                kitchen.release();

                // Deliver order
                System.out.println("Waiter " + waiterId + " brings the order to customer " + currentOrder[tableNumber]);
                food[tableNumber].release();
                numServed++;
            }
        }  catch (InterruptedException e) {
            System.out.println("Waiter " + waiterId + " is cleans up the table and leaves the restaurant");
        }
    }
}
