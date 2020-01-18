
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Font;
import org.junit.jupiter.api.Test;

class FlyweightPatternTests 
{
	
	@Test
	public void testFlyweightCharacter() 
	{
		FlyweightCharacter characterA = new FlyweightCharacter('A');
		int unicodeA = characterA.getUnicode();
		assertEquals('A',(char)unicodeA);
		
		FlyweightCharacter characterB = new FlyweightCharacter('B');
		int unicodeB = characterB.getUnicode();
		
		assertEquals('B',(char)unicodeB);
		assertNotEquals(unicodeA,unicodeB);
		
	}
	
	@Test
	public void testSingleInstanceCharacterFactory()
	{
		CharacterFactory charFactory1 = CharacterFactory.getInstance();
		CharacterFactory charFactory2 = CharacterFactory.getInstance();
		
		assertSame(charFactory1,charFactory2);	
		assertEquals(charFactory1,charFactory2);
	}
	
	@Test
	public void testCharacterFactory()
	{
		CharacterFactory charFactory = CharacterFactory.getInstance();
		
		//new character object for 'a' is created
		FlyweightCharacter charA1 = charFactory.getCharacter('a'); 
		
		// no new character object is created, same character object has to be used
		FlyweightCharacter charA2 = charFactory.getCharacter('a');
		
		// new character object for 'b' is created
		FlyweightCharacter charB = charFactory.getCharacter('b'); 
		
		assertEquals(charA1.hashCode(),charA2.hashCode());
		
		assertNotEquals(charA1.hashCode(),charB.hashCode());
	}
	
	@Test
	public void testSingleInstanceFontFactory()
	{
		FontFactory fontFactory1 = FontFactory.getInstance();
		FontFactory fontFactory2 = FontFactory.getInstance();
		
		assertSame(fontFactory1,fontFactory2);	
		assertEquals(fontFactory1,fontFactory2);
	}
	
	@Test
	public void testFontFactory() 
	{
		FontFactory fontFactory = FontFactory.getInstance();
		
		// new font object is created
		Font font1 =fontFactory.getFont("Comic Sans", Font.ITALIC, 16);
		
		// same font object is returned since font properties has not changed
		Font font2 =fontFactory.getFont("Comic Sans", Font.ITALIC, 16);
		
		//new font object is created because font size has changed
		Font font3 =fontFactory.getFont("Comic Sans", Font.ITALIC, 18);
		
		assertEquals(font1.hashCode(),font2.hashCode());		
		assertNotEquals(font1.hashCode(),font3.hashCode());
		
	}
	
	@Test
	public void testRunArray()
	{
		
		Font font1 =new Font("Comic Sans", Font.ITALIC, 16);
		
		Font font2 =new Font("Monotype Corsiva", Font.BOLD, 16);
		
		RunArray run = new RunArray();
		// 10 characters starting from 0 i.e. 0 to 9 in font1
		run.addRun(0, 10, font1);
		// 5 characters starting from 10 i.e. 10 to 14 in font2
		run.addRun(10, 5, font2);
		
		Font testFont0 = run.getFont(0);
		assertEquals(font1,testFont0);
		
		Font testFont5 = run.getFont(5);
		assertEquals(font1,testFont5);
		
		Font testFont10 = run.getFont(10);
		assertEquals(font2,testFont10);
		assertNotEquals(font1,testFont10);
		
		Font testFont15 = run.getFont(15);
		assertNull(testFont15);
		
	}
}