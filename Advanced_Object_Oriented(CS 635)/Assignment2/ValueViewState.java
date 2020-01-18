

public class ValueViewState implements State
{
	/**
	 * Class defines the operation when data is entered into cell or fetched
	 * from model when in spreadsheet is in value view
	 */
	CellDataModel dataObj;
	public ValueViewState()
	{}
	
	@Override
	public void setData(String value,int row,int col,CellDataModel cModel) 
	{
		// throw exception if value entered has unwanted characters
		if(hasAlpha(value))
			throw new NumberFormatException();
		else
			cModel.setValue(value,row,col);
	}
	
	@Override
	public Object getData(int row,int col,CellDataModel cModel) 
	{
		return cModel.getValue(row,col);
	}

	public boolean hasAlpha(String str)
	{
		return (str.matches(".*[a-zA-Z]+.*")||(str.matches("[!@#$%^&(),?\":{}|<>]")));
	}


}
