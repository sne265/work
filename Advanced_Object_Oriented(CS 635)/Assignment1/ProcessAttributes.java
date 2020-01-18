/* Class that that is used define
 *  the attributes that the process objects hold*/
public class ProcessAttributes 
{
	// process attributes
	String name,owner,totalCPUTime;
	int pid,numberOfThreads; 
	double percentageOfCPU;
	
	/* default constructor to initialize process fields*/
	public ProcessAttributes()
	{
		name=null;
		owner=null;
		pid=0;
		numberOfThreads=0;
		percentageOfCPU=0.0;
		totalCPUTime=null;
		
	}
	/* constructor to accept process attribute values*/
	public ProcessAttributes(String name,String owner,
							int PID,int numberOfThreads,double PercentageOfCPU,
							String TotalCPUTime)
	{
		this.name=name;
		this.owner=owner;
		this.pid=PID;
		this.numberOfThreads=numberOfThreads;
		this.percentageOfCPU=PercentageOfCPU;
		this.totalCPUTime=TotalCPUTime;
	}
	/* toString function override
	 * to print process objects with its attributes
	 */
	 public String toString() 
	    { 
	        return "\nProcess name :" + name +
	        		"\nOwner :" + owner + 
	        		"\nPID :" + pid + 
	        		"\nNumber of Threads :" + numberOfThreads + 
	        		"\nPercentage of CPU :" + percentageOfCPU + "%" +
	        		"\nTotal CPU time :" + totalCPUTime;
	    }
}
