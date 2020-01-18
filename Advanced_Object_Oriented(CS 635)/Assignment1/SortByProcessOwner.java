import java.util.Comparator;
public class SortByProcessOwner implements Comparator<ProcessAttributes> 
{ 
    // class used to get objects sorted by process owner in lexicographic order 
    public int compare(ProcessAttributes pObject1, ProcessAttributes pObject2) 
    { 
    	
    	//override compareTo method of Comparator to compare process owner
        return pObject1.owner.compareTo(pObject2.owner);
    } 
} 
