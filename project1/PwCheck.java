//Collin Dreher
import java.util.*;
import java.io.*;

public class PwCheck
{
	static char[] pass_chars = {'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
							'0', '2', '3', '5', '6', '7', '8', '9', '!', '@', '$', '^', '_', '*'};				//CANT use a, i, 1, or 4 -- since a and i fall in list of bad words
	static boolean validPassword, continueSearching;
	static PrintWriter writer;
	static long startTime, endTime;
	static StringBuilder prefix = new StringBuilder();
	static StringBuilder out = new StringBuilder("");
	
	public static void main(String args[]) throws IOException
	{
		DLB newDLB = new DLB();
		FileInputStream f = new FileInputStream("dictionary.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(f));
		String line, passwordsLine, time;
		String str = "";
		
	
		while((line=br.readLine()) != null)
		{
			if(line.equals("I"))
			{
				newDLB.add("i");
			}
			else
			{
				newDLB.add(line);
			}
		}
		br.close();
		
		
		if(args[0].equals("-find"))
		{
			writer = new PrintWriter(new File("all_passwords.txt"));
			startTime = System.nanoTime();
			generatePasswords(str, 5);
			writer.close();
		}
		if(args[0].equals("-check"))
		{
			FileInputStream f2 = new FileInputStream("all_passwords.txt");
			BufferedReader br2 = new BufferedReader(new InputStreamReader(f2));
			Scanner keyboard = new Scanner(System.in);
			DLB STDLB = new DLB();
			while((passwordsLine=br2.readLine()) != null)
			{
				String[] fileStrings = passwordsLine.split(",");
				prefix = new StringBuilder("");
				STDLB.addST(fileStrings[0], fileStrings[1]);
			}
			br2.close();
			
			String input = "";
			while(!input.equals("stop"))
			{
				System.out.println("Please enter a password to check, enter \"stop\" when finished:");
				input = keyboard.nextLine().toLowerCase();
				if(input.equals("stop"))
					break;
				Node curr = STDLB.root.head;
				prefix = new StringBuilder("");
				
				time = checkPasswords(input, 0, curr);
				//System.out.println("TIME: " + time);
				if(time == "" || time == null)
				{
					//System.out.println("PREFIX: " + prefix.toString());
					if(prefix.toString().equals(""))
						System.out.println("Sorry, you entered a password with no similar alt. passwords.");
					else
					{
						System.out.println("Sorry! Password invalid! Here are ten other passwords similar to the one entered, and their runtimes in seconds:");
						getAltPasswords(out, prefix, 0, STDLB.root.head, 10);
						/* System.out.println("1) " + alt.substring(0,5) + ", which took" + alt.substring(5) + " seconds to crack");
						int valuesToPrint = 9; */
						
					}
				}
				else
				{
					System.out.println("Valid password! Time to crack password was" + time + " seconds!");
				}
			}
		}
	}
	
	public static String checkPasswords(String userInput, int index, Node node)
	{
		String t = "";
		
		
			//System.out.print(index);
			//System.out.println(node.data);
			if(index >= userInput.length())
			{
				t = node.timeValue;
				//System.out.println("TEST: " + node.timeValue);
				return t;
			}
			if(userInput.charAt(index) == node.data)
			{
				prefix.append(userInput.charAt(index));
				//System.out.println(prefix.toString());
				node = node.child;
				//System.out.println(node.data);
				t = checkPasswords(userInput, index+1, node);
			}
			else if(node.nextNode != null)
			{
				node = node.nextNode;
				t = checkPasswords(userInput, index, node); 
			}
		
		return t;
	}
	
	public static void getAltPasswords(StringBuilder output, StringBuilder prefix, int i, Node node, int valuesToPrint)
	{
		
		/* if(i == 5)
		{
			System.out.print(node.timeValue);
			//System.out.println("1) " + output.toString().substring(0,5) + ", which took" + output.toString().substring(5) + " seconds to crack");
			//System.out.print(" BREAK ");
			return;
		} */
		if(i >= prefix.length())
		{
			boolean moveNode = false;
			boolean nodeHasBeenMoved = false;
			char prev = node.data;
			valuesToPrint = printValue(node, valuesToPrint);
			
			while(valuesToPrint > 0)
			{
				if(moveNode)
				{
					node=node.nextNode;
					nodeHasBeenMoved = true;
					//System.out.print("test1");
				}
				if(node.nextNode != null && moveNode)		
				{
					System.out.print(prefix);
					System.out.print(prev);
					//System.out.print("test2");
					node = node.nextNode;
					moveNode = false;
					valuesToPrint = printValue(node, valuesToPrint);
				}
				else if(node.nextNode != null)		
				{
					System.out.print(prefix);
					if(nodeHasBeenMoved)
						System.out.print(prev);
					node = node.nextNode;
					//System.out.print("test3");
					valuesToPrint = printValue(node, valuesToPrint);
				}
				else
				{
					System.out.print(prefix);
					prev = node.data;
					System.out.print(prev);
					//System.out.print("test4");
					node = node.child;
					//System.out.println("TEST"+node.data);
					if(node.nextNode == null)
					{
						node = node.child;
					}
					moveNode = true;
					valuesToPrint = printValue(node.nextNode, valuesToPrint);
				}
			}
			//System.out.print(node.data + " " + valuesToPrint); 
			return;
			
		}
		else if(prefix.charAt(i) == node.data)
		{
			System.out.print(node.data);
			node = node.child;
			getAltPasswords(output, prefix, i+1, node, valuesToPrint);
		}
		else if(node.nextNode != null)
		{
			node = node.nextNode;
			getAltPasswords(output, prefix, i, node, valuesToPrint);
		}
		//System.out.print(output.toString());
		return;
	}
	
	public static int printValue(Node node, int valuesToPrint)
	{
		System.out.print(node.data);
		/* if(node.child.child == null && !pathReached)		//false when on fourth letter in sequence
		{
			System.out.print(node.child.data);			//fifth letter
			System.out.println(node.child.timeValue); 		//runtime
			valuesToPrint--;
			pathReached = true;
		} */
		if(node.child.child == null)
		{
			System.out.print(node.child.data);			//fifth letter
			System.out.println(node.child.timeValue); 		//runtime
			valuesToPrint--;
			
		}
		else
		{
			node = node.child;
			valuesToPrint = printValue(node, valuesToPrint);
		}
		return valuesToPrint;
	}
	
	public static void generatePasswords(String s, int passwordLength) 
	{
		
		//startTime = System.nanoTime();
		if(passwordLength == 0) 
		{				
			test(s);		
			return;
		}
		for(int i = 0; i < pass_chars.length; i++) 
		{			
			String password = s + pass_chars[i];		
			generatePasswords(password, passwordLength-1);
		}
	}
	
	public static Node searchDLB(String pass, int index, Node node) 
	{
		if(index >= pass.length())
		{
			validPassword = false;
		}
		else if(node.data == '*') 
		{		
			validPassword = false;
		}
		else if(pass.charAt(index) == node.data) 
		{			
			node = node.child;
			if(node.data == '*')
				validPassword = false;
			else
				node = searchDLB(pass, index+1, node);	
		}
		else if(node.nextNode != null) 
		{	
			//System.out.println("currentNode is: " + node.data);
			node = node.nextNode;
			//System.out.println("nextNode is: " + node.data);
			node = searchDLB(pass, index, node);			
		}
		else 
		{				
			continueSearching = false;
			node = DLB.root.head;			//no illegal word found, go back to root of DLB
			
		}
		
		return node;
	}
	
	public static void test(String s) 
	{
		int lowerCaseCounter = 0;
		int numberCounter = 0;
		int specialCharactersCounter = 0;			
		
		for(int i = 0; i < s.length(); i++) 
		{	
			if(Character.isDigit(s.charAt(i))) 							//Character.isDigit is in Java Library...checks if character is a number
			{
				numberCounter++;
			}
			else if(Character.isLetter(s.charAt(i)))					//Character.isLetter is in Java Library...checks if character is a letter
			{
				lowerCaseCounter++;
			}
			else 
			{				
				specialCharactersCounter++;
			}
		}
			
		if(lowerCaseCounter < 1 || lowerCaseCounter > 3 ) { return; }
		if(numberCounter < 1 || numberCounter > 2) { return; } 
		if(specialCharactersCounter < 1 || specialCharactersCounter > 2) { return; }
		
		String new_password = new String(s);
		/* System.out.println("S: " + s.toString());
		System.out.println("NEW: " + new_password.toString()); */
		validPassword = true;					
		
		while(new_password.length() > 2 && validPassword)
		{		
			Node currentNode = DLB.root.head;		//place node spot back at root of DLB	
			//System.out.println("currentNode is: " + currentNode.data);
			continueSearching = true;				
			
			for(int i = 0; i < new_password.length(); i++) 
			{
				//System.out.println("currentNode is: " + currentNode.data);
				//currentNode = searchDLB(new_password.charAt(i), currentNode);	
				currentNode = searchDLB(new_password, i, currentNode);	
				
				if(validPassword == false) 
				{		
					return;
				}
				/* if(continueSearching == false) 
				{		
					//currentNode = DLB.root.head;
					break;
				}   */
			}
			
			new_password = new_password.substring(1);		
		}
		
		writer.print(s);	
		if(s.length() == 5)
		{
			endTime = System.nanoTime();
			double time = (double)(endTime - startTime) / 1000000000.0;			//convert time to seconds
			writer.println(", " + time);
		}
		return;
	}
	
}