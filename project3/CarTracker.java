//Collin Dreher
import java.util.*;

public class CarTracker
{
	static CarUI userInterface = new CarUI();
	static PQ pq = new PQ();
	
	public static void main(String[] args)
	{
		int userChoice = 0;
		while(userChoice != -1)
		{
			userChoice = userInterface.menu();
			System.out.println();
			
			if(userChoice == 1)		//add
			{
				Car x = userInterface.newCar();
				
				if(x == null)
				{
					System.out.println("--ERROR ADDING CAR!!--");
					System.out.println("\n-----------------------\n");
					break;
				}
				
				pq.addCar(x);
			
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 2)		//update
			{
				System.out.println("You chose to update an existing car!");
				System.out.print("Please enter the VIN # of the car to remove: ");
				Scanner keyboard = new Scanner(System.in);
				String vin = keyboard.nextLine();
				
				boolean updated = pq.updateCar(vin);
				
				if(!updated)
					System.out.println("\nThis car does not exist.");
				else
					System.out.println("Car successfully updated.");
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 3)		//remove
			{
				System.out.println("You chose to remove an existing car!");
				System.out.print("Please enter the VIN # of the car to remove: ");
				Scanner keyboard = new Scanner(System.in);
				String vin = keyboard.nextLine();
				
				boolean removed = pq.removeCar(vin);
				
				if(!removed)
					System.out.println("\nThis car does not exist.");
				else
					System.out.println("Car successfully removed.");
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 4)
			{
				System.out.println("You chose to retrieve lowest price car!");
				
				Car x = pq.getMinPrice();
				if(x != null)
				{
					System.out.println("The car with the lowest price is:\n");
					x.display();
				}
				else
					System.out.println("No cars are in the system!");
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 5)
			{
				System.out.println("You chose to retrieve lowest mileage car!");
				
				Car x = pq.getMinMileage();
				if(x != null)
				{
					System.out.println("The car with the lowest mileage is:\n");
					x.display();
				}
				else
					System.out.println("No cars are in the system!");
				
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 6)
			{
				System.out.println("You chose to retrieve lowest price car by make and model!");
				System.out.print("Car's make: ");
				
				Scanner keyboard = new Scanner(System.in);
				String make = keyboard.nextLine();
				
				System.out.print("Car's model: ");
				String model = keyboard.nextLine();
				
				String makeModel = make + model;
				//System.out.println(makeModel);
				
				Car x = pq.getMinPriceMakeModel(makeModel);
				if(x != null)
				{
					System.out.println("The car with the lowest price by make and model is:\n");
					x.display();
				}
				else
					System.out.println("No cars are in the system!");
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 7)
			{
				System.out.println("You chose to retrieve lowest mileage car by make and model!");
				System.out.print("Car's make: ");
				
				Scanner keyboard = new Scanner(System.in);
				String make = keyboard.nextLine();
				
				System.out.print("Car's model: ");
				String model = keyboard.nextLine();
				
				String makeModel = make + model;
				//System.out.println(makeModel);
				
				Car x = pq.getMinMileageMakeModel(makeModel);
				if(x != null)
				{
					System.out.println("The car with the lowest mileage by make and model is:\n");
					x.display();
				}
				else
					System.out.println("No cars are in the system!");
				
				System.out.println("\n-----------------------\n");
			}
			else if(userChoice == 8)
			{
				System.out.println("----Quitting CAR TRACKER MAIN MENU----");
				break;
			}
		}
	}
}