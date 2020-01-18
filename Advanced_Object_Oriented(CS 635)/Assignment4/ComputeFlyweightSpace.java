import java.awt.Font;
import java.util.ArrayList;

public class ComputeFlyweightSpace extends SizeofUtil
{
	// class to compute the space used by sample text using flyweight pattern
	
	@Override
	protected int create() 
	{
		// save each character of the sample text using flyweight pattern
		
		CharacterFactory charfactory = CharacterFactory.getInstance();
	    FontFactory fontfactory = FontFactory.getInstance();
	    ArrayList<FlyweightCharacter> chArray = new ArrayList<>();
	    
	    String sampleText = getSampleText();
	    
	    for (int i=0;i<sampleText.length();i++)
			chArray.add( charfactory.getCharacter(sampleText.charAt(i)) );
	    	    	      
	    RunArray runArray = new RunArray();
	    Font fontA = fontfactory.getFont("Courier New", Font.PLAIN, 20);
	    Font fontB = fontfactory.getFont("Times New Roman", Font.BOLD, 14);
	    
	    runArray.addRun(0, 144, fontA);
	    runArray.appendRun(212, fontB);
	    	      
	    return 1 ;
	}
	   
	public double getSpaceUsed()
	{
		return averageBytes();
		//System.out.println("Space used by text with flyweight pattern : "+averageBytes());
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
