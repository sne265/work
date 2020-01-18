
public class StateContext 
{
	/**
	 * Class to handle changes in state  
	 * when view changes in the spreadsheet
	 */
	private State currentState;
	
	public StateContext()
	{
		currentState = new ValueViewState();
	}
	public void setState(State state) 
	 {
	        currentState = state;
	 }
	
	public State getState()
	 {
	        return currentState;
	 }
	
}
