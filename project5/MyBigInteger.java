//Collin Dreher
import java.util.*;
import java.math.*;
import java.io.*;

public class MyBigInteger
{
	public byte[] byteArray;
	public int numBytes = 0;
	public int[] intArray;
	
	public MyBigInteger(byte[] arr)		//constructor for byte array input
	{
		this.byteArray = new byte[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			this.byteArray[i] = arr[i];
			this.numBytes++;
		}
	}
	
	public MyBigInteger(int bits, int num)		//constructor for signal number input
	{
		this.byteArray = new byte[(bits/8)];
		for(int i = 0; i < byteArray.length; i++)
		{
			if(i == byteArray.length-1)
				byteArray[i] = (byte)num;
			else
				byteArray[i] = 0;
		}
	}
	
	public MyBigInteger add(int x)
	{
		int[] m1IntArr = this.toIntArray();		//integer array
		byte[] result = new byte[m1IntArr.length];		//result byte array
		
		for(int i = m1IntArr.length-1; i > 0; i--)
		{
			if(i == (m1IntArr.length-1))
				result[i] = (byte)(m1IntArr[i]+x);
			else
				result[i] = (byte)m1IntArr[i];
		}
	
		
		return new MyBigInteger(result);
	}
	
	public MyBigInteger subtract(int x)
	{
		int[] m1IntArr = this.toIntArray();		//multiplicand 1 integer array
		byte[] result = new byte[m1IntArr.length];		//result byte array
		
		for(int i = m1IntArr.length-1; i > 0; i--)
		{
			if(i == m1IntArr.length-1)
				result[i] = (byte)(m1IntArr[i]-x);
			else
				result[i] = (byte)m1IntArr[i];
		}
	
		
		return new MyBigInteger(result);
	}
	
	public MyBigInteger multiply(MyBigInteger m2)
	{
		BigInteger p = new BigInteger(this.byteArray);
		BigInteger q = new BigInteger(m2.byteArray);
		return new MyBigInteger(p.multiply(q).toByteArray()); 
		
	}
	
	public MyBigInteger gcd(MyBigInteger b)
	{
		int[] arr1 = this.toIntArray();		//convert to int
		int[] arr2 = b.toIntArray();			//convert to int
		//System.out.println(Arrays.toString(arr1));
		byte[] result = new byte[arr1.length];
		int e = 0;
		long h2=0;			//integers for horners method
		int exponent = 3;
		int shift = 24;
		//System.out.println("TESTTEST: " + (result.length-1));
		
		for(int i = 0; i < arr1.length; i++)
		{
			e += arr1[i];
		}
		
		for(int i = 0; i < arr2.length; i++)
		{
			
			//System.out.println("TEST I: " + i);
			int index = i+3;
			int current = i;
			//System.out.println(index + " " + current);
			while(exponent >= 0 && current <= index)		//take every 4 cells and combine using horner's method
			{
				if(exponent==0)
				{
					h2+= (long)(arr2[current]);
					//System.out.println("TEST: " + h2);
					exponent--;
					current++;
				}
				else
				{
					h2 += (long)(arr2[current] * (int)(Math.pow(256, exponent)));
					//System.out.println(arr2[index] + " * " + (int)(Math.pow(256, exponent)));
					//System.out.println(h1);
					//System.out.println(h2);
					exponent--;
					current++;
				}
			}
			//System.out.println(h1);
			//System.out.println(h2);
			if(h2 < 0)
				h2 = h2*(-1);
			for(int j = 0; j < arr1.length; j++)
			{
				result[j] = (byte)((h2%e) >> shift);
				//System.out.println(result[j]);
				shift -= 8;
			}
			exponent = 3;
			shift = 24;
			h2=0;
			if(index == (arr2.length-1))
				break;
	
		}
		
		return new MyBigInteger(result);
		
	}
	
	public MyBigInteger modInverse(MyBigInteger a)
	{
		//return new MyBigInteger(new BigInteger(a.byteArray).modInverse(new BigInteger(byteArray)).toByteArray());
		BigInteger e = new BigInteger(a.byteArray);
		BigInteger phi = new BigInteger(byteArray);
		BigInteger res = e.modInverse(phi);
		MyBigInteger result = new MyBigInteger(res.toByteArray());
		return result;
	}
	
	public int compareTo(MyBigInteger b)
	{
		int[] arr1 = this.toIntArray();
		int[] arr2 = b.toIntArray();
		
		for(int i = arr1.length-1; i > 0; i--)
		{
			if(arr1[i] > arr2[i])
				return 1;
			if(arr1[i] < arr2[i])
				return -1;
		}
		return 0;
	}
	
	
	public int[] toIntArray()
	{
		intArray = new int[byteArray.length];
		for(int i = 0; i < byteArray.length; i++)
		{
			intArray[i] = byteArray[i] & 0xff;
		}
		return intArray;
	}
	
	public String toString()
	{
		return Arrays.toString(byteArray);
	}
}
