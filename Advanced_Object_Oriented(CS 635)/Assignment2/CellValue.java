import java.util.ArrayList;

public class CellValue implements Observer,Observable
{
	/**
	 * Class that defines the cell attributes
	 * each cell holds a value and an equation
	 */
	protected String value;
	protected String equation;
	CellValue subject;
	Context context;
	private ArrayList<CellValue> observers = new ArrayList<>();
	
	public CellValue()
	{
		value="";
		equation="";
	}
	
	public void setContext(Context context)
	{
		// update context object to evaluate expression with updated values
		this.context = context;
	}
	
	public void setCellValue(String newValue)
	{
		value=newValue;
	}
	
	public String getCellValue() 
	{
		return value;
	}
	
	public void setCellEquation(String newValue)
	{
		equation=newValue;
	}
	
	public String getCellEquation() 
	{
		return equation;
	}
	
	public Double evaluateEquation(String newInputString,Context context)
	{
		/**
		 * function to evaluate the new equation entered in a cell
		 */
		EvaluatePostfixExpression exp = new EvaluatePostfixExpression(newInputString);
		
		Double result = exp.evaluate(context);
		
		return result;
		
	}
	
	public void addObservers(ArrayList<Observable> subjectList)
	{
		/**
		 * Function to add Observers for each subject 
		 * found after parsing the equation
		 */
		for(Observable subject : subjectList)
		{
			subject.addObserver(this);
		}
	}	
	
	public void removeObservers(ArrayList<Observable> subjectList)
	{
		/**
		 * Function to remove observers when equation of an 
		 * observer cell is changed to value
		 */
		for(Observable subject : subjectList)
		{
			subject.removeObserver(this);
		}
		
	}
	@Override
	public void update(CellValue observer) 
	{
		// update value of observers after evaluating the equation
		
		Double result = evaluateEquation(observer.getCellEquation(),context);
		
		observer.setCellValue(result.toString());
	}
	
	@Override
	public void addObserver(CellValue observer) {
        this.observers.add(observer);
    }
	
	@Override
    public void removeObserver(CellValue observer) {
        this.observers.remove(observer);
    }
	
	public void setObservable(CellValue subject) 
	{
		// helper function to notify a subject value has changed
		this.subject = subject;
		dataChanged(subject);
		notifyObservers();
		
	}
	
	public CellValue getObservable()
	{
		return subject;
		
	}
	
	public ArrayList<CellValue> getObservers()
	{
		// return set of observers for the current cell object
		return observers;
	}
	
    public void dataChanged(CellValue subject) 
    {
        this.subject=subject; //subject that has changed 
    }
        
   	@Override
	public void notifyObservers() 
   	{
		/*
		 * Function to notify all observers to update their values
		 */
   		for(CellValue observer:observers)
   		{
   			System.out.println("IN NOTIFYOBSERVERS:"+this.subject);
   			update(observer);
   		}
   	}
   		
}
