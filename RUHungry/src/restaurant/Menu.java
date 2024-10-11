package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Menu {
    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        String inputFile = "menu.in";
        String stockInput = "stock.in";
        // String outputFile = args[1];
	
        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();


	// 3. Call the menu() method to read the menu
        rh.menu(inputFile);
        rh.createStockHashTable(stockInput);
        rh.updatePriceAndProfit();

        rh.order("Cheddar Biscuits", 11);
        rh.order("House Salad", 7);
        rh.order("The RU Burger", 18);
        rh.order("Fries", 9);
        rh.order("Coca Cola", 5);
        rh.order("Mozzarella Sticks", 3);
        rh.order("The RU Burger", 15);
        rh.order("Roasted Veggies", 10);
        rh.order("Blue Raspberry Scarlet Pop", 25);
        rh.order("The Scarlet Brownie Sundae", 24);
        rh.order("Scarlet Special Salad", 3);
        rh.order("Spinach Artichoke Dip", 16);
        rh.order("Fiesta Quesadillas", 24);
        rh.order("RU & OG Cheesecake", 16);
        rh.order("Water", 20);




        




        /*rh.order("Water", 40);
        rh.order("Spinach Artichoke Dip", 11);
        rh.order("Classic Chicken Sandwich", 27);
        rh.order("Sprite", 5);
        rh.order("The Scarlet Brownie Sundae", 57);
        rh.donation("Water", 27);
        rh.donation("Chicken", 5);
        rh.donation("Lettuce", 12);
        rh.donation("Onions", 3);
        rh.donation("Potatoes", 27);
        rh.donation("Cucumber",11);
        rh.donation("Oil", 13);
        rh.donation("Lettuce", 27);
        rh.donation("Sour Cream", 15);
        rh.donation("Milk", 18);
        rh.restock("Cheese", 10);
        rh.restock("Oil", 5);
        rh.restock("Flour", 2);
        rh.restock("Bread", 15);
        rh.restock("Oil", 7);
        rh.restock("Mayo", 4);
        rh.restock("Water", 20);
        rh.restock("Lemon", 5);
        rh.restock("Salt", 2);
        rh.restock("Lettuce", 5);*/

        //rh.order("RU & OG Cheesecake", 57);
        
        


	// 4. Set output file
	// Option to remove this line if you want to print directly to the screen
        // StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();

    }
}
