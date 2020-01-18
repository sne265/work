public class SineOperand implements Operand 
{
	/**
	 * Class to perform sine operation
	 */
	Operand lastOperand;
	
	public SineOperand(Operand lastOperand) 
	{
		this.lastOperand = lastOperand;
				
	}

	@Override
	public Double evaluate(Context context) {
		return (Math.sin(lastOperand.evaluate(context))) ;
	}

}
