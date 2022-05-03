package cobol;

import parse.*;
import parse.tokens.*;
public class FunctionAssembler extends Assembler {

	
	/**
	 * Pop a string, and set the target functionName to this
	 * string.
	 *
	 * @param   Assembly   the assembly to work on
	 */
	public void workOn(Assembly a) {
		System.out.println("[FunctionAssembler.workOn()] Running...");
		
		Cobol c = new Cobol();
		Token t = (Token) a.pop();
		System.out.println(t.sval().trim());
		c.addFunctionElement(t.sval().trim());
		a.setTarget(c);
		
	}

}
