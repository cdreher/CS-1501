# CS/COE 1501 Project 3
Goal:
To explore an advanced application of priority queues in order to gain a deeper understanding of the data structure.

High-level description:
You will be writing a basic application to help a user select a car to buy. You will write a menu-based user interface driver program (to be run in the terminal, no GUI), but most of the logic will be in implementing a priority queue-based data structure. You should write a PQ-based data structure that stores objects according to the relative priorities of two of their attributes, making it efficient to retrieve objects with the minimum value of either attribute. Your data structure should further be indexable to allow for efficient updates of entered items. You will want users to be able to enter details about cars that they are considering buying. The user should then be able to efficiently retrieve the car with the lowest mileage or lowest price. These retrievals should be possible on the set of all entered cars or on the set of all cars of a specific make and model (e.g., "lowest price Ford Fiesta", "lowest mileage Cadillac Escalade").

Specifications:
First you must create a class to store data about cars to buy. Specifically, this class must contain the following information:
A unique VIN number (17 character string of numbers and capital letters (but no I (i), O (o), or Q (q) to avoid confusion with numerals 1 and 0)
The car's make (e.g., Ford, Toyota, Honda)
The car's model (e.g., Fiesta, Camry, Civic)
The price to purchase (in dollars)
The mileage of the car
The color of the car
You must write a terminal menu-based driver program (again, no GUI). Specifically, your driver must present the user with the following options:
Add a car
This will (one at a time) prompt the user for all of the above-listed attributes of a car to keep track of.
Update a car
This option will prompt the user for the VIN number of a car to update, and then ask the user if they would like to update 1) the price of the car, 2) the mileage of the car, or 3) the color of the car.
Remove a specific car from consideration
This option will prompt the user for the VIN number of a car to remove from the data structure (e.g., if it is no longer for sale).
Note that this mean you will need to support removal of cars other than the minimum (price or mileage) car.
Retrieve the lowest price car
Retrieve the lowest mileage car
Retrieve the lowest price car by make and model
This option will prompt the user to enter (one at a time) a make and model and then return the car with the minimum price for that make and model.
Retrieve the lowest mileage car by make and model
This option will prompt the user to enter (one at a time) a make and model and then return the car with the minimum mileage for that make and model.
Retrieval operations should not remove the car with minimum price or mileage from the data structure, just print the information about that car. Cars should only be removed via the "remove a specific car from consideration" menu option.
To ensure efficiency of operations, you must base your data structure around the use of heaps with indirection (in order to make them indexable). Note that operations on either attribute (e.g., retrieve minimum price, retrieve minimum mileage) must have no worse than logarithmic runtime (both for all cars and for a specific make and model). Updates and removals must also had a logarithmic runtime. Take care in selecting your approach to the indirection data structure to account for the types of keys you will need to store and the type and number operations that you will need to perform on them.
Because this project requires you to make a number of decisions about how to implement its requirements, you will need to write documentation explaining your implementation, and justifying your decisions. Name this file documentation.txt. Be sure to carefully document your approach to ease the effort required to trace through your code for grading. Be sure to include descriptions of the runtime and space requirements of your approach and use them in your justification of why you think your approach is the best way to go.

Submission Guidelines:
DO NOT upload any IDE package files.
You must name the primary driver for your program CarTracker.java.
You must be able to compile your game by running javac CarTracker.java.
You must be able to run your program with java CarTracker.
You must document and justify your approach in documentation.txt.
You must fill out info_sheet.txt.
The project is due at 11:59 PM on Saturday, March 18. Upload your progress to Box frequently, even far in advance of this deadline. No late assignments will be accepted. At the deadline, your Box folder will automatically be changed to read-only, and no more changes will be accepted. Whatever is present in your Box folder at that time will be considered your submission for this assignment—no other submissions will be considered.
Additional Notes and Hints:
You are free to use code provided by the textbook authors in implementing your solution. It is up to you to decide if it would be easier to modify the provided code to meet the requirements of this project, or if it would be easier to start with a clean slate with all of your own code.
Your program does not need to enforce that users enter properly formatted VIN numbers, but you must design your data structure to operate efficiently on VIN numbers as specified here. This should make testing your program much easier.
