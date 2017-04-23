//Collin Dreher
import java.util.*;
import java.io.*;

public class DLB
{
	private int LLCount;
	static boolean isEmpty;
	static LinkedList root; 		//root of DLB
	static int size;				//size of LinkedList
	static final char END_WORD_CHARACTER = '*';
	
	public DLB()		//empty DLB constructor
	{
		isEmpty = true;
		LLCount = 0;
		size = 0;
	}
	
	/* public DLB(String s)
	{
		this.isEmpty = true;
		this.LLCount = 0;
		this.add(s);
		this.size = 1;
	} */
	
	public static boolean add(String s)		//add word into DLB correctly
	{
		size++;
		boolean decreaseTemp = false;
		
		if(isEmpty == true)		//if first word
		{
			LinkedList previous = new LinkedList(s.charAt(0));
			root = previous;		//make first letter root node
			////System.out.print(root.head.data);
			for(int i = 1; i < s.length(); i++)
			{
				LinkedList curr = new LinkedList(s.charAt(i));		//create new DLB list for current word
				previous.head.child = curr.head;		//make current letter child of previous letter
				previous = curr;
				////System.out.print(previous.head.data);
			}
			
			LinkedList end_character = new LinkedList(END_WORD_CHARACTER);
			previous.head.child = end_character.head;			//add end character to end of word
			
			isEmpty = false;
			return true;
		}
		else
		{
			Node currentNode = root.head;
			int temp = 0;
			boolean addOnWordBranch = false;
			
			for(int i = 0; i < s.length(); i++)
			{
				while(currentNode.nextNode != null && currentNode.data != s.charAt(i))
				{
					currentNode = currentNode.nextNode;
				}
				if(currentNode.data == s.charAt(i))
				{
					temp=i+1;
					addOnWordBranch = true;
					////System.out.print(currentNode.data);
				}	
					
				else		//nextNode == null
				{
					currentNode.nextNode = new Node(s.charAt(i));
					currentNode = currentNode.nextNode;
					////System.out.print(currentNode.data);
					temp = i+1;		//temp int to show place in current word
				}
				break; 			//break from for loop, to build rest of word
			}
			
			while(temp < s.length() && addOnWordBranch == true)			//building new branch (word)
			{
				currentNode = currentNode.child;
				while(temp < s.length())
				{
					if(currentNode.data == s.charAt(temp))
					{
						currentNode = currentNode.child;
						temp++;
						decreaseTemp = true;
					}
					else
						break;
					
				}
				if(decreaseTemp)
					temp--;
				LinkedList newList = new LinkedList(s.charAt(temp));
				while(currentNode.nextNode != null)
					currentNode = currentNode.nextNode;
				currentNode.nextNode = newList.head;
				currentNode = currentNode.nextNode;
				////System.out.print(currentNode.data);
				temp++;
				addOnWordBranch = false;
			}
			
			while(temp < s.length())			//build rest of letters as children		
			{
				LinkedList newList = new LinkedList(s.charAt(temp));
				currentNode.child = newList.head;
				currentNode = currentNode.child;
				////System.out.print(currentNode.data);
				temp++;
			}
			if(addOnWordBranch)		//case for one letter words
			{
				currentNode = currentNode.child;
				LinkedList end_character = new LinkedList(END_WORD_CHARACTER);
				currentNode.nextNode = end_character.head;			//add end character
				currentNode = currentNode.nextNode;
				////System.out.print(currentNode.data);
			}
			else
			{
				LinkedList end_character = new LinkedList(END_WORD_CHARACTER);
				currentNode.child = end_character.head;			//add end character to end of word
				currentNode = currentNode.child;
				////System.out.print(currentNode.data);
			}
			return true;
		}
	}
	
	public static boolean addST(String s, String timeString)
	{
		size++;

		if(isEmpty)		//if first word
		{
			////System.out.print(timeString);
			LinkedList previous = new LinkedList(s.charAt(0));
			root = previous;		//make first letter root node
			//System.out.print(root.head.data);
			for(int i = 1; i < s.length(); i++)
			{
				LinkedList curr = new LinkedList(s.charAt(i));		//create new DLB list for current word
				previous.head.child = curr.head;		//make current letter child of previous letter
				previous = curr;
				//System.out.print(previous.head.data);
			}
			
			////System.out.println(timeString);
			LinkedList time = new LinkedList(timeString);
			previous.head.child = time.head;			//add end character to end of word
			//System.out.println(previous.head.child.timeValue);
			isEmpty = false;
			return true;
		}
		else
		{
			Node currentNode = root.head;
			int temp = 0;
			boolean addOnWordBranch = false;
			
			for(int i = 0; i < s.length(); i++)
			{
				while(currentNode.nextNode != null && currentNode.data != s.charAt(i))
				{
					currentNode = currentNode.nextNode;
				}
				if(currentNode.data == s.charAt(i))
				{
					temp=i+1;
					addOnWordBranch = true;
					//System.out.print(currentNode.data);
				}	
					
				else		//nextNode == null
				{
					currentNode.nextNode = new Node(s.charAt(i));
					currentNode = currentNode.nextNode;
					//System.out.print(currentNode.data);
					temp = i+1;		//temp int to show place in current word
				}
				break; 			//break from for loop, to build rest of word
			}
			
			while(temp < s.length() && addOnWordBranch == true)			//building new branch (word)
			{
				currentNode = currentNode.child;
				while(temp < s.length())
				{
					if(currentNode.data == s.charAt(temp))
					{
						currentNode = currentNode.child;
						temp++;
						
					}
					else
						break;
					
				}
				
				while(currentNode.nextNode != null)
				{
					currentNode = currentNode.nextNode;
					while(currentNode.data == s.charAt(temp))
					{
						temp++;
						currentNode = currentNode.child;
					}
					
				}
				LinkedList newList = new LinkedList(s.charAt(temp));
				currentNode.nextNode = newList.head;
				currentNode = currentNode.nextNode;
				//System.out.print(currentNode.data);
				temp++;
				addOnWordBranch = false;
			}
			
			while(temp < s.length())			//build rest of letters as children		
			{
				LinkedList newList = new LinkedList(s.charAt(temp));
				currentNode.child = newList.head;
				currentNode = currentNode.child;
				//System.out.print(currentNode.data);
				temp++;
			}
			if(addOnWordBranch)		//case for one letter words
			{
				currentNode = currentNode.child;
				LinkedList time = new LinkedList(timeString);
				currentNode.nextNode = time.head;			//add end character
				currentNode = currentNode.nextNode;
				//System.out.println(currentNode.timeValue);
			}
			else
			{
				LinkedList time = new LinkedList(timeString);
				currentNode.child = time.head;			//add end character to end of word
				currentNode = currentNode.child;
				//System.out.println(currentNode.timeValue);
			}
			return true;
		}
	}
	
	
}