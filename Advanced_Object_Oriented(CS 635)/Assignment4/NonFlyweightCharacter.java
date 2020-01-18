import java.awt.Font;

public class NonFlyweightCharacter 
{
	//non flyweight class to store document by each character, along with its font information 
	
	char character;
	Font font;
	
	public NonFlyweightCharacter(char character, Font font)
	{
		 this.character = character;
		 this.font = font;
	}
	
	public char getCharacter()
	{
		return this.character;
	}
	
	public void setCharacter(char character)
	{
		this.character = character;
	}
	
	public Font getFont()
	{
		return this.font;
	}
	
	public void setFont(Font font)
	{
		this.font = font;
	}

}
