
abstract class Node 
{	
	private String key;
	private Node left,right;
	
	public abstract boolean isNull();
	
	public abstract void accept(TreeVisitor visitor);
	
	public Node()
	{
		key="";
		left = null;
		right = null;
	}
	public Node(String key)
	{
		this.key = key;
		left = new NullNode();
		right = new NullNode();
	}
	public Node getLeft() 
	{
		return left;
	}
	
	
	public Node getRight() 
	{
		return right;
	}
	
	
	public String getValue() 
	{
		return key;
	}
	
	public void setLeft(Node node) 
	{
		this.left = node ;
	}
	
	public void setRight(Node node) 
	{
		this.right = node;
	}

}