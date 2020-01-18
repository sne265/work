import java.awt.Font;
import java.util.ArrayList;

public class RunArray 
{
	//class to keep track of runs and font information
	
	ArrayList<FontIndices> indexFontList = new ArrayList<FontIndices>();
	
	private int index = 0;
	
	public void addRun(int startIndex,int length,Font font)
	{
		indexFontList.add(new FontIndices(startIndex,length,font));
		index += length;
	}
	
	public void appendRun(int length,Font font)
	{
		indexFontList.add(new FontIndices(index,length,font));
		index += length;
	}
	
	public Font getFont(int index)
	{
		// checks the start index and end index of each saved run in the list
		// to return the font used by the character in that location
		for (FontIndices f : indexFontList)
		{
			if(f.getStartIndex() <= index && index < f.getEndIndex()) 
				return f.getFont();
		}
		
	   return null;
	
	}

}
