/* class to quick sort process 
 * objects based on object attributes*/
import java.util.Comparator;

public class QuickSortObjects
{
	
	public ProcessAttributes[] quickSort(ProcessAttributes[] processObjects,
		 									int leftIndex,int rightIndex,
		 									Comparator<ProcessAttributes> comp)
	{
		
		int low = leftIndex;
		
        int high = rightIndex;

        if (high > low)
        {
        	// calculate pivot 
        	ProcessAttributes pivot = processObjects[(low + high)/2];
        	
            while (low <= high)
            {
            	 /*
                 * In each iteration, we will identify object attribute from left side which 
                 * is greater then the pivot value, and we will identify a object attribute 
                 * from right side which is less then the pivot value. Once the search 
                 * is done, then we exchange both attribute values.
                 */
               while(low < rightIndex && comp.compare( processObjects[low], pivot) < 0)
               {
                    low++;
               }
               
               while(high > leftIndex && comp.compare( processObjects[high], pivot) > 0)
               {
                    high--;
               }
               
                if (low <= high)
                {
                    swap( processObjects, low ,high);
                    low++;
                    high--;
                }
            }
            // repeat sorting for left and right partitions
            if (leftIndex < high)
            {
                quickSort(processObjects,leftIndex,high, comp);

            }
          
            if (low < rightIndex)
            {
                quickSort(processObjects, low, rightIndex, comp);
            }
        }
        /* return sorted array of process objects*/
        return processObjects;
    }
	
	private static void swap(ProcessAttributes[] a, int i, int j)
	{
		/* exchange process objects*/
		ProcessAttributes swapVariable = a[i];
		a[i] = a[j];
		a[j] = swapVariable;
		
	}
}


