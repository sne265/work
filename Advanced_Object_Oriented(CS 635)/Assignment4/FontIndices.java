import java.awt.Font;

public class FontIndices 
{
	// data class to store start and end indices of run
	// and font information, for RunArray class
	
	private int start;
	private int length;
	private Font font;
	
	public FontIndices(int start,int length,Font font)
	{
		this.start = start;
		this.length = length;
		this.font = font;
	}

	public int getStartIndex()
	{
		return this.start;
	}
	
	public int getEndIndex()
	{
		return this.start + this.length;
	}
	
	public int getLength()
	{
		return this.length;
	}
	
	public Font getFont()
	{
		return this.font;
	}
	
}