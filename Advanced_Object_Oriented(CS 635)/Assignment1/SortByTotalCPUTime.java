import java.util.Comparator;
public class SortByTotalCPUTime implements Comparator<ProcessAttributes> 
{ 
    // Used for sorting in ascending order of total CPU time 
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	
    	//override compareTo method of Comparator to compare totalCPUTime 
    	//used in the process
        return pObject1.totalCPUTime.compareTo(pObject2.totalCPUTime);
    } 
} 
