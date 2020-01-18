import static org.junit.Assert.assertEquals;
//import org.junit.Assert;
import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnitTestQueue 
{
	
		CircularDoublyLinkedQueue cPQ = new CircularDoublyLinkedQueue();
		CircularDoublyLinkedQueue cPQ1 = new CircularDoublyLinkedQueue();	
		ProcessAttributes[] pArray,result;
		   
		
		@Test
		
		void queueSizeTest()
		{
			assertEquals(cPQ.elementsInQueue(), 0);
		}
		
		void printEmptyQueueTest() 
		{
			assertEquals(cPQ.getProcessObjectsInArray(), 0);
		}
		
		void getFirstElementTest() 
		{
			assertNull(cPQ.getFirstElement());
		}
		void deleteFirstElementTest() 
		{
			assertNull(cPQ.deleteFirstElement());
		}
		
		void testFIFO() 
		{
			cPQ.insertElementsIntoQueue(new ProcessAttributes("eclipse","1abc",22345,1578,0.91,"01:07:88"));
			assertEquals(cPQ.elementsInQueue(),1);
			cPQ.insertElementsIntoQueue(new ProcessAttributes("avast","babc",12345,3178,0.51,"01:07:89"));
			assertEquals(cPQ.elementsInQueue(),2);
			cPQ.insertElementsIntoQueue(new ProcessAttributes("googlechrome","kabc",32345,3878,0.10,"01:06:89"));
			assertEquals(cPQ.elementsInQueue(),3);
			cPQ.insertElementsIntoQueue(new ProcessAttributes("adobe","0abc",2345,3378,0.11,"00:07:89"));
			assertEquals("eclipse", cPQ.deleteFirstElement().name);
			assertEquals("avast", cPQ.deleteFirstElement().name);
			assertEquals("googlechrome", cPQ.deleteFirstElement().name);
			assertEquals("adobe", cPQ.deleteFirstElement().name);
		}
			
		void testDeletionAfterQueueFull()
		{
			
			cPQ.insertElementsIntoQueue(new ProcessAttributes("eclipse","1abc",22345,1578,0.91,"01:07:88"));
			assertEquals(cPQ.elementsInQueue(),1);
			cPQ.insertElementsIntoQueue(new ProcessAttributes("avast","babc",12345,3178,0.51,"01:07:89"));
			assertEquals(cPQ.elementsInQueue(),2);
			cPQ.deleteFirstElement();
			cPQ.deleteFirstElement();
			assertEquals(cPQ.elementsInQueue(),0);
			cPQ.insertElementsIntoQueue(new ProcessAttributes("googlechrome","kabc",32345,3878,0.10,"01:06:89"));
			assertEquals(cPQ.elementsInQueue(),1);
			assertEquals("googlechrome", cPQ.getFirstElement().name);
			System.out.println("Hello");
		}
		void testSortbyName()
		{
			cPQ1.insertElementsIntoQueue(new ProcessAttributes("eclipse","1abc",22345,1578,0.91,"01:07:88"));
			
			cPQ1.insertElementsIntoQueue(new ProcessAttributes("avast","babc",12345,3178,0.51,"01:07:89"));
			
			cPQ1.insertElementsIntoQueue(new ProcessAttributes("googlechrome","kabc",32345,3878,0.10,"01:06:89"));
			
			cPQ1.insertElementsIntoQueue(new ProcessAttributes("adobe","0abc",2345,3378,0.11,"00:07:89"));
			
			pArray=cPQ1.getProcessObjectsInArray();
			String[] sortedArray = {"adobe","avast","eclipse","googlechrome"};
					
			result = cPQ1.sortByName(pArray);
			
			for(int index=0;index<sortedArray.length;index++)
			
				assertEquals(result[index].name, sortedArray[index]);
			
				
		}
		
		void testSortByOwner()
		{
			String[] sortedArray = {"0abc", "1abc", "babc", "kabc"};
			
			result = cPQ1.sortByOwner(pArray);
			
			for(int index=0;index<sortedArray.length;index++)
				assertEquals(result[index].owner, sortedArray[index]);
			
		}
		void testSortByPID()
		{
			Integer[] sortedArray = {2345,1345,22345,32345};
			
			result = cPQ1.sortByPID(pArray);
			
			for(int index=0;index < sortedArray.length;index++)
				assertEquals(result[index].pid, sortedArray[index]);
					
		}
		void testSortByThreads()
		{
			int[] sortedArray1 = new int[]{1578,3178,3378,3878};
			
			result = cPQ1.sortByNumberOfThreads(pArray);
			
			for(int index=0;index<sortedArray1.length;index++)
				assertEquals(result[index].numberOfThreads, sortedArray1[index]);
			
		}
		void testSortByPercentageCPUTime()
		{
			Double[] sortedArray = {0.10,0.11,0.51,0.91};
			
			result = cPQ1.sortByPercentageCPU(pArray);
			
			for(int index=0;index<sortedArray.length;index++)
				assertEquals(result[index].percentageOfCPU, sortedArray[index]);
			
		}
		void testsortByTotalCPUTime()
		{
			String[] sortedArray= {"00:07:89","01:06:89","01:07:88","01:07:89"};
			
			result = cPQ1.sortByTotalCPUTime(pArray);
			
			for(int index=0;index<sortedArray.length;index++)
				assertEquals(result[index].totalCPUTime, sortedArray[index]);
			
		}
	}


