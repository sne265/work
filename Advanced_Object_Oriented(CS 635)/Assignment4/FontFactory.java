import java.awt.Font;
import java.util.HashMap;

public class FontFactory 
{
	// hash map to store font objects, key is font property(fontname-style-size)
	// and value is its corresponding font object
	private HashMap<String,Font> fontDetails = new HashMap<>(); 
	
	private static FontFactory fontFactoryInstance=null;
	
	private FontFactory()
	{
		
	}
	public static FontFactory getInstance() 
	{
		// FontFactory must have only single instance
        if (fontFactoryInstance == null)
        {
            fontFactoryInstance =  new FontFactory();
        }

        return fontFactoryInstance;
    }
	
	public Font getFont(String fontName,int style,int size)
	{
		// method returns the font object that has the fontname,style and size given in the parameters
		String key = fontName + "-" + style + "-" + size;
		
		if (fontDetails.containsKey(key))
			return fontDetails.get(key);
		
		Font font = new Font(fontName,style,size);			
		fontDetails.put(key, font);
		return font;
		
	}

}
