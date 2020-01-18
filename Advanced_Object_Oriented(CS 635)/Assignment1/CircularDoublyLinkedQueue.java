import java.util.Comparator;

public class CircularDoublyLinkedQueue 
{
	 	Node head;
	    Node tail ;
	    Node currentNode;
	    int totalElementsInQueue,capacityOfQueue;

	    /* Constructor to initialize instance variables 
	     * and to create nodes for an empty queue of given capacity*/
	    public CircularDoublyLinkedQueue()
	    {
	        
	    	head = null;
	        tail = null;
	        capacityOfQueue = 2;
	        totalElementsInQueue = 0;
	        createNodes();
	    }
	    
	    
	    /* Function to check if list is empty */
	    public boolean isEmpty()
	    {
	        return head == null;
	    }
	    
	    
	    /* Function to get number of elements currently in the queue */
	    public int elementsInQueue()
	    {
	        return totalElementsInQueue;
	    }
	    
	    
	    /*Function to create nodes of required capacity*/
	    public void createNodes()
	    {
	    	
	    	for(int size = elementsInQueue(); size < capacityOfQueue; size++)
	    	{
	    		// create an empty node of class Node
	    		Node newInputNode = new Node(null, null, null);
	    		
	    		// check if it the first node in the queue
	    		if (isEmpty())
	        	{
	    			/* set head and tail to the new node
	    			 *  since it is the first and only node available
	    			 */	    			
	    			newInputNode.setNext(newInputNode);
	    			newInputNode.setPrev(newInputNode);
	    			head = newInputNode;
	    			tail = newInputNode;
	    			
	        	}
	        	else
	        	{
	        		/* set new input node as the last node i.e tail node*/
	        		newInputNode.setPrev(tail);
	        		newInputNode.setNext(head);
	        		tail.setNext(newInputNode);
	        		head.setPrev(newInputNode);
	        		tail = newInputNode;   
	        	}
	    		
	    	}
	    }
	    
	    
	    /*Function to insert elements into queue from rear end of Queue */
	    public void insertElementsIntoQueue(ProcessAttributes newProcessData)
	    {
	    	// check if queue is full
	    	if (totalElementsInQueue == capacityOfQueue)
	        {
	        	
	        	// double the capacity of queue
	        	capacityOfQueue = 2*capacityOfQueue;
	        	
	        	// create empty node for queue of new capacity
	        	createNodes();
	        	
	        }
	    	// if there are no elements in queue 
    		//current Node should be the head node
	    	//otherwise current node should be the next node
	    	if(elementsInQueue()==0)
	    		
	    		currentNode = head;
	    	else
	    		currentNode = currentNode.getNext();
	    	
	    	// insert process object data into the current node
	    	currentNode.setData(newProcessData);
	    	
	    	totalElementsInQueue++;
	        		        
	    }
	    
	    public ProcessAttributes getFirstElement()
	    {
	    	// if queue is empty return null
	    	if (totalElementsInQueue == 0)
            {
                
                return null;
            }
	    	// head contains the first element in the queue
	    	ProcessAttributes firstProcess=head.getData();
	    	
	    	return firstProcess;
	    	//System.out.println(p.toString());
	    	
	    }
	   
	    
	    /* Function to delete node at position  */
	    public ProcessAttributes deleteFirstElement()
	    {
	    	// check if queue is empty
	    	if (totalElementsInQueue == 0)
	    	{
	    		return null;
	    	}
	    	// get first process object data that is to be deleted
	    	ProcessAttributes deletedProcess=head.getData();
	    	// set head data to null and move tail,head to next node
	    	head.setData(null);
	    	
	    	tail=head;
	    	
	    	head=head.getNext();
	    	// reduce the count of elements in queue
	    	totalElementsInQueue--;
	    	
	    	return deletedProcess;
	    	
	    }
	    
	    /*Function to sort process objects by process name*/
	    public ProcessAttributes[] sortByName(ProcessAttributes[] processObj)
	    {
	    	/* compName is an instance of SortByProcessName Comparator class
	    	 * which is used to sort process objects by its name
	    	 */
	    	Comparator<ProcessAttributes> compName = new SortByProcessName();
	    	
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compName);
	    	
	    	return processObj;
	    	
	    }
	    
	    public ProcessAttributes[] sortByOwner(ProcessAttributes[] processObj)
	    {

	    	/* compOwner is an instance of SortByProcessOwner Comparator class
	    	 * which is used to sort process objects by its owner
	    	 */
	    	Comparator<ProcessAttributes> compOwner = new SortByProcessOwner();
	    	
	    	/* sort process objects using quick sort*/
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compOwner);
	    	
	    	return processObj;
	    	
	    }
	    
	    
	    public ProcessAttributes[]  sortByPID(ProcessAttributes[] processObj)
	    {
	    	/* compPID is an instance of SortByPID Comparator class
	    	 * which is used to sort process objects by its PID
	    	 */
	    	Comparator<ProcessAttributes> compPID = new SortByPID();
	    	
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compPID);   
	    	
	    	return processObj;
	    	
	    }
	    
	    public ProcessAttributes[] sortByNumberOfThreads(ProcessAttributes[] processObj)
	    {
	    	/* compThreads is an instance of SortByNumberOfThreads Comparator class
	    	 * which is used to sort process objects by number of threads of the process
	    	 */
	    	Comparator<ProcessAttributes> compThreads = new SortByNumberOfThreads();
	    	
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compThreads);   
	    	
	    	return processObj;
	    	
	    }
	    
	    public ProcessAttributes[] sortByPercentageCPU(ProcessAttributes[] processObj)
	    {
	    	/* compPercentageCPU is an instance of SortByPercentageCPUTime Comparator class
	    	 * which is used to sort process objects by its percentage of CPU being used
	    	 */
	    	Comparator<ProcessAttributes> compPercentageCPU = new SortByPercentageCPUTime();
	    	
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compPercentageCPU);
	    	
	    	return processObj;
	    	
	    }
	    
	    
	    public ProcessAttributes[] sortByTotalCPUTime(ProcessAttributes[] processObj)
	    {
	    	/* compTotalCPUTime is an instance of SortByTotalCPUTime Comparator class
	    	 * which is used to sort process objects by its total CPU time
	    	 */
	    	Comparator<ProcessAttributes> compTotalCPUTime = new SortByTotalCPUTime();
	    	
	    	processObj = (new QuickSortObjects()).quickSort(processObj, 0, totalElementsInQueue-1, compTotalCPUTime);
	    	
	    	return processObj;
	    	
	    }
	    
	    public ProcessAttributes[] getProcessObjectsInArray()
	    {
	    	/*Function to retrieve process objects from 
	    	 * Circular Doubly Linked Queue into an array of process objects
	    	 * for sorting
	    	 */
	    	ProcessAttributes[] processArray=new ProcessAttributes[totalElementsInQueue];
	    	
	    	Node indexNode=head;
	    	
	    	for(int indexvalue=0;indexvalue<elementsInQueue();indexvalue++)
	    		{
	    			processArray[indexvalue]=indexNode.getData();
	    			indexNode=indexNode.getNext();
	    		}
	    	return processArray;
	    
	    }

}
