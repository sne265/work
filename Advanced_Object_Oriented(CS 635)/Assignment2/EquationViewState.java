

public class EquationViewState implements State
{
	/**
	 * Class defines the operation when data is entered into cell or fetched
	 * from model when in spreadsheet is in equation view
	 */
	EquationViewState()
	{}
	
	@Override
	public void setData(String equation,int row,int col,CellDataModel cModel) 
	{
		if (cModel.isValidEquation(equation))
		{
			cModel.setEquation(equation,row,col);
			
		}
		else
			throw new IllegalArgumentException("Equation Syntax Error");
	}
	
	@Override
	public Object getData(int row,int col,CellDataModel cModel) 
	{
		return cModel.getEquation(row, col);
	}
 
}
