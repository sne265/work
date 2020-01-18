import java.util.HashMap;

public class CharacterFactory 
{
	
	private static CharacterFactory charFactoryInstance=null;
	private HashMap<Character,FlyweightCharacter> characterMap = new HashMap<>();
	
	private CharacterFactory()
	{
		
	}
	 
	public static CharacterFactory getInstance() 
	{
		// to get single instance for character factory

        if (charFactoryInstance == null)
        {
        	charFactoryInstance =  new CharacterFactory();
        }

        return charFactoryInstance;
    }
	
	
	public FlyweightCharacter getCharacter(char c)
	{
		// returns the character object for the particular input character
		
		if (characterMap.containsKey(c))
			return characterMap.get(c);
		
		FlyweightCharacter character = new FlyweightCharacter(c);
		
		characterMap.put(c, character);
		
		return character;
		
	}

}
