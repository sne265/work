import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BSTTesting 
{
		
	BinarySearchTree lexicalBST = new BinarySearchTree(new LexicalOrdering());
	BinarySearchTree revStrBST = new BinarySearchTree(new ReverseStringLexOrdering());
		
	@Test
	public void testEmptyTree()
	{
		assertTrue(lexicalBST.isEmpty());
	}
	
	@Test
	public void testInsertion()
	{
		lexicalBST.insert("east");
		revStrBST.insert("east");
		assertFalse(lexicalBST.isEmpty());
		assertFalse(revStrBST.isEmpty());
	}
	
	@Test
	public void testRealNode()
	{
		Node n = new RealNode("test");
		assertEquals(n.getValue(),"test");
		assertTrue(n.getLeft().isNull());
		assertTrue(n.getRight().isNull());
	}
	
	@Test
	public void testNullNode()
	{
		Node n = new NullNode();
		assertEquals(n.getValue(),"");
		assertEquals(n.getLeft(),null);
		assertEquals(n.getRight(),null);
	}
	
	@Test
	public void testLexicalOrdering()
	{
		String abc = "abc";
		String abcd = "abcd";
		String emptyString = " ";
		
		OrderingStrategy strategy = new LexicalOrdering();
		int result = strategy.compare(abc, abcd);
		assertTrue(result<0);
		
		result = strategy.compare(abc, emptyString);
		assertTrue(result>0);
		
		result = strategy.compare(emptyString, emptyString);
		assertTrue(result==0);
	}
	
	@Test
	public void testReverseStringLexicalOrdering()
	{
		String abcz = "abcz";
		String zebra = "zebra";
		String emptyString = " ";
		OrderingStrategy strategy = new ReverseStringLexOrdering();
		
		int result = strategy.compare(abcz, zebra);
		assertTrue(result>0);
		
		result = strategy.compare(emptyString, abcz);
		assertTrue(result<0);
		
		result = strategy.compare(abcz, abcz);
		assertTrue(result==0);
	}
	
	@Test
	public void testPreOrderLexicalTraversal()
	{
		lexicalBST.insert("east");
		lexicalBST.insert("applez");
		lexicalBST.insert("access");
		lexicalBST.insert("bulb");
		lexicalBST.insert("applez");
		lexicalBST.insert("easter");
		/*
		 creates the following binary search tree 
		 based on normal lexical ordering
		 		 
		 		 east
		 	    /    \
		 	 applez   easter
		 	  / \    
		access   bulb
		
		 */
		
		TreeVisitor visitor = new PreOrderVisitor();
		lexicalBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of (node(left subtree)(right sub tree))
		
		assertEquals(result,"(east(applez(access()())(bulb()()))(easter()()))");
	}
	
	@Test
	public void testPreOrderReverseLexicalTraversal()
	{
		revStrBST.insert("east");
		revStrBST.insert("applez");
		revStrBST.insert("access");
		revStrBST.insert("bulb");
		revStrBST.insert("applez");
		/*
		 creates the following binary search tree based on 
		 reversed string lexical ordering
		 
		 		east
		 	    /  \
		    access  applez
		      /
		   bulb
		    
		 */
		TreeVisitor visitor = new PreOrderVisitor();
		revStrBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of (node(left subtree)(right sub tree))
		
		assertEquals(result,"(east(access(bulb()())())(applez()()))");
		
	}
	
	@Test
	public void testPostOrderLexicalTraversal()
	{
		lexicalBST.insert("east");
		lexicalBST.insert("applez");
		lexicalBST.insert("applez");
		lexicalBST.insert("easter");
		/*
		 creates the following binary search tree 
		 based on normal lexical ordering
		 		 
		 		 east
		 	    /    \
		 	 applez   easter
		 	   		
		 */
		
		TreeVisitor visitor = new PostOrderVisitor();
		lexicalBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of ((left subtree)(right sub tree)node)
		
		assertEquals(result,"((()()applez)(()()easter)east)");
	}
	
	@Test
	public void testPostOrderReverseLexicalTraversal()
	{
		revStrBST.insert("east");
		revStrBST.insert("applez");
		revStrBST.insert("access");
		revStrBST.insert("bulb");
		revStrBST.insert("applez");
		/*
		 creates the following binary search tree based on 
		 reversed string lexical ordering
		 
		 		east
		 	    /  \
		    access  applez
		      /
		   bulb
		    
		 */
		TreeVisitor visitor = new PostOrderVisitor();
		revStrBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of ((left subtree)(right sub tree)node)
		
		assertEquals(result,"(((()()bulb)()access)(()()applez)east)");
		
	}
	@Test
	public void testInOrderLexicalTraversal()
	{
		lexicalBST.insert("east");
		lexicalBST.insert("applez");
		lexicalBST.insert("bulb");
		lexicalBST.insert("applez");
		lexicalBST.insert("easter");
		/*
		 creates the following binary search tree 
		 based on normal lexical ordering
		 		 
		 		 east
		 	    /    \
		 	 applez   easter
		 	   \    
		        bulb
		
		 */
		
		TreeVisitor visitor = new InOrderVisitor();
		lexicalBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of ((left subtree)node(right sub tree))
		
		assertEquals(result,"((()applez(()bulb()))east(()easter()))");
	}
	
	@Test
	public void testInOrderReverseLexicalTraversal()
	{
		//BinarySearchTree revStrBST = new BinarySearchTree(new ReverseStringLexOrdering());
		revStrBST.insert("east");
		revStrBST.insert("applez");
		revStrBST.insert("bulb");
		revStrBST.insert("applez");
		/*
		 creates the following binary search tree based on 
		 reversed string lexical ordering
		 
		 		east
		 	    /  \
		    bulb  applez
		     
		    
		 */
		TreeVisitor visitor = new InOrderVisitor();
		revStrBST.accept(visitor);
		String result = visitor.getTraversalSeq();
		// result will be in the form of ((left subtree)node(right sub tree))
		
		assertEquals(result,"((()bulb())east(()applez()))");
		
	}

}