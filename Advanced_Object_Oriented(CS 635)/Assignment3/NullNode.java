
public class NullNode extends Node 
{
	
	public NullNode()
	{
		// calls non-parameterized constructor of its super class
		super(); 
	}
	public boolean isNull()
	{
		return true;
	}
	@Override
	public void accept(TreeVisitor visitor) 
	{
		// calls visit method that has NullNode object as its parameter
		visitor.visit(this);
	}
	
}
