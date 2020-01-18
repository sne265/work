
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CellDataModel 
{
	CellValue cellObj = new CellValue();
	
	// hash map to store column number and its corresponding value,equation
	HashMap<Integer,CellValue> data=new HashMap<>();
	
	
	private ArrayList<Observable> subjectList = new ArrayList<>();
	
	CellCaretaker caretaker = new CellCaretaker();
	
	private Double result;
	private int colCount;
	private String colNames[];
	private Context context;
	private int col;
	
	public CellDataModel(SpreadsheetTableModel model)
	{
		colCount = model.getColumnCount();
		
		colNames = new String[colCount];
		
		context = new Context();

		for (int colIndex = 0 ; colIndex < colCount ; colIndex++)
		{
			data.put(colIndex, new CellValue());//)
			colNames[colIndex] = model.getColumnName(colIndex);
			context.setVariableValue(colNames[colIndex],0.0);
		}
					
	}
	
	public void setValue(String newInputString,int row,int col)
	{
		this.col = col;
			
		cellObj.setCellValue(newInputString);
		
		if(!(cellObj.getCellEquation().isEmpty()))
			cellObj.setCellEquation("");
		
		data.put(col,cellObj);
		
		context.setVariableValue(colNames[col],(Double.parseDouble(newInputString)));
		
		cellObj.setContext(context);
		
		cellObj.dataChanged(cellObj);
		
		caretaker.save(this);
		
	}
	
	public void setEquation(String newInputString,int row,int col)
	{
		this.col = col;
		
		cellObj.setCellEquation(newInputString);
		
		result=cellObj.evaluateEquation(newInputString,context);
		
		cellObj.setCellValue(result.toString());
		
		data.put(col,cellObj);
		
		context.setVariableValue(colNames[col],result);
		
		caretaker.save(this);
		
		findObservers(newInputString,col);
		
	}
	
	public Object getValue(int row,int col)
	{
		cellObj=data.get(col); // get cell object for the corresponding column
		
		return cellObj.getCellValue();
		
	}
	
	public Object getEquation(int row,int col)
	{
		
		cellObj=data.get(col); // get cell object for the corresponding column
		
		return cellObj.getCellEquation();
		
	}
	
	public boolean isValidEquation(String expression)
	{
		return context.isValidEquation(expression);
	}
	
	public void findObservers(String expression,int col)
	{
		/**
		 * function to find observers when equation is set for a cell
		 */
		String[] tokenSet = expression.split(" ");
		
		for (String token : tokenSet)
		{
			//add cells to be observed from the equation
			if(token.startsWith("$"))
			{
				int cellIndex = Arrays.asList(colNames).indexOf(token);
				
				CellValue subject = data.get(cellIndex);
				
				subjectList.add(subject);
			}
				
		}
		cellObj.addObservers(this.subjectList);
	
	}
	
	// to save state of current cell object
	public Memento save()
	{
		return new Memento(cellObj);
	}
	
	public void undoToLastSave(Object obj)
	{
		Memento memento = (Memento) obj;
		
		data.get(col).setCellValue(memento.value);
		data.get(col).setCellEquation(memento.equation);
	}
	
	
	public void undoChanges() 
	{
		/**
		 * Function to be called when undo button is clicked
		 */
		
		caretaker.undo(this);
		if(!cellObj.getCellValue().isEmpty())
			context.setVariableValue(colNames[col],Double.parseDouble(cellObj.getCellValue()));
		data.put(col,cellObj);
				
	}
}
