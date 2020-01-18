
public class PreOrderVisitor implements TreeVisitor
{
	String traversedNodes;
	
	public PreOrderVisitor()
	{
		traversedNodes=""; 
	}
	
	public void visit(RealNode node)
	{
		// Pre-order traversal of binary search tree
	    // sequence is formed in (node(left sub tree)(right subtree)) format.
		
		traversedNodes = traversedNodes.concat("("+node.getValue());
		node.getLeft().accept(this);
		node.getRight().accept(this);
		traversedNodes = traversedNodes.concat(")");
	
	}
	
	public void visit(NullNode node)
	{
		traversedNodes = traversedNodes.concat("()");
	}
	
	public String getTraversalSeq()
	{
		return traversedNodes;
	}
}