public class LinkedList
{
	public Node head;		//head of DLB linked list
	public int size;
	
	public LinkedList(char c)
	{
		Node newNode = new Node(c);
		head = newNode;
		size++;
	}
	public LinkedList(String s)
	{
		Node newNode = new Node(s);
		head = newNode;
		size++;
	}
}