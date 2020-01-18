import java.util.HashMap;

public class Context {
	/**
	 *  This class acts as a context class to access variable values given the column name
	 */
	
	// this hash map columnValues holds mapping between variable name(the name
	// of the column) and the value that it contains
	private HashMap<String,Double> colNameValues; 
	
	
	public Context()
	{
		colNameValues = new HashMap<>();
	}
	
	public Double getVariableValue(String name)
	{
		return colNameValues.get(name);
	}
	
	public void setVariableValue(String name,Double value)
	{
		colNameValues.put(name,value);
	}
	
	public boolean containsKey(String key)
	{
		return colNameValues.containsKey(key);
	}
	
	public boolean isOperator(String operator)
	{
		if (operator.equals("+") || operator.equals("-") || operator.equals("*") || 
			operator.equals("/") || operator.equals("sin") || operator.equals("lg"))
			
			return true;
		else
			return false;
			
	}
	
	public boolean isValidEquation(String expression)
	{
		
		boolean validEq = true;
		boolean operatorExist = false;
		String[] tokens=expression.split(" ");
		int countTokens = tokens.length;
		
		for(String str : tokens)
		{
			
			// expression must contain only cell references,numbers and operators
			// check if expression contains invalid terms
			
			if( (!containsKey(str)) && (!isOperator(str))
					&& (!isNumeric(str)))
			
				validEq = false;
			
			// if there is more than one term in equation,
			// there should be at least one operator 
			
			if (countTokens > 1)
				 if(isOperator(str))
					 operatorExist=true;
		}
		
		if(operatorExist) 
			return validEq;
		else 
			return false;
		
	}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}		
	
}
