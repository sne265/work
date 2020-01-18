public class MultiplyOperand implements Operand 
{
	/**
	 * Class to perform multiplication operation
	 */
	Operand leftOperand;
	Operand rightOperand;
	
	public MultiplyOperand(Operand leftOperand,Operand rightOperand) 
	{
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Double evaluate(Context context) 
	{
		return leftOperand.evaluate(context) * rightOperand.evaluate(context);
	}
}