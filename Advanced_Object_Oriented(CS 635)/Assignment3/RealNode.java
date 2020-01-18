
public class RealNode extends Node 
{
	
	public RealNode(String key)
	{
		// calls parameterized constructor of its super class 
		super(key);  
	}
	
	public boolean isNull()	
	{
		return false;
	}
	
	@Override
	public void accept(TreeVisitor visitor) 
	{
		// calls visit method that has RealNode object as its parameter
		visitor.visit(this);
	}
}