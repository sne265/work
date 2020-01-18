public class BinarySearchTree 
{
	private Node root;
	private OrderingStrategy strategy;
	
	BinarySearchTree(OrderingStrategy strategy)
	{
		// create empty root node
		root = new NullNode();
		this.strategy = strategy;
	}
	
	public boolean isEmpty()
	{
		return root.isNull();
	}
		
	public void insert(String input_string)
	{
		// call recursive function for insertion
		root = addNode(root,input_string);
	}
	
	private Node addNode(Node currentParent,String input) 
	{
		
		// if root/current node is null, create new node with input string and return
		if ( currentParent.isNull() ) 
	        return new RealNode(input);
		
		// lexical comparison of strings, strategy decides if the comparison is done 
		// using original strings or by using reverse of the original strings
		int compareString = this.strategy.compare(input,currentParent.getValue());
	    
		
		// if input key is less than the current node, recur for left subtree
	    if ( compareString < 0) 
	    	currentParent.setLeft(addNode(currentParent.getLeft(), input));
	    
	    
	    // input key is greater than the current node, recur for right subtree
	    else  if ( compareString > 0) 
	    	currentParent.setRight(addNode(currentParent.getRight(), input));
	   	    
	    // if input key already exists, do not insert, return
	    else
	        return currentParent;
	    
	    return currentParent;
	}
	public void accept(TreeVisitor visitor)
	{
		// start traversal from root node
		root.accept(visitor);
	}
}