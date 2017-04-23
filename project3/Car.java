//Collin Dreher
public class Car
{
	public String vinNumber;
	public String make;
	public String model;
	public int price;
	public int mileage;
	public String color;
	
	public Car(String vin, String m, String mdl, int p, int mlg, String c)
	{
		vinNumber = vin;
		make = m;
		model = mdl;
		price = p;
		mileage = mlg;
		color = c;
	}
	
	public void display()
	{
		System.out.println("VIN number:   " + vinNumber);
		System.out.println("Make:         " + make);
		System.out.println("Model:        " + model);
		System.out.println("Price:        " + price);
		System.out.println("Mileage       " + mileage);
		System.out.println("Color:        " + color);
	}
	
	public String getVinNumber()
	{
		return vinNumber;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public void setPrice(int p)
	{
		price = p;
	}
	
	public int getMileage()
	{
		return mileage;
	}
	
	public void setMileage(int m)
	{
		mileage = m;
	}
	
	public void setColor(String c1)
	{
		color = c1;
	}
	
	public String getMakeModel()
	{
		String makeModel = make + model;
		return makeModel;
	}
}