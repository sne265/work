import java.util.Stack;

public class EvaluatePostfixExpression 
{
	String expression;
	
	Stack<Operand> stack = new Stack<Operand>();
	public EvaluatePostfixExpression() {}
	
	public EvaluatePostfixExpression(String expression)
	{
		this.expression = expression;
			
	}
	
	public Double evaluate(Context context)
	{
		
		String[] tokenList = expression.split(" ");
		Operand operand,operator;
		
		
		for (String token : tokenList) 
		{
			if (context.isOperator(token))
			{
				// check if operator requires only single operand
				if(token.equals("sin") || token.equals("lg") )				
					operator=SingleOperand(token);				
				else			
					operator=DoubleOperand(token);
				
				Double result = operator.evaluate(context);
				stack.push(new NumberOperand(result));
				
			}
			else 
				if(token.contains("$")) 
				{
					// if token contains variable(column reference)		
					operand = new VariableOperand(token);
					stack.push(operand);
				}
				else
				{
					operand = new NumberOperand(token);
					stack.push(operand);
				}
		
		}
		return stack.pop().evaluate(context);	
	}
	
	public Operand SingleOperand(String token)
	{
		// get last pushed operand or value for sin and lg operation
		Operand lastOperand = stack.pop();
		
		return getOperatorInstance(token,lastOperand);	
		
	}
	
	public Operand DoubleOperand(String token)
	{
		Operand rightOperand = stack.pop();
		Operand leftOperand = stack.pop();
		
		return getOperatorInstance(token,leftOperand,rightOperand);
		
	}
	
	public Operand getOperatorInstance(String operator, Operand left,
			Operand right) 
	{
		switch (operator) 
		{
		case "+":
			return new PlusOperand(left, right);
		case "-":
			return new MinusOperand(left, right);
		case "*":
			return new MultiplyOperand(left, right);
		case "/":
			return new DivideOperand(left, right);
			
		}
		return null;
	}
	
	
	public Operand getOperatorInstance(String operator,Operand last)
	{
		switch (operator)
		{
		case "sin":
			return new SineOperand(last);
		case "lg":
			return new LogBase2Operand(last);
		}
		return null;
	}
}

