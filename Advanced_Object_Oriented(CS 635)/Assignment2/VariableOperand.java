class VariableOperand implements Operand 
{
	/**
	 * Class to get variable value to evaluate expression
	 * Context class object is used to get value of the particular variable(column)
	 */
    private String name;
    

    public VariableOperand(String name) {
        this.name = name;
    }

    
    public Double evaluate(Context context) {
        return context.getVariableValue(name);
    }
}