
public class InOrderVisitor implements TreeVisitor
{
	String traversedNodes; 
	
	public InOrderVisitor()
	{
		traversedNodes=""; 
	}
	
	public void visit(RealNode node)
	{
		// In-order traversal of binary search tree
		// sequence is formed in ((left sub tree)node(right subtree)) format.
		
		traversedNodes = traversedNodes.concat("(");
		node.getLeft().accept(this);
		traversedNodes = traversedNodes.concat(node.getValue());
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