public class NumberOperand implements Operand {
	/**
	 * TerminalExpression Class of interpreter pattern
	 * that returns the value of leaf nodes
	 */
	Double number;

	public NumberOperand(int i) {
		number = new Double(i);
	}
	public NumberOperand(Double i) {
		number = i;
	}

	public NumberOperand(String s) 
	{
		number = Double.parseDouble(s);
	}

	@Override
	public Double evaluate(Context context) {
		return number;
	}

}