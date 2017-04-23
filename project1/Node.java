public class Node
{
	//node fields
	public Node child;
	public Node nextNode;
	public char data;
	public String timeValue;
	
	public Node(char c)
	{
		data = c;
		child = null;
		nextNode = null;
	}
	public Node(String s)
	{
		timeValue = s;
		child = null;
		nextNode = null;
	}
}