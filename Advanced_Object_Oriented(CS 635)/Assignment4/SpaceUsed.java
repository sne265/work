
public class SpaceUsed 
{
	/* Due to issues with TLAB(Thread Local Allocation Buffer), need to disable TLAB
	 * using -XX:-UseTLAB and run computations that use sizeofUtil class from command prompt.
	 * javac SpaceUsed.java
	 * java -XX:-UseTLAB SpaceUsed*/
	/*
	 * This class is used to display the space required by flyweight objects 
	 * and by non-flyweight objects, to store the same text document. */
	
	public static void main(String args[])
	{
		
		ComputeNonFlyweightSpace nonFlyweight = new ComputeNonFlyweightSpace();
		
		
		Double normalSpace = nonFlyweight.getSpaceUsed();
		
		System.out.println("\nAvg.Space used by sample text without flyweight : "+normalSpace+" bytes");
		
		ComputeFlyweightSpace flyweight = new ComputeFlyweightSpace();
		
		
		Double flyweightSpace = flyweight.getSpaceUsed();
		
		System.out.println("\nAvg.Space used by sample text with flyweight pattern : "+flyweightSpace+" bytes");
		
		System.out.println("\nSpace saved by FLYWEIGHT : " +(normalSpace - flyweightSpace)+" bytes");
	}
	

}
