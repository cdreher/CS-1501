//Collin Dreher

import java.util.HashMap;
import java.util.Arrays;
import java.util.*;

public class PQ
{
	private Car[] priceHeap;
	private Car[] mileageHeap;
	
	//4 HashMaps for searching options
	private HashMap<String, Integer> priceIndex;
	private HashMap<String, Integer> mileageIndex;
	private HashMap<String, Car> priceMakeModel;
	private HashMap<String, Car> mileageMakeModel; 
	
	//variables for: # cars in heap and start size of heap
	private int size = 0;
	private final int STARTSIZE = 32;
	
	
	public PQ()
	{
		priceHeap = new Car[STARTSIZE];
		mileageHeap = new Car[STARTSIZE];
		priceIndex = new HashMap<String, Integer>(STARTSIZE);
		mileageIndex = new HashMap<String, Integer>(STARTSIZE);
		priceMakeModel = new HashMap<String, Car>(STARTSIZE);
		mileageMakeModel = new HashMap<String, Car>(STARTSIZE);
	}
	
	public void addCar(Car car)			//option 1 -- add car to data structure
	{
		priceHeap[size] = car;
		mileageHeap[size] = car;
		
		//use vinNumber of car to place into hash
		priceIndex.put(car.getVinNumber(), size);
		mileageIndex.put(car.getVinNumber(), size);
		
		//swim hash map to maintain structure
		swimPrice(size);
		swimMileage(size);
		size++;		
		
		//resize heap if it is full
		if(size == priceHeap.length) resizeHeap();
		
		if(priceMakeModel.containsKey(car.getMakeModel()))
		{
			//need to check HashMap to see if Car object stored has lower price
			minMakeModel(true, car);
		}
		else
		{
			priceMakeModel.put(car.getMakeModel(), car);
		}
		
		if(mileageMakeModel.containsKey(car.getMakeModel()))
		{
			//need to check HashMap to see if Car object stored has lower mileage
			minMakeModel(false, car);
		}
		else
		{
			mileageMakeModel.put(car.getMakeModel(), car);
		}
	}
	
	
	public boolean updateCar(String vin)
	{
		if(priceIndex.containsKey(vin))
		{
			int index = priceIndex.get(vin);		//get index of desired vin
			Car c = priceHeap[index];				//find that car in the heap and store
			
			System.out.println("1. Price");
			System.out.println("2. Mileage");
			System.out.println("3. Color");
			System.out.print("Choose one of the above options to update: ");
			Scanner keyboard = new Scanner(System.in);
			int choice = keyboard.nextInt();
			
			
			if(choice == 1)
			{
				System.out.print("What would you like the new price to be? ");
				keyboard = new Scanner(System.in);
				int priceInput = keyboard.nextInt();
				int prevPrice = priceHeap[index].getPrice();
				
				c.setPrice(priceInput);
				
				swimPrice(index);
				sinkPrice(index);
			
				if(priceMakeModel.get(c.getMakeModel()) == c)
				{
					if(priceHeap[index].getPrice() < prevPrice)
						priceMakeModel.put(c.getMakeModel(), c);
					else
						newLowMakeModel(true, c.getMakeModel());
				}
				else if(priceInput < priceMakeModel.get(c.getMakeModel()).getPrice())
				{
					priceMakeModel.put(c.getMakeModel(), c);
				}
			}
			else if(choice == 2)
			{
				System.out.print("What would you like the new mileage to be? ");
				keyboard = new Scanner(System.in);
				int mileageInput = keyboard.nextInt();
				int prevMileage = mileageHeap[index].getMileage();
				
				c.setMileage(mileageInput);
				
				swimMileage(index);
				sinkMileage(index);
			
				if(mileageMakeModel.get(c.getMakeModel()) == c)
				{
					if(mileageHeap[index].getMileage() < prevMileage)
						mileageMakeModel.put(c.getMakeModel(), c);
					else
						newLowMakeModel(false, c.getMakeModel());
				}
				else if(mileageInput < mileageMakeModel.get(c.getMakeModel()).getMileage())
				{
					mileageMakeModel.put(c.getMakeModel(), c);
				}
			}
			else if(choice == 3)
			{
				System.out.print("What would you like the new color to be? ");
				keyboard = new Scanner(System.in);
				String input = keyboard.nextLine();
				
				c.setColor(input);
			}
			
			
			return true;
			
		}
		return false;
	}
	
	public boolean removeCar(String vin)		//option 3 -- remove car
	{
		if(priceIndex.containsKey(vin))
		{
			int index = priceIndex.get(vin);		//get index of desired vin
			Car c = priceHeap[index];				//find that car in the heap and store
			
			
			size--;
			
			exchange(index, size, priceHeap, priceIndex);		//swap last leaf and desired car
			priceHeap[size] = null;		//remove last leaf (i.e. the desired car)
			priceIndex.remove(vin);		//remove from hash map
			swimPrice(index);
			sinkPrice(index);
			if(priceMakeModel.get(c.getMakeModel()) == c)
			{
				priceMakeModel.remove(c.getMakeModel());
				newLowMakeModel(true, c.getMakeModel());
			}
			
			index = mileageIndex.get(vin);
			exchange(index, size, mileageHeap, mileageIndex);		//swap last leaf and desired car
			mileageHeap[size] = null;		//remove last leaf (i.e. the desired car)
			mileageIndex.remove(vin);		//remove from hash map
			swimMileage(index);
			sinkMileage(index);
			if(mileageMakeModel.get(c.getMakeModel()) == c)
			{
				mileageMakeModel.remove(c.getMakeModel());
				newLowMakeModel(false, c.getMakeModel());
			}
			
			return true;
		}
		return false;
		
	}
	
	public Car getMinPrice()
	{
		return priceHeap[0];
	}
	
	public Car getMinMileage()
	{
		return mileageHeap[0];
	}
	
	public Car getMinPriceMakeModel(String makeModel)
	{
		return priceMakeModel.get(makeModel);
	}
	
	public Car getMinMileageMakeModel(String makeModel)
	{
		return mileageMakeModel.get(makeModel);
	}
	
	
	
	private void minMakeModel(boolean isPrice, Car car)
	{
		if(isPrice) 
		{
			Car temp = priceMakeModel.get(car.getMakeModel()); 
			if(car.getPrice() < temp.getPrice())
			{
				priceMakeModel.remove(temp.getMakeModel());
				priceMakeModel.put(car.getMakeModel(), car); 
			}
		}
		else //perform same thing for mileage
		{
			Car temp = mileageMakeModel.get(car.getMakeModel());
			if(car.getMileage() < temp.getMileage())
			{
				mileageMakeModel.remove(temp.getMakeModel());
				mileageMakeModel.put(car.getMakeModel(), car); 
			}
		}
	}

	private boolean lessPrice(int i, int j)	//check to see if price at i is less than j
	{
		if(priceHeap[i] == null || priceHeap[j] == null)
			return false;
		return priceHeap[i].getPrice() < priceHeap[j].getPrice();
	}
	

	private boolean lessMileage(int i , int j)	//similar to lessPrice() method
	{
		if(mileageHeap[i] == null || mileageHeap[j] == null)
			return false;
		return mileageHeap[i].getMileage() < mileageHeap[j].getMileage();
	}
	
 
	private void exchange(int i, int j, Car[] heap, HashMap<String, Integer> map)	//exchange values in the heap, also updates hashmap
	{
		map.remove(heap[i].getVinNumber());
		map.remove(heap[j].getVinNumber());
	
		Car t = heap[i];
		heap[i] = heap[j];
		heap[j] = t;
	
		map.put(heap[i].getVinNumber(), i);
		map.put(heap[j].getVinNumber(), j);
	}
	 

	private void swimPrice(int k)			//swim car up through priceHeap	
	{
		while(k > 0 && lessPrice(k, (k-1)/2))
		{
			exchange(k,(k-1)/2, priceHeap, priceIndex);
			k = (k-1)/2;
		}
	}
	 
	
	 private void swimMileage(int k)		 //swims car up through mileageHeap
	 {
		 while(k > 0 && lessMileage(k, (k-1)/2))
		 {
			 exchange(k,(k-1)/2, mileageHeap, mileageIndex);
			 k = (k-1)/2;
		 }
	 }
	 
	
	private void sinkPrice(int k)	//sinks car down through priceHeap
	{
		while((2*k + 1) < size)
		{
			int j = 2*k + 1;
			
			if(j < size && lessPrice(j+1, j)) 
				j++;
			if(!lessPrice(j, k)) 
				break;
			exchange(k, j, priceHeap, priceIndex);
			k = j;
		}
	}
	 
	
	private void sinkMileage(int k)	//sinks car down through mileageHeap
	{
		while((2*k + 1) < size)
		{
			int j = 2*k + 1;
			
			if(j < size && lessMileage(j+1, j)) 
				j++;
			if(!lessMileage(j, k)) 
				break;
			exchange(k, j, mileageHeap, mileageIndex);
			k = j;
		}
	}
	 
	//doubles the size of each of the heaps
	private void resizeHeap()
	{
		int currSize = priceHeap.length;
		int resize = currSize*2;
		Car[] temp1 = new Car[resize]; //temp for priceHeap
		Car[] temp2 = new Car[resize]; //temp for mileageHeap
		 
		for(int i = 0; i < size; i++)
		{
			temp1[i] = priceHeap[i];
			temp2[i] = mileageHeap[i];
		}
		priceHeap = temp1;
		mileageHeap = temp2;
	}
	
	private void newLowMakeModel(boolean isPrice, String makeModel)
	{
		if(isPrice)
		{
			for(int i = 0; i < size; i++)
			{
				if(priceHeap[i].getMakeModel().equals(makeModel))
				{
					if(priceMakeModel.containsKey(makeModel))
					{
						minMakeModel(true, priceHeap[i]);
					}
					else
						priceMakeModel.put(makeModel, priceHeap[i]);
				}
			}
		}
		else
		{
			for(int i = 0; i < size; i++)
			{
				if(mileageHeap[i].getMakeModel().equals(makeModel))
				{
					if(mileageMakeModel.containsKey(makeModel))
					{
						minMakeModel(false, mileageHeap[i]);
					}
					else
						mileageMakeModel.put(makeModel, mileageHeap[i]);
				}
			}
		}
	}
}
