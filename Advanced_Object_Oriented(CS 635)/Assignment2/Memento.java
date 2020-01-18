
public class Memento 
{
	String value;
	String equation;
	
	public Memento(CellValue cellObj)
	{
		this.value=cellObj.getCellValue();
		this.equation=cellObj.getCellEquation();
	}
}


