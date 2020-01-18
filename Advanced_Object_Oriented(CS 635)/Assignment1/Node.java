/* Node : Class to define the structure of Node
 * A Node has 3 parts:prev,data,next
 * prev contains the previous node
 * data contains the process objects
 * next contains the next node
 */
public class Node 
{
		ProcessAttributes data;
	    Node next, prev;

	    /* Constructor to initialize an empty Node */
	    public Node()
	    {
	        next = null;
	        prev = null;
	        data = null;
	    }
	    /* Constructor to input data into a Node*/
	    public Node(ProcessAttributes processData, Node next, Node prev)
	    {
	        data = processData;
	        this.next = next;
	        this.prev = prev;
	    }
	    /* Function to assign link to next node */
	    public void setNext(Node next)
	    {
	        this.next = next;
	    }
	    /* Function to assign link to previous node */
	    public void setPrev(Node prev)
	    {
	        this.prev = prev;
	    }   
	    /* Function to get the next node */
	    public Node getNext()
	    {
	        return next;
	    }
	    /* Function to get the previous node */
	    public Node getPrev()
	    {
	        return prev;
	    }
	    /* Function to assign process object data to node */
	    public void setData(ProcessAttributes processData)
	    {
	        data = processData;
	    }
	    /* Function to get process object data from node */
	    public ProcessAttributes getData()
	    {
	        return data;
	    }

}


