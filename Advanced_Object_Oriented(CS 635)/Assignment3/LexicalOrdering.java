
public class LexicalOrdering implements OrderingStrategy 
{
	// class to compare strings in their normal lexicographic ordering

	@Override
	public int compare(String inputString, String nodeKey) 
	{
		/* returns 0 if strings are equal,
		 * returns value <0 if inputString appears before nodeKey string in alphabetic order
		 * returns value >0 if inputString comes after nodeKey string in alphabetic order
		*/
		
		return inputString.compareToIgnoreCase(nodeKey);
	}
}