import java.util.Comparator;
public class SortByNumberOfThreads implements Comparator<ProcessAttributes> 
{ 
    // Used for sorting in ascending order of Number of Threads
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	//override compareTo method of Comparator to compare numberOfThreads 
    	//in the process
    	return pObject1.numberOfThreads - pObject2.numberOfThreads;
    } 
} 

