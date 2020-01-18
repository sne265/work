
public class FlyweightCharacter 
{
	// character class to store the unicode of a character
	int uniCode;
	
	public FlyweightCharacter(char character)
	{
		this.uniCode = (int)character;
	}
	
	public int getUnicode()
	{
		return this.uniCode;
	}
	 
	public char getCharacter()
	{
		return (char)uniCode;
		 
	}
}
