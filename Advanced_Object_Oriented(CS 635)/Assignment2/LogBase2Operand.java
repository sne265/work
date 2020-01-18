
public class LogBase2Operand implements Operand 
{
	/**
	 * Class to perform log base 2 operation
	 */
	Operand lastOperand;
	
	public LogBase2Operand(Operand lastOperand) 
	{
		this.lastOperand = lastOperand;
				
	}
	
	@Override
	public Double evaluate(Context context) 
	{
		// compute log base 2
		return (Math.log(lastOperand.evaluate(context))/Math.log(2));
	}

}
