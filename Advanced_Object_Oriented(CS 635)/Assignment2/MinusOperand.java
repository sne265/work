public class MinusOperand implements Operand 
{
	/**
	 * Class to perform subtraction operation
	 */
	Operand leftOperand;
	Operand rightOperand;

	public MinusOperand(Operand leftOperand,Operand rightOperand) 
	{
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Double evaluate(Context context) 
	{
		return leftOperand.evaluate(context) - rightOperand.evaluate(context);
	}


}