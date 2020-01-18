public class DivideOperand implements Operand 
{
	/**
	 * Class to perform divide operation
	 */
	Operand leftOperand;
	Operand rightOperand;

	public DivideOperand(Operand leftOperand, Operand rightOperand) 
	{
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
	}

	@Override
	public Double evaluate(Context context) {
		return leftOperand.evaluate(context) / rightOperand.evaluate(context);
	}

}
