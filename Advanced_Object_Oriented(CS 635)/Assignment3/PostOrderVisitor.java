
public class PostOrderVisitor implements TreeVisitor
{
	String traversedNodes; 
	
	public PostOrderVisitor()
	{
		traversedNodes=""; 
	}
	
	public void visit(RealNode node)
	{
		// In-order traversal of binary search tree
		// sequence is formed in ((left sub tree)(right subtree)node) format.
		
		traversedNodes = traversedNodes.concat("(");
		node.getLeft().accept(this);
		node.getRight().accept(this);
		traversedNodes = traversedNodes.concat(node.getValue());
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