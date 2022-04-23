package cobol;

import parse.*;
import parse.tokens.*;
public class FunctionAssembler extends Assembler {

	@Override
	public void workOn(Assembly a) {
		Cobol c = new Cobol();
		Token t = (Token) a.pop();
		c.addFunctionElement(t.sval().trim());
		a.setTarget(c);
		
	}

}
