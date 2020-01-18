
// interface used by State pattern classes
public interface State 
{
	public Object getData(int row,int column,CellDataModel cModel);
	public void setData(String val,int row,int col,CellDataModel cModel);

}
