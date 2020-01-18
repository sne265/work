import java.util.Comparator;
public class SortByPercentageCPUTime implements Comparator<ProcessAttributes> 
{ 
    // Used for sorting in ascending order of percentage of CPU used 
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	//override compareTo method of Comparator to compare percentage of CPU 
    	//used by each process
    	if (pObject1.percentageOfCPU < pObject2.percentageOfCPU) return -1;
        if (pObject1.percentageOfCPU > pObject2.percentageOfCPU) return 1;
        return 0;
    	
    } 
} 
