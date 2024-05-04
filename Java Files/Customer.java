import java.util.Random;

public class Customer extends RestaurantSimulator implements Runnable {
    private final int customerId;

    Customer(int customerId) {
        this.customerId = customerId;
    }

    public void run() {
        try     {
            // Pick a random table to eat at
            Random random = new Random();
            int firstTableChoice = random.nextInt(NUM_WAITERS);
            int backupTableChoice = random.nextInt(NUM_WAITERS);
            if (firstTableChoice != backupTableChoice)
                System.out.println("Customer " + customerId + " chooses the table " + firstTableChoice +
                        " as their first choice and table " + backupTableChoice + " as a backup");
            else
                System.out.println("Customer " + customerId + " chooses the table " + firstTableChoice + " as their first choice");

            // Enter restaurant
            doors.acquire();
            System.out.println("Customer " + customerId + " enters through a door");
            doors.release();

            // Pick table to go sit at
            int currentTable = firstTableChoice;
            if (tableLine[firstTableChoice] >= 7 && tableLine[backupTableChoice] < 7)
                currentTable = backupTableChoice;
            System.out.println("Customer " + customerId + " selects the table " + currentTable);

            // Get in line to sit at table
            if(atTable[currentTable] >= 4){
                System.out.println("Customer " + customerId + " gets in line for table " + currentTable);
                tableLine[currentTable]++;
            }

            // Sit at table
            tables[currentTable].acquire();
            tableLine[currentTable] = (tableLine[currentTable] >=  4) ? tableLine[currentTable]-1 : tableLine[currentTable];
            atTable[currentTable]++;
            System.out.println("Customer " + customerId + " has sit down at table " + currentTable);

            // Call waiter
            System.out.println("Customer " + customerId + " has called waiter " + currentTable);
            waiterCall[currentTable].release();

            // Give waiter the customer's name
            getCustomerID[currentTable].acquire();
            System.out.println("Customer " + customerId + " gives their name to Waiter " + currentTable);
            currentOrder[currentTable] = customerId;
            giveCustomerID[currentTable].release();

            // Give order to waiter
            takeOrder[currentTable].acquire();
            System.out.println("Customer " + customerId + " gives order to Waiter " + currentTable);
            giveOrder[currentTable].release();

            // Waiter brings back order
            food[currentTable].acquire();
            System.out.println("Customer " + customerId + " receives the food from Waiter " + currentTable);

            //Customer eats the food
            System.out.println("Customer " + customerId + " eats the food");
            Thread.sleep((int) (Math.random() * 800 + 200));

            //Customer leaves the table
            atTable[currentTable]--;
            System.out.println("Customer " + customerId + " leaves the table");
            tables[currentTable].release();

            //Customer pays for food
            register.acquire();
            System.out.println("Customer " + customerId + " pays at the register");

            //Customer leaves the restaurant
            System.out.println("Customer " + customerId + " leaves the restaurant");
            register.release();

            // Interrupt the waiters if it is the last customer
            completedCustomers++;
            if (completedCustomers == NUM_CUSTOMERS) {
                for (int i = 0; i < NUM_WAITERS; i++) {
                    waiters[i].interrupt();
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Error in Customer " + customerId + ": " + e);
        }
    }
}

