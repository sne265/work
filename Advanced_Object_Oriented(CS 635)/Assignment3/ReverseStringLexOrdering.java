
public class ReverseStringLexOrdering implements OrderingStrategy 
{
	
	// class to compare strings in their normal lexicographic ordering after the
	// strings are reversed
	
	@Override
	public int compare(String inputString, String nodeKey) 
	{
		
		inputString = new StringBuilder(inputString).reverse().toString();	
		nodeKey = new StringBuilder(nodeKey).reverse().toString();
		
		/* returns 0 if reversed strings are equal,
		 * returns value <0 if reversed inputString appears 
		   before reversed nodeKey string in alphabetic order
		 * returns value >0 if reversed inputString comes 
		   after reversed nodeKey string in alphabetic order
		*/
		
		return inputString.compareToIgnoreCase(nodeKey);
	}
}