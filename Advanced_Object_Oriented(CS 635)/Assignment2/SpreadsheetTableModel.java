
import javax.swing.table.AbstractTableModel;

public class SpreadsheetTableModel extends AbstractTableModel {

	/**
	 * Custom table model for the Spreadsheet
	 * Invokes getValueAt and setValueAt automatically 
	 * when cell is edited
	 */
	private static final long serialVersionUID = 1L;
	
	protected String[] columnNames={"$A","$B","$C","$D","$E","$F","$G","$H","$I"};
	int numRows=1;
	
	State state;
	StateContext stateObj;
	CellDataModel cellModel;
	
	public SpreadsheetTableModel()
	{
		stateObj=new StateContext();
		cellModel = new CellDataModel(this);
		
	}
	
	public void actionValueSelected()
	{
		// set state when value button is clicked
		stateObj.setState(new ValueViewState());
		getCurrentState();
	}
	
	public void actionEquationSelected()
	{
		// set state when value button is clicked
		stateObj.setState(new EquationViewState());
		getCurrentState();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	//@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return numRows;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	public void getCurrentState() 
	{
		state = stateObj.getState();
	}

	@Override
	public void setValueAt(Object newValue, int row, int column) 
	{
		getCurrentState();
		state.setData(newValue.toString(),row, column,cellModel);
	}
	
	@Override
	public Object getValueAt(int row, int column) 
	{
		getCurrentState();
		
		Object ob=state.getData(row,column,cellModel);
		
		return ob;
		
	}

	public void undoSelected() 
	{
		// function to call when undo button is clicked
		cellModel.undoChanges();	
	}  
    
}
