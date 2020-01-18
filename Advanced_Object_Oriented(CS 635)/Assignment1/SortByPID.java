import java.util.Comparator;
public class SortByPID implements Comparator<ProcessAttributes> 
{ 
    // Used for sorting in ascending order of PIDs
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	//override compareTo method of Comparator to compare process PID
    	return pObject1.pid - pObject2.pid;
    } 
} 
