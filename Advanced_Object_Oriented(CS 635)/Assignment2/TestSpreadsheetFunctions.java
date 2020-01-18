//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestSpreadsheetFunctions {

	@Test
	void testExpreesionEvaluation() {
		Context context=new Context();
		EvaluatePostfixExpression exp = new EvaluatePostfixExpression("100 9 * 5 / 32 +");
		assertEquals(exp.evaluate(context),"212.0");
		exp = new EvaluatePostfixExpression("1 2 + sin");
		assertEquals(exp.evaluate(context),"0.1411200080598672");
		exp = new EvaluatePostfixExpression("10 lg");
		assertEquals(exp.evaluate(context),"3.3219280948873626");
	
	}
	 // Driver  method 
	   public static void main(String[] args)
	   {
		   new Spreadsheet();
		   
	   }
	   
}
