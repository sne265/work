import java.awt.Font;
import java.util.ArrayList;

public class ComputeNonFlyweightSpace extends SizeofUtil
{
	// class to compute the space used by sample text without using flyweight pattern
	
	@Override
	protected int create() 
	{
		// store each character of the sample text as non flyweight objects
		// i.e. an object array of size equal to the number of characters in the text
		
		ArrayList<NonFlyweightCharacter> chArray = new ArrayList<>();
		
		String sampleText = getSampleText();
		Font fontA = new Font("Times New Roman",Font.BOLD,36);
		
		Font fontB = new Font("Courier New", Font.PLAIN, 20);
		
	    for (int i=0;i<144;i++)
			chArray.add( new NonFlyweightCharacter(sampleText.charAt(i),fontA) );
	    
	    for (int i=144;i<sampleText.length();i++)
			chArray.add( new NonFlyweightCharacter(sampleText.charAt(i),fontB) );
	    
		return 1;
	}
	
	public double getSpaceUsed()
	{
		return averageBytes();
		//System.out.println("Space used by text without flyweight : "+averageBytes());
	}
	
	public String getSampleText()
	{
		String text = "CS 635 Advanced Object-Oriented Design & Programming "
						+ "Fall Semester, 2018 "
						+ "Doc 17 Mediator, Flyweight, Facade, Demeter, Active Object "
						+ "Nov 19, 2019 "
						+ "Copyright ©, All rights reserved. 2019 SDSU & Roger Whitney, "
						+ "5500 Campanile Drive, San Diego, CA 92182-7700 USA. "
						+ "OpenContent (http://www.opencontent.org/opl.shtml) "
						+ "license defines the copyright on this document.";
		
		return text;
	}
	
	

}
