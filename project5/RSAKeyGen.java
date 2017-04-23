//Collin Dreher
import java.util.*;
import java.math.*;
import java.io.*;

public class RSAKeyGen
{
	public static void main(String[] args)
	{
		Random r = new Random();
		
		//create p
		MyBigInteger p = new MyBigInteger(RandomPrime.generate(512, r));
		
		
		//create q
		MyBigInteger q = new MyBigInteger(RandomPrime.generate(512, r));
		
		
		//calculate n as p*q
		MyBigInteger n = p.multiply(q);
		
		//calculate phi as (p-1)*(q-1)
		p = p.subtract(1);
		q = q.subtract(1);
		MyBigInteger phi = p.multiply(q);
	
		//choose e so 1 < e < phi(n) AND gcd(e, phi(n)) = 1
		MyBigInteger e = new MyBigInteger(512, 2);		//start at 2
		MyBigInteger seven = new MyBigInteger(512, 7);
		//System.out.println("e: " + e.toString());
		//System.out.println("TEST gcd: " + e.gcd(phi).toString());
		
		while(e.compareTo(phi) == -1 && e.compareTo(seven) <= 0)
		{
			//System.out.println(e.toString());
			int[] modarr = e.gcd(phi).toIntArray();
			int mod = modarr[modarr.length-1];
			if(mod == 1)		//if there is a remainder, was not divided evenly, 
				break;
			else
			{
				if(e.compareTo(seven) == -1)
					e = e.add(1);	//increment e
				else
					break;
			}
		}
		
		//calculate d
		MyBigInteger d = phi.modInverse(e);
		
		try		//write results to files accordingly
		{
			PrintWriter publicfile = new PrintWriter("pubkey.rsa", "UTF-8");
			PrintWriter privatefile = new PrintWriter("privkey.rsa", "UTF-8");
			
			publicfile.println(e.byteArray[e.byteArray.length-1]); //write e
			publicfile.println(n); //write n
			publicfile.close();
			publicfile.close();
			
			privatefile.println(d);	//write d		UNCOMMENT WHEN D IS MADE
			privatefile.println(n);	//write new
			privatefile.close();
			privatefile.close();
			System.out.println("RSA Keypairs created and printed to files.");

		}
		catch(Exception exc)
		{
		  System.out.println("Could not write to file...Exiting now.");
		  System.exit(0);
		}
	}
	
}