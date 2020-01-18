
public interface TreeVisitor 
{
	public void visit(RealNode node);
	public void visit(NullNode node);
	public String getTraversalSeq();
	
}
