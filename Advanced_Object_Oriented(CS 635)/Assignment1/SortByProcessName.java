import java.util.Comparator;
public class SortByProcessName implements Comparator<ProcessAttributes> 
{ 
	// class used to get objects sorted by process name in lexicographic order 
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	//override compareTo method of Comparator to compare process names
        return pObject1.name.compareTo(pObject2.name);
    } 
} 
