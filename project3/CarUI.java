//Collin Dreher
import java.util.*;

public class CarUI
{
	public int menu()
	{
		int choice = 0;
		System.out.println("CAR TRACKER MAIN MENU!\n");
        System.out.println("Here are your options: ");
        System.out.println("1. Add a car");
        System.out.println("2. Update an existing car");
        System.out.println("3. Remove an existing car");
        System.out.println("4. Retrieve the lowest price car");
        System.out.println("5. Retrieve the lowest mileage car");
        System.out.println("6. Retrieve the lowest price car by make and model");
        System.out.println("7. Retrieve the lowest mileage car by make and model");
        System.out.println("8. Quit\n");
		
		System.out.print("Please choose one of the above options: ");		//prompt user to make a choice
		Scanner keyboard = new Scanner(System.in);
		choice = keyboard.nextInt();
		
		if(choice < 1 || choice > 8)
			return -1;
		return choice;
	}
	
	public Car newCar()
	{
		Scanner keyboard = new Scanner(System.in);
		System.out.println("You chose to add a new car!");
		System.out.println("Enter the following: \n");
		
		System.out.print("Unique VIN number: ");
		String vin = keyboard.nextLine();
		
		System.out.print("Car's make: ");
		String make = keyboard.nextLine();
		
		System.out.print("Car's model: ");
		String model = keyboard.nextLine();
		
		System.out.print("Price to purchase (in dollars): ");
		int price = keyboard.nextInt();
		
		System.out.print("Mileage of car (in miles): ");
		int miles = keyboard.nextInt();
		keyboard.nextLine();
		System.out.print("Color of car: ");
		String color = keyboard.nextLine();
		
		Car car1 = new Car(vin, make, model, price, miles, color);
		return car1;
		
	}
}