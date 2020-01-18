public class PlusOperand implements Operand 
{
	/**
	 * Class to perform addition operation
	 */
	
	Operand leftOperand;
	Operand rightOperand;

	public PlusOperand(Operand leftOperand, Operand rightOperand) {
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Double evaluate(Context context) 
	{
		return leftOperand.evaluate(context) + rightOperand.evaluate(context);
	}

}
